package com.example.springbootchatmessenger.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link SessionEntity}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionEntityDto implements Serializable {
    private Long id;
    private String username1;
    private String username2;
}