package PlataformaIdeiasInovacao.proposta.indicador;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import PlataformaIdeiasInovacao.proposta.indicador.dto.EngajamentoDTO;
import PlataformaIdeiasInovacao.proposta.indicador.dto.IndicadoresPropostasDTO;
import PlataformaIdeiasInovacao.proposta.indicador.dto.TaxaAprovacaoDTO;

@RestController
@RequestMapping("/propostas/indicadores")
public class IndicadorController {

    private final IndicadorService indicadorService;

    public IndicadorController(IndicadorService indicadorService) {
        this.indicadorService = indicadorService;
    }

    @GetMapping
    public ResponseEntity<IndicadoresPropostasDTO> buscarIndicadores(

            @RequestParam(required = false)
            String categoria,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime dataInicial,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime dataFinal) {

        return ResponseEntity.ok(
                indicadorService.buscarIndicadores(
                        categoria,
                        dataInicial,
                        dataFinal
                )
        );
    }

    @GetMapping("/engajamento")
    public ResponseEntity<EngajamentoDTO> buscarEngajamento(

            @RequestParam(required = false)
            String categoria,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime dataInicial,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime dataFinal) {

        return ResponseEntity.ok(
                indicadorService.buscarEngajamento(
                        categoria,
                        dataInicial,
                        dataFinal
                )
        );
    }

    @GetMapping("/taxa-aprovacao")
    public ResponseEntity<TaxaAprovacaoDTO> buscarTaxaAprovacao(

            @RequestParam(required = false)
            String categoria,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime dataInicial,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime dataFinal) {

        return ResponseEntity.ok(
                indicadorService.buscarTaxaAprovacao(
                        categoria,
                        dataInicial,
                        dataFinal
                )
        );
    }
}