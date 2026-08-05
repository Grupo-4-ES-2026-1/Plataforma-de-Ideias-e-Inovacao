package PlataformaIdeiasInovacao.auth;

import PlataformaIdeiasInovacao.auth.dto.AuthenticationDTO;
import PlataformaIdeiasInovacao.auth.dto.LoginResponseDTO;
import PlataformaIdeiasInovacao.auth.dto.RegisterDTO;
import PlataformaIdeiasInovacao.security.TokenService;
import PlataformaIdeiasInovacao.user.User;
import PlataformaIdeiasInovacao.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    public void register(RegisterDTO data) {
        if (this.userRepository.findByEmail(data.email()) != null) {
            throw new IllegalArgumentException("Email já cadastrado na plataforma.");
        }

        String encryptedPassword = passwordEncoder.encode(data.password());
        User newUser = new User(data.nome(), data.email(), encryptedPassword);
        this.userRepository.save(newUser);
    }

    public LoginResponseDTO login(AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        return new LoginResponseDTO(token);
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }
}