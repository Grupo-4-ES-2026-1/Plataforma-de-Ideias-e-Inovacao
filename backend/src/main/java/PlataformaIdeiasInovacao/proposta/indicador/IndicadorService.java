package PlataformaIdeiasInovacao.proposta.indicador;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import PlataformaIdeiasInovacao.proposta.PropostaRepository;
import PlataformaIdeiasInovacao.proposta.StatusProposta;
import PlataformaIdeiasInovacao.proposta.indicador.dto.EngajamentoDTO;
import PlataformaIdeiasInovacao.proposta.indicador.dto.IndicadoresPropostasDTO;
import PlataformaIdeiasInovacao.proposta.indicador.dto.TaxaAprovacaoDTO;
import PlataformaIdeiasInovacao.voto.VotoRepository;

@Service
public class IndicadorService {

    private final PropostaRepository propostaRepository;
    private final VotoRepository votoRepository;

    public IndicadorService(PropostaRepository propostaRepository, VotoRepository votoRepository) {
        this.propostaRepository = propostaRepository;
        this.votoRepository = votoRepository;
    }

    public IndicadoresPropostasDTO buscarIndicadores(
            String categoria,
            LocalDateTime dataInicial,
            LocalDateTime dataFinal) {

        long total = propostaRepository.contarTotalComFiltros(
                categoria,
                dataInicial,
                dataFinal
        );

        Map<StatusProposta, Long> distribuicao =
                new EnumMap<>(StatusProposta.class);

        // Começa todos os status em zero
        for (StatusProposta status : StatusProposta.values()) {
            distribuicao.put(status, 0L);
        }

        // Substitui pelos valores encontrados no banco
        propostaRepository
                .contarPorStatusComFiltros(
                        categoria,
                        dataInicial,
                        dataFinal
                )
                .forEach(resultado ->
                        distribuicao.put(
                                resultado.getStatus(),
                                resultado.getTotal()
                        )
                );

        return new IndicadoresPropostasDTO(total, distribuicao);
    }

    public EngajamentoDTO buscarEngajamento(
        String categoria,
        LocalDateTime dataInicial,
        LocalDateTime dataFinal) {

        long totalVotos = votoRepository.contarVotosComFiltros(
                categoria,
                dataInicial,
                dataFinal
        );

        long totalPropostas = propostaRepository.contarTotalComFiltros(
                categoria,
                dataInicial,
                dataFinal
        );

        double media = totalPropostas == 0
                ? 0.0
                : (double) totalVotos / totalPropostas;

        return new EngajamentoDTO(
                totalVotos,
                media
        );
    }

    public TaxaAprovacaoDTO buscarTaxaAprovacao(
        String categoria,
        LocalDateTime dataInicial,
        LocalDateTime dataFinal) {

        Map<StatusProposta, Long> distribuicao =
                new EnumMap<>(StatusProposta.class);

        for (StatusProposta status : StatusProposta.values()) {
            distribuicao.put(status, 0L);
        }

        propostaRepository
                .contarPorStatusComFiltros(
                        categoria,
                        dataInicial,
                        dataFinal
                )
                .forEach(resultado ->
                        distribuicao.put(
                                resultado.getStatus(),
                                resultado.getTotal()
                        )
                );

        long aprovadas =
                distribuicao.get(StatusProposta.APROVADA)
                + distribuicao.get(StatusProposta.EM_IMPLANTACAO)
                + distribuicao.get(StatusProposta.IMPLANTADA);

        long rejeitadas =
                distribuicao.get(StatusProposta.REJEITADA);

        long avaliadas = aprovadas + rejeitadas;

        double taxa = avaliadas == 0
                ? 0.0
                : ((double) aprovadas / avaliadas) * 100.0;

        return new TaxaAprovacaoDTO(
                avaliadas,
                aprovadas,
                taxa
        );
    }
}