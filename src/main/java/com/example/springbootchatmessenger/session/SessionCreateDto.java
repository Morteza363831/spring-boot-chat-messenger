package com.example.springbootchatmessenger.session;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * DTO for {@link SessionEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionCreateDto implements Serializable {
    @NotBlank(message = "First username cannot be null")
    private String user1;
    @NotBlank(message = "Second username cannot be null")
    private String user2;
}