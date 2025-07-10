package cineconecta.cineconecta.controllers.Users;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    String status;
    String token;
    String message;
}
