package PlataformaIdeiasInovacao.proposta.indicador;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import PlataformaIdeiasInovacao.proposta.PropostaRepository;
import PlataformaIdeiasInovacao.proposta.StatusProposta;
import PlataformaIdeiasInovacao.proposta.indicador.dto.EngajamentoDTO;
import PlataformaIdeiasInovacao.proposta.indicador.dto.IndicadoresPropostasDTO;
import PlataformaIdeiasInovacao.proposta.indicador.dto.TaxaAprovacaoDTO;
import PlataformaIdeiasInovacao.voto.VotoRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndicadorServiceTest {

    @Mock
    private PropostaRepository propostaRepository;

    @Mock
    private VotoRepository votoRepository;

    @InjectMocks
    private IndicadorService indicadorService;

    @Test
    void deveRetornarTotalEDistribuicaoPorStatus() {

        when(propostaRepository.contarTotalComFiltros(null, null, null))
                .thenReturn(5L);

        when(propostaRepository.contarPorStatusComFiltros(null, null, null))
                .thenReturn(List.of(
                        criarResultado(StatusProposta.SUBMETIDA, 2L),
                        criarResultado(StatusProposta.APROVADA, 2L),
                        criarResultado(StatusProposta.REJEITADA, 1L)
                ));

        IndicadoresPropostasDTO resultado =
                indicadorService.buscarIndicadores(null, null, null);

        assertThat(resultado.totalPropostas()).isEqualTo(5L);

        assertThat(
                resultado.distribuicaoPorStatus()
                        .get(StatusProposta.SUBMETIDA)
        ).isEqualTo(2L);

        assertThat(
                resultado.distribuicaoPorStatus()
                        .get(StatusProposta.APROVADA)
        ).isEqualTo(2L);

        assertThat(
                resultado.distribuicaoPorStatus()
                        .get(StatusProposta.REJEITADA)
        ).isEqualTo(1L);

        assertThat(
                resultado.distribuicaoPorStatus()
                        .get(StatusProposta.IMPLANTADA)
        ).isZero();
    }

    @Test
    void deveCalcularTaxaDeAprovacao() {

        when(propostaRepository.contarPorStatusComFiltros(null, null, null))
                .thenReturn(List.of(
                        criarResultado(StatusProposta.APROVADA, 3L),
                        criarResultado(StatusProposta.EM_IMPLANTACAO, 1L),
                        criarResultado(StatusProposta.IMPLANTADA, 2L),
                        criarResultado(StatusProposta.REJEITADA, 4L)
                ));

        TaxaAprovacaoDTO resultado =
                indicadorService.buscarTaxaAprovacao(null, null, null);

        assertThat(resultado.totalAvaliadas()).isEqualTo(10L);
        assertThat(resultado.totalAprovadas()).isEqualTo(6L);
        assertThat(resultado.taxaAprovacao()).isEqualTo(60.0);
    }

    @Test
    void deveRetornarTaxaZeroQuandoNaoExistiremPropostasAvaliadas() {

        when(propostaRepository.contarPorStatusComFiltros(null, null, null))
                .thenReturn(List.of());

        TaxaAprovacaoDTO resultado =
                indicadorService.buscarTaxaAprovacao(null, null, null);

        assertThat(resultado.totalAvaliadas()).isZero();
        assertThat(resultado.totalAprovadas()).isZero();
        assertThat(resultado.taxaAprovacao()).isZero();
    }

    @Test
    void deveCalcularEngajamento() {

        when(votoRepository.contarVotosComFiltros(null, null, null))
                .thenReturn(12L);

        when(propostaRepository.contarTotalComFiltros(null, null, null))
                .thenReturn(4L);

        EngajamentoDTO resultado =
                indicadorService.buscarEngajamento(null, null, null);

        assertThat(resultado.totalVotos()).isEqualTo(12L);
        assertThat(resultado.mediaVotosPorProposta()).isEqualTo(3.0);
    }

    @Test
    void deveRetornarMediaZeroQuandoNaoExistiremPropostas() {

        when(votoRepository.contarVotosComFiltros(null, null, null))
                .thenReturn(0L);

        when(propostaRepository.contarTotalComFiltros(null, null, null))
                .thenReturn(0L);

        EngajamentoDTO resultado =
                indicadorService.buscarEngajamento(null, null, null);

        assertThat(resultado.totalVotos()).isZero();
        assertThat(resultado.mediaVotosPorProposta()).isZero();
    }

    @Test
    void deveAplicarFiltrosNosIndicadores() {

        String categoria = "TECNOLOGIA";

        LocalDateTime inicio =
                LocalDateTime.of(2026, 8, 1, 0, 0);

        LocalDateTime fim =
                LocalDateTime.of(2026, 8, 31, 23, 59);

        when(propostaRepository.contarTotalComFiltros(
                categoria,
                inicio,
                fim
        )).thenReturn(2L);

        when(propostaRepository.contarPorStatusComFiltros(
                categoria,
                inicio,
                fim
        )).thenReturn(List.of(
                criarResultado(StatusProposta.APROVADA, 2L)
        ));

        indicadorService.buscarIndicadores(
                categoria,
                inicio,
                fim
        );

        verify(propostaRepository)
                .contarTotalComFiltros(
                        categoria,
                        inicio,
                        fim
                );

        verify(propostaRepository)
                .contarPorStatusComFiltros(
                        categoria,
                        inicio,
                        fim
                );
    }

    private StatusCountProjection criarResultado(
        StatusProposta status,
        Long total) {

        return new StatusCountProjection() {

            @Override
            public StatusProposta getStatus() {
                return status;
            }

            @Override
            public Long getTotal() {
                return total;
            }
        };
    }
}
