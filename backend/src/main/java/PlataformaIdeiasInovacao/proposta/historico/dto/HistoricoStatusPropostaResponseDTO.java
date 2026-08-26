package PlataformaIdeiasInovacao.proposta.historico.dto;

import java.time.LocalDateTime;

import PlataformaIdeiasInovacao.proposta.StatusProposta;

public class HistoricoStatusPropostaResponseDTO {

    private StatusProposta statusAnterior;
    private StatusProposta statusNovo;
    private LocalDateTime data;

    public StatusProposta getStatusAnterior() {
        return statusAnterior;
    }

    public void setStatusAnterior(StatusProposta statusAnterior) {
        this.statusAnterior = statusAnterior;
    }

    public StatusProposta getStatusNovo() {
        return statusNovo;
    }

    public void setStatusNovo(StatusProposta statusNovo) {
        this.statusNovo = statusNovo;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
}