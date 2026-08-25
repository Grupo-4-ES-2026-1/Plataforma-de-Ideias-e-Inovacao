package PlataformaIdeiasInovacao.proposta;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PropostaNaoEncontradaException.class)
    public ResponseEntity<String> tratarPropostaNaoEncontrada(
            PropostaNaoEncontradaException exception) {

        return ResponseEntity
                .status(404)
                .body(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> tratarTransicaoInvalida(
            IllegalArgumentException exception) {

        return ResponseEntity
                .badRequest()
                .body(exception.getMessage());
    }
}