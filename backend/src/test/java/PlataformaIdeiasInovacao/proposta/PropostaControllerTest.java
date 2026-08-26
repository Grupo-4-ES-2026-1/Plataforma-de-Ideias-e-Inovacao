package PlataformaIdeiasInovacao.proposta;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import PlataformaIdeiasInovacao.proposta.dto.PropostaResponseDTO;
import PlataformaIdeiasInovacao.proposta.dto.StatusPropostaRequestDTO;

@ExtendWith(MockitoExtension.class)
class PropostaControllerTest {

    @Mock
    private PropostaService propostaService;

    @InjectMocks
    private PropostaController propostaController;

    @Test
    void deveListarTodasAsPropostasSemFiltros() {
        PropostaResponseDTO proposta = new PropostaResponseDTO();
        proposta.setId(1L);
        proposta.setTitulo("Proposta 1");
        proposta.setStatus("SUBMETIDA");

        when(propostaService.listarTodos(null, null))
                .thenReturn(List.of(proposta));

        ResponseEntity<List<PropostaResponseDTO>> response =
                propostaController.listarTodos(null, null);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getTitulo())
                .isEqualTo("Proposta 1");
    }

    @Test
    void deveListarPropostasFiltradasPorStatus() {
        PropostaResponseDTO proposta = new PropostaResponseDTO();
        proposta.setId(1L);
        proposta.setStatus("APROVADA");

        when(propostaService.listarTodos(
                StatusProposta.APROVADA,
                null
        )).thenReturn(List.of(proposta));

        ResponseEntity<List<PropostaResponseDTO>> response =
                propostaController.listarTodos(
                        StatusProposta.APROVADA,
                        null
                );

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getStatus())
                .isEqualTo("APROVADA");
    }

    @Test
    void deveListarPropostasComOrdenacaoInformada() {
        PropostaResponseDTO proposta = new PropostaResponseDTO();
        proposta.setId(1L);
        proposta.setTitulo("Proposta A");

        when(propostaService.listarTodos(
                null,
                "titulo,asc"
        )).thenReturn(List.of(proposta));

        ResponseEntity<List<PropostaResponseDTO>> response =
                propostaController.listarTodos(
                        null,
                        "titulo,asc"
                );

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getTitulo())
                .isEqualTo("Proposta A");
    }

    @Test
    void deveAtualizarStatusDaProposta() {
        StatusPropostaRequestDTO request =
                new StatusPropostaRequestDTO();

        request.setStatus(StatusProposta.EM_ANALISE);

        PropostaResponseDTO responseDTO =
                new PropostaResponseDTO();

        responseDTO.setId(1L);
        responseDTO.setStatus("EM_ANALISE");

        when(propostaService.atualizarStatus(
                1L,
                StatusProposta.EM_ANALISE
        )).thenReturn(responseDTO);

        ResponseEntity<PropostaResponseDTO> response =
                propostaController.atualizarStatus(
                        1L,
                        request
                );

        assertThat(response.getStatusCode().value())
                .isEqualTo(200);

        assertThat(response.getBody())
                .isNotNull();

        assertThat(response.getBody().getId())
                .isEqualTo(1L);

        assertThat(response.getBody().getStatus())
                .isEqualTo("EM_ANALISE");
    }
}