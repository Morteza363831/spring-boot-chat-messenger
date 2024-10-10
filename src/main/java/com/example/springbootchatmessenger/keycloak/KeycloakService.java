package com.example.springbootchatmessenger.keycloak;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        ResponseEntity<String> response = restTemplate.exchange(getTokenUrl, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Token obtained successfully : {}", response.getBody());
            String responseBody = response.getBody();
            assert responseBody != null;
            int startIndex = responseBody.indexOf("access_token\":\"") + "access_token\":\"".length();
            int endIndex = responseBody.indexOf("\"", startIndex);
            return responseBody.substring(startIndex, endIndex);
        }
        log.error("Error obtaining token : {}", response.getStatusCode());
        return null;
    }

}
