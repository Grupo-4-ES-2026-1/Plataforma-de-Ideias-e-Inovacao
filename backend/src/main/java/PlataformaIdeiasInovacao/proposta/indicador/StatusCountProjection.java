package PlataformaIdeiasInovacao.proposta.indicador;

import PlataformaIdeiasInovacao.proposta.StatusProposta;

public interface StatusCountProjection {

    StatusProposta getStatus();

    Long getTotal();
}