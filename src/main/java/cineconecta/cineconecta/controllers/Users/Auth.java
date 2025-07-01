package cineconecta.cineconecta.controllers.Users;

import cineconecta.cineconecta.controllers.Fimls.CineResponse;
import cineconecta.cineconecta.model.Users.Cinephile;
import cineconecta.cineconecta.service.User.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.util.function.ThrowingSupplier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class Auth {

    private final UserServices userServices;

    @GetMapping("/health")
    public ResponseEntity<String> checkHeaders() {
        return ResponseEntity.ok("cine conecta is up and running");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<CineResponse> registerUser(@RequestBody Cinephile cinephile) {
        return handleRequestProcess(() -> {
            String response = userServices.registerUser(cinephile);
            return CineResponse.builder().message(response).build();
        });
    }

    private ResponseEntity<CineResponse> handleRequestProcess(ThrowingSupplier<CineResponse> supplier) {
        try {
            return ResponseEntity.ok(supplier.get());
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body(CineResponse.builder().message("Error to send email").build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(CineResponse.builder().message("Internal Server Error: " + e ).build());
        }
    }

}
