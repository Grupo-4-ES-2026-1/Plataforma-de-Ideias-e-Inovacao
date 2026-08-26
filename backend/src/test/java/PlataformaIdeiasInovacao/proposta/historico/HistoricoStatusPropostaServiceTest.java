package PlataformaIdeiasInovacao.proposta.historico;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import PlataformaIdeiasInovacao.proposta.PropostaNaoEncontradaException;
import PlataformaIdeiasInovacao.proposta.PropostaRepository;
import PlataformaIdeiasInovacao.proposta.StatusProposta;
import PlataformaIdeiasInovacao.proposta.historico.dto.HistoricoStatusPropostaResponseDTO;

@ExtendWith(MockitoExtension.class)
class HistoricoStatusPropostaServiceTest {

    @Mock
    private HistoricoStatusPropostaRepository historicoRepository;

    @Mock
    private PropostaRepository propostaRepository;

    @InjectMocks
    private HistoricoStatusPropostaService historicoService;

    @Test
    void deveRetornarHistoricoOrdenadoQuandoPropostaExiste() {
        HistoricoStatusProposta item = new HistoricoStatusProposta();
        item.setStatusAnterior(StatusProposta.SUBMETIDA);
        item.setStatusNovo(StatusProposta.EM_ANALISE);
        item.setData(LocalDateTime.now());

        when(propostaRepository.existsById(1L)).thenReturn(true);
        when(historicoRepository.findByPropostaIdOrderByDataAsc(1L))
                .thenReturn(List.of(item));

        List<HistoricoStatusPropostaResponseDTO> resultado =
                historicoService.buscarPorProposta(1L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getStatusAnterior())
                .isEqualTo(StatusProposta.SUBMETIDA);
        assertThat(resultado.get(0).getStatusNovo())
                .isEqualTo(StatusProposta.EM_ANALISE);
    }

    @Test
    void deveLancarExcecaoQuandoPropostaNaoExiste() {
        when(propostaRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> historicoService.buscarPorProposta(99L))
                .isInstanceOf(PropostaNaoEncontradaException.class)
                .hasMessageContaining("não encontrada");
    }
}