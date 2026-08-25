package PlataformaIdeiasInovacao.proposta.dto;

import PlataformaIdeiasInovacao.proposta.StatusProposta;

public class StatusPropostaRequestDTO {

    private StatusProposta status;

    public StatusProposta getStatus() {
        return status;
    }

    public void setStatus(StatusProposta status) {
        this.status = status;
    }
}