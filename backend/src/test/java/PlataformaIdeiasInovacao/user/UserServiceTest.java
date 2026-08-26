package PlataformaIdeiasInovacao.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

class UserTest {

    @Test
    void deveRetornarApenasRoleUserQuandoRoleForUser() {
        User user = new User("Fulano", "fulano@teste.com", "senha123");

        List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        assertThat(authorities).containsExactly("ROLE_USER");
    }

    @Test
    void deveRetornarRoleAdminERoleUserQuandoRoleForAdmin() {
        User user = new User("Fulano", "fulano@teste.com", "senha123");
        user.setRole("ADMIN");

        List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        assertThat(authorities).containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_USER");
    }

    @Test
    void deveRetornarApenasRoleUserQuandoRoleForNulo() {
        User user = new User("Fulano", "fulano@teste.com", "senha123");
        user.setRole(null);

        List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        assertThat(authorities).containsExactly("ROLE_USER");
    }
}