package cineconecta.cineconecta.controllers.Users;

import cineconecta.cineconecta.model.Users.Cinephile;
import cineconecta.cineconecta.model.Users.User;
import cineconecta.cineconecta.service.User.AuthServices;
import cineconecta.cineconecta.service.User.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.util.function.ThrowingSupplier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserServices userServices;
    private final AuthServices authService;

    @GetMapping("/health")
    public ResponseEntity<String> checkHeaders() {
        return ResponseEntity.ok("cine conecta is up and running");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody User user) {
        return handleRequestProcess(() -> authService.login(user));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody Cinephile cinephile) {
        return handleRequestProcess(() -> {
            String response = userServices.registerUser(cinephile);
            return AuthResponse.builder().message(response).build();
        });
    }

    private ResponseEntity<AuthResponse> handleRequestProcess(ThrowingSupplier<AuthResponse> supplier) {
        try {
            return ResponseEntity.ok(supplier.get());
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body(AuthResponse.builder().message("Error: " + e).build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(AuthResponse.builder().message("Internal Server Error: " + e ).build());
        }
    }

}
