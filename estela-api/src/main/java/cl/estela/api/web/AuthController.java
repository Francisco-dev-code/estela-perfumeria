package cl.estela.api.web;

import cl.estela.api.domain.AppUser;
import cl.estela.api.domain.Role;
import cl.estela.api.dto.RegisterRequest;
import cl.estela.api.repo.AppUserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AppUserRepository users;
    private final PasswordEncoder encoder;

    public AuthController(AppUserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequest request) {
        if (users.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya esta registrado");
        }
        users.save(new AppUser(request.email().toLowerCase(), encoder.encode(request.password()),
                request.fullName(), Role.CLIENT));
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Cuenta creada"));
    }

    @GetMapping("/me")
    public Map<String, String> me(Principal principal) {
        AppUser user = users.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sesion no valida"));
        return Map.of("email", user.getEmail(), "name", user.getFullName(), "role", user.getRole().name());
    }
}
