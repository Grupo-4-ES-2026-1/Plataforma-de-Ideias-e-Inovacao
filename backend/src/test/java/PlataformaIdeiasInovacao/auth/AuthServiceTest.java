package PlataformaIdeiasInovacao.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import PlataformaIdeiasInovacao.auth.dto.AuthenticationDTO;
import PlataformaIdeiasInovacao.auth.dto.LoginResponseDTO;
import PlataformaIdeiasInovacao.auth.dto.RegisterDTO;
import PlataformaIdeiasInovacao.security.TokenService;
import PlataformaIdeiasInovacao.user.User;
import PlataformaIdeiasInovacao.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthService authService;

    private RegisterDTO registerDTO;

    @BeforeEach
    void setUp() {
        registerDTO = new RegisterDTO("Laissa Gama", "laissa@exemplo.com", "senha123");
    }

    @Test
    void deveCadastrarNovoUsuarioComSucesso() {
        when(userRepository.findByEmail(registerDTO.email())).thenReturn(null);
        when(passwordEncoder.encode(registerDTO.password())).thenReturn("senha-criptografada");

        authService.register(registerDTO);

        verify(userRepository).save(argThat(user ->
                user.getNome().equals("Laissa Gama") &&
                user.getEmail().equals("laissa@exemplo.com") &&
                user.getPassword().equals("senha-criptografada") &&
                user.getRole().equals("USER")
        ));
    }

    @Test
    void deveLancarExcecaoAoCadastrarEmailJaExistente() {
        User usuarioExistente = new User("Outro Nome", registerDTO.email(), "hash-qualquer");
        when(userRepository.findByEmail(registerDTO.email())).thenReturn(usuarioExistente);

        assertThatThrownBy(() -> authService.register(registerDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email já cadastrado");

        verify(userRepository, never()).save(any());
    }

    @Test
    void deveRealizarLoginERetornarToken() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("laissa@exemplo.com", "senha123");
        User usuarioAutenticado = new User("Laissa Gama", "laissa@exemplo.com", "senha-criptografada");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(usuarioAutenticado);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(tokenService.generateToken(usuarioAutenticado)).thenReturn("token-jwt-falso");

        LoginResponseDTO response = authService.login(authenticationDTO);

        assertThat(response.token()).isEqualTo("token-jwt-falso");
        verify(tokenService).generateToken(usuarioAutenticado);
    }
}