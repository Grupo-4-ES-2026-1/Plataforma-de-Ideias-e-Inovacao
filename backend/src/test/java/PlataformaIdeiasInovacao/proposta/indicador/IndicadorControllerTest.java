package PlataformaIdeiasInovacao.proposta.indicador;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import PlataformaIdeiasInovacao.proposta.StatusProposta;
import PlataformaIdeiasInovacao.proposta.indicador.dto.EngajamentoDTO;
import PlataformaIdeiasInovacao.proposta.indicador.dto.IndicadoresPropostasDTO;
import PlataformaIdeiasInovacao.proposta.indicador.dto.TaxaAprovacaoDTO;

@ExtendWith(MockitoExtension.class)
class IndicadorControllerTest {

    @Mock
    private IndicadorService indicadorService;

    @InjectMocks
    private IndicadorController indicadorController;

    @Test
    void deveRetornarIndicadoresGerais() {
        IndicadoresPropostasDTO dto = new IndicadoresPropostasDTO(
                5L, Map.of(StatusProposta.SUBMETIDA, 5L));

        when(indicadorService.buscarIndicadores(null, null, null)).thenReturn(dto);

        ResponseEntity<IndicadoresPropostasDTO> response =
                indicadorController.buscarIndicadores(null, null, null);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().totalPropostas()).isEqualTo(5L);
    }

    @Test
    void deveRetornarEngajamento() {
        EngajamentoDTO dto = new EngajamentoDTO(10L, 2.5);

        when(indicadorService.buscarEngajamento(null, null, null)).thenReturn(dto);

        ResponseEntity<EngajamentoDTO> response =
                indicadorController.buscarEngajamento(null, null, null);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().totalVotos()).isEqualTo(10L);
    }

    @Test
    void deveRetornarTaxaDeAprovacao() {
        TaxaAprovacaoDTO dto = new TaxaAprovacaoDTO(8L, 6L, 75.0);

        when(indicadorService.buscarTaxaAprovacao(null, null, null)).thenReturn(dto);

        ResponseEntity<TaxaAprovacaoDTO> response =
                indicadorController.buscarTaxaAprovacao(null, null, null);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().taxaAprovacao()).isEqualTo(75.0);
    }
}