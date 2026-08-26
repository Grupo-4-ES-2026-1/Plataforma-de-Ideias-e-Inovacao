package PlataformaIdeiasInovacao.voto;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import PlataformaIdeiasInovacao.user.User;
import PlataformaIdeiasInovacao.voto.dto.VotoResponseDTO;

@ExtendWith(MockitoExtension.class)
class VotoControllerTest {

    @Mock
    private VotoService votoService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private VotoController votoController;

    @Test
    void deveRegistrarVoto() {
        User usuario = new User("Fulano", "fulano@teste.com", "senha123");
        usuario.setId(1L);

        VotoResponseDTO responseDTO = new VotoResponseDTO();
        responseDTO.setId(10L);
        responseDTO.setPropostaId(1L);
        responseDTO.setUsuarioId(1L);

        when(authentication.getPrincipal()).thenReturn(usuario);
        when(votoService.votar(1L, usuario)).thenReturn(responseDTO);

        ResponseEntity<VotoResponseDTO> response =
                votoController.votar(1L, authentication);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(10L);
    }

    @Test
    void deveContarVotosDeUmaProposta() {
        when(votoService.contarVotos(1L)).thenReturn(3L);

        ResponseEntity<Long> response = votoController.contarVotos(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(3L);
    }
}