package PlataformaIdeiasInovacao.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import PlataformaIdeiasInovacao.auth.dto.AuthenticationDTO;
import PlataformaIdeiasInovacao.auth.dto.LoginResponseDTO;
import PlataformaIdeiasInovacao.auth.dto.RegisterDTO;
import PlataformaIdeiasInovacao.security.TokenService;
import PlataformaIdeiasInovacao.user.User;
import PlataformaIdeiasInovacao.user.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

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