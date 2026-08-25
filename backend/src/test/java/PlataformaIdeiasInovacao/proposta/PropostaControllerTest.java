package PlataformaIdeiasInovacao.proposta;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
    void deveAtualizarStatusDaProposta() {
        StatusPropostaRequestDTO request = new StatusPropostaRequestDTO();
        request.setStatus(StatusProposta.EM_ANALISE);

        PropostaResponseDTO responseDTO = new PropostaResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setStatus("EM_ANALISE");

        when(propostaService.atualizarStatus(
                1L,
                StatusProposta.EM_ANALISE
        )).thenReturn(responseDTO);

        ResponseEntity<PropostaResponseDTO> response =
                propostaController.atualizarStatus(1L, request);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getStatus()).isEqualTo("EM_ANALISE");
    }
}
