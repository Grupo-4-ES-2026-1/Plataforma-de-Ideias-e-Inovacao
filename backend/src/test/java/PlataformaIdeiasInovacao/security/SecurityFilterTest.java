package PlataformaIdeiasInovacao.security;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import PlataformaIdeiasInovacao.user.User;
import PlataformaIdeiasInovacao.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class SecurityFilterTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private SecurityFilter securityFilter;

    @BeforeEach
    void limparContextoDeSeguranca() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveSeguirSemAutenticarQuandoNaoHaHeaderDeAutorizacao() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        securityFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void deveSeguirSemAutenticarQuandoTokenForInvalido() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer token-invalido");
        when(tokenService.validateToken("token-invalido")).thenReturn("");

        securityFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void deveSeguirSemAutenticarQuandoUsuarioDoTokenNaoExiste() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer token-valido");
        when(tokenService.validateToken("token-valido")).thenReturn("fantasma@teste.com");
        when(userRepository.findByEmail("fantasma@teste.com")).thenReturn(null);

        securityFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void deveAutenticarQuandoTokenEUsuarioSaoValidos() throws Exception {
        User usuario = new User("Fulano", "fulano@teste.com", "senha123");
        usuario.setId(1L);

        when(request.getHeader("Authorization")).thenReturn("Bearer token-valido");
        when(tokenService.validateToken("token-valido")).thenReturn("fulano@teste.com");
        when(userRepository.findByEmail("fulano@teste.com")).thenReturn(usuario);

        securityFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .isEqualTo(usuario);
        verify(filterChain, times(1)).doFilter(request, response);
    }
}