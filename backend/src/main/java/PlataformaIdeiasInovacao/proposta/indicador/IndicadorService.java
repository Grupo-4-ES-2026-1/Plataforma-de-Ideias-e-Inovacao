package PlataformaIdeiasInovacao.proposta.indicador;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import PlataformaIdeiasInovacao.proposta.PropostaRepository;
import PlataformaIdeiasInovacao.proposta.StatusProposta;
import PlataformaIdeiasInovacao.proposta.indicador.dto.IndicadoresPropostasDTO;
import PlataformaIdeiasInovacao.voto.VotoRepository;
import PlataformaIdeiasInovacao.proposta.indicador.dto.EngajamentoDTO;

@Service
public class IndicadorService {

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private VotoRepository votoRepository;

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
}