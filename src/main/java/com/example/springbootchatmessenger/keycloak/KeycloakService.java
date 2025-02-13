package com.example.springbootchatmessenger.keycloak;

import com.example.springbootchatmessenger.user.UserEntityDto;
import com.nimbusds.jose.Header;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

/*
 * this class will handle keycloak requests and responses
 */

@Service
@Slf4j
public class KeycloakService {


    /*
     * @Value will get value of a variable in application.properties and store it in these variables !
     */
    @Value("${keycloak.auth-server-url}") // keycloak base url
    private String keycloakUrl;

    @Value("${keycloak.realm}") // keycloak realm name --> for all services which we want in this single-sign-on (sso)
    private String realm;

    /*
     * web messenger is just a service for now . but in future we will add some services to this sso and complain all of them !
     */
    @Value("${keycloak.client-id}") // keycloak client name --> for this service (web messenger service)
    private String clientId;

    @Value("${keycloak.client-secret}") // keycloak client secret --> for chat-messenger
    private String clientSecret;


    // injections
    private final RestTemplate restTemplate;
    private ResponseEntity<String> response;

    // constructor
    public KeycloakService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    /*
     * in this method we will get user details like : username , firstname , lastname , email and password
     * then we will create a new user with these details in keycloak .
     * --> the endpoint must be something like it : http://localhost:8080/admin/realms/{realm-name}/users
     * --> to create a new user we need a structure in json like :
     *
      {
           "username": "app-user-username",
           "firstName": "app-user-firstname",
           "lastName": "app_user_lastname",
           "email": "a1@gmail1.com",
           "emailVerified":true,
           "enabled": true,
           "credentials": [
               {
                   "type": "password",
                   "value": "app-user-password",
                   "temporary": false
               }
           ]
      }
     * --> and for authentication you need to send access-token with this request
     */
    public void registerUser(UserEntityDto userEntityDto) {
        String registerUserUrl = UriComponentsBuilder.fromHttpUrl(keycloakUrl)
                .pathSegment("admin" , "realms" , realm , "users")
                .toUriString();
        log.info("register url is : {} " , registerUserUrl); // print register url for debugging


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Optional<String> token = getToken().describeConstable();
        if (token.isPresent()) {

            headers.set("Authorization", "Bearer " + token.get());
            HttpEntity<Map<String,Object>> entity = new HttpEntity<>(mapUser(userEntityDto), headers);
            response = restTemplate.exchange(registerUserUrl, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("User registered successfully : {}", userEntityDto);
            } else {
                log.error("Error registering user: {}", response.getBody());
            }

        }

    }


    /*
     * get token from keycloak to give access to do something in application .
     * now we just use this token to make a new user .
     * But in future we will use it to open chats ( pages for users to chat with together)
     * and more ...
     */
    public String getToken() {
        String getTokenUrl = UriComponentsBuilder.fromHttpUrl(keycloakUrl)
                .pathSegment("realms", realm, "protocol", "openid-connect", "token")
                .toUriString();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "client_id=" + clientId + "&client_secret=" + clientSecret + "&grant_type=client_credentials";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        response = restTemplate.exchange(getTokenUrl, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Token obtained successfully : {}", response.getBody());
            String responseBody = response.getBody();
            return extractAccessToken(Objects.requireNonNull(responseBody));
        }
        log.error("Error obtaining token : {}", response.getStatusCode());
        return null;
    }



    private String extractAccessToken(String token) {
        int startIndex = token.indexOf("access_token\":\"") + "access_token\":\"".length();
        int endIndex = token.indexOf("\"", startIndex);
        return token.substring(startIndex, endIndex);
    }

    private Map<String, Object> mapUser(UserEntityDto userEntityDto) {
        Map<String, Object> user = new HashMap<>();
        user.put("username", userEntityDto.getUsername());
        user.put("enabled", true);
        user.put("email", userEntityDto.getEmail());
        user.put("emailVerified", userEntityDto.getEmailVerified());
        user.put("firstName", userEntityDto.getFirstName());
        user.put("lastName", userEntityDto.getLastName());
        user.put("credentials", List.of(Map.of("type", "password","value", userEntityDto.getPassword(), "temporary",false)));
        return user;
    }



}
