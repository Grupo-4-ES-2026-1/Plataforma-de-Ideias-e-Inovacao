package PlataformaIdeiasInovacao.proposta.indicador;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import PlataformaIdeiasInovacao.proposta.indicador.dto.EngajamentoDTO;
import PlataformaIdeiasInovacao.proposta.indicador.dto.IndicadoresPropostasDTO;
import PlataformaIdeiasInovacao.proposta.indicador.dto.TaxaAprovacaoDTO;

@RestController
@RequestMapping("/propostas/indicadores")
public class IndicadorController {

    @Autowired
    private IndicadorService indicadorService;

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
