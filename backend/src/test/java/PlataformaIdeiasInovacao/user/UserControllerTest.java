package PlataformaIdeiasInovacao.user;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import PlataformaIdeiasInovacao.user.dto.UserResponseDTO;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void deveListarTodosOsUsuarios() {
        User user = new User("Fulano", "fulano@teste.com", "senha123");
        user.setId(1L);

        when(userService.listarTodos()).thenReturn(List.of(user));

        ResponseEntity<List<UserResponseDTO>> response =
                userController.listarTodos();

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getNome()).isEqualTo("Fulano");
    }

    @Test
    void deveBuscarUsuarioPorIdQuandoExiste() {
        User user = new User("Fulano", "fulano@teste.com", "senha123");
        user.setId(1L);

        when(userService.buscarPorId(1L)).thenReturn(Optional.of(user));

        ResponseEntity<UserResponseDTO> response =
                userController.buscarPorId(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo("fulano@teste.com");
    }

    @Test
    void deveRetornar404QuandoUsuarioNaoExiste() {
        when(userService.buscarPorId(99L)).thenReturn(Optional.empty());

        ResponseEntity<UserResponseDTO> response =
                userController.buscarPorId(99L);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void deveExcluirUsuario() {
        ResponseEntity<Void> response = userController.excluir(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        verify(userService, times(1)).excluir(1L);
    }
}