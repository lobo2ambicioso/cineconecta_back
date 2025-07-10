package cineconecta.cineconecta.service.User;

import cineconecta.cineconecta.controllers.Users.AuthResponse;
import cineconecta.cineconecta.model.Users.User;
import cineconecta.cineconecta.repository.Users.UsersRepository;
import cineconecta.cineconecta.service.Jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServices {

    private final JwtService jwtService;
    private final UsersRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(User request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtService.getAccessToken(user);
        return AuthResponse.builder()
                .status("success")
                .token(token)
                .build();
    }
}
