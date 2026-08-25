package PlataformaIdeiasInovacao.proposta;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import PlataformaIdeiasInovacao.proposta.dto.StatusPropostaRequestDTO;

class StatusPropostaRequestDTOTest {

    @Test
    void deveDefinirERetornarStatus() {
        StatusPropostaRequestDTO dto = new StatusPropostaRequestDTO();

        dto.setStatus(StatusProposta.EM_ANALISE);

        assertThat(dto.getStatus()).isEqualTo(StatusProposta.EM_ANALISE);
    }
}