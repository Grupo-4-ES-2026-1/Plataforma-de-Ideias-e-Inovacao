package PlataformaIdeiasInovacao.proposta;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void deveRetornar404QuandoPropostaNaoForEncontrada() {
        PropostaNaoEncontradaException exception =
                new PropostaNaoEncontradaException("Proposta não encontrada.");

        ResponseEntity<String> response =
                handler.tratarPropostaNaoEncontrada(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Proposta não encontrada.");
    }

    @Test
    void deveRetornar400QuandoTransicaoForInvalida() {
        IllegalArgumentException exception =
                new IllegalArgumentException("Transição de status inválida.");

        ResponseEntity<String> response =
                handler.tratarTransicaoInvalida(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Transição de status inválida.");
    }
}