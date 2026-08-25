package PlataformaIdeiasInovacao.proposta;

import java.util.List;

public enum StatusProposta {

    SUBMETIDA,
    EM_ANALISE,
    APROVADA,
    REJEITADA,
    EM_IMPLANTACAO,
    IMPLANTADA;

    public List<StatusProposta> proximosStatus() {
        return switch (this) {
            case SUBMETIDA -> List.of(EM_ANALISE, REJEITADA);
            case EM_ANALISE -> List.of(APROVADA, REJEITADA);
            case APROVADA -> List.of(EM_IMPLANTACAO);
            case EM_IMPLANTACAO -> List.of(IMPLANTADA);
            case IMPLANTADA, REJEITADA -> List.of();
        };
    }

    public boolean podeTransicionarPara(StatusProposta novoStatus) {
        return proximosStatus().contains(novoStatus);
    }
}