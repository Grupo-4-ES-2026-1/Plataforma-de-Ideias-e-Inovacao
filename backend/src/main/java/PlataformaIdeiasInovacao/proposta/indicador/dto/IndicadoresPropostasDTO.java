package PlataformaIdeiasInovacao.proposta.indicador.dto;

import java.util.Map;

import PlataformaIdeiasInovacao.proposta.StatusProposta;

public record IndicadoresPropostasDTO(
        long totalPropostas,
        Map<StatusProposta, Long> distribuicaoPorStatus
) {}