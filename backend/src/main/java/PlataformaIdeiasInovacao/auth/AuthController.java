package PlataformaIdeiasInovacao.auth;

import PlataformaIdeiasInovacao.auth.dto.AuthenticationDTO;
import PlataformaIdeiasInovacao.auth.dto.LoginResponseDTO;
import PlataformaIdeiasInovacao.auth.dto.RegisterDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDTO data) {

        System.out.println(">>> AUTH REGISTER FOI CHAMADO!");
        
        this.authService.register(data);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
        LoginResponseDTO response = this.authService.login(data);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        this.authService.logout();
        return ResponseEntity.noContent().build();
    }
}