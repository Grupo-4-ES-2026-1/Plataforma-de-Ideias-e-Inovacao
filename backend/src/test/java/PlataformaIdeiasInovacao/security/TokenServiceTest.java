package PlataformaIdeiasInovacao.security;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import PlataformaIdeiasInovacao.user.User;

class TokenServiceTest {

    private TokenService tokenService;
    private User usuario;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", "segredo-de-teste-123");

        usuario = new User("Laissa Gama", "laissa@exemplo.com", "hash-qualquer");
        usuario.setId(1L);
    }

    @Test
    void deveGerarTokenComEmailComoSubject() {
        String token = tokenService.generateToken(usuario);

        assertThat(token).isNotBlank();

        DecodedJWT decoded = JWT.decode(token);
        assertThat(decoded.getSubject()).isEqualTo("laissa@exemplo.com");
        assertThat(decoded.getIssuer()).isEqualTo("plataforma-ideias-api");
    }

    @Test
    void deveIncluirClaimsDeNomeERole() {
        String token = tokenService.generateToken(usuario);

        DecodedJWT decoded = JWT.decode(token);
        assertThat(decoded.getClaim("nome").asString()).isEqualTo("Laissa Gama");
        assertThat(decoded.getClaim("role").asString()).isEqualTo("USER");
    }

    @Test
    void deveValidarTokenGeradoERetornarSubject() {
        String token = tokenService.generateToken(usuario);

        String subject = tokenService.validateToken(token);

        assertThat(subject).isEqualTo("laissa@exemplo.com");
    }

    @Test
    void deveRetornarVazioParaTokenInvalido() {
        String subject = tokenService.validateToken("token.invalido.aqui");

        assertThat(subject).isEmpty();
    }
}