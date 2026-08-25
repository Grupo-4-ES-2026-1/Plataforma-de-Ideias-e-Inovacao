package PlataformaIdeiasInovacao.proposta;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class StatusPropostaTest {

    @Test
    void submetidaDevePermitirEmAnaliseERejeitada() {
        assertThat(StatusProposta.SUBMETIDA.proximosStatus())
                .containsExactly(
                        StatusProposta.EM_ANALISE,
                        StatusProposta.REJEITADA
                );
    }

    @Test
    void emAnaliseDevePermitirAprovadaERejeitada() {
        assertThat(StatusProposta.EM_ANALISE.proximosStatus())
                .containsExactly(
                        StatusProposta.APROVADA,
                        StatusProposta.REJEITADA
                );
    }

    @Test
    void aprovadaDevePermitirEmImplantacao() {
        assertThat(StatusProposta.APROVADA.proximosStatus())
                .containsExactly(StatusProposta.EM_IMPLANTACAO);
    }

    @Test
    void emImplantacaoDevePermitirImplantada() {
        assertThat(StatusProposta.EM_IMPLANTACAO.proximosStatus())
                .containsExactly(StatusProposta.IMPLANTADA);
    }

    @Test
    void implantadaERejeitadaNaoDevemPermitirNovosStatus() {
        assertThat(StatusProposta.IMPLANTADA.proximosStatus()).isEmpty();
        assertThat(StatusProposta.REJEITADA.proximosStatus()).isEmpty();
    }

    @Test
    void deveValidarTransicaoPermitida() {
        assertThat(
                StatusProposta.SUBMETIDA
                        .podeTransicionarPara(StatusProposta.EM_ANALISE)
        ).isTrue();
    }

    @Test
    void deveRejeitarTransicaoNaoPermitida() {
        assertThat(
                StatusProposta.SUBMETIDA
                        .podeTransicionarPara(StatusProposta.IMPLANTADA)
        ).isFalse();
    }
}