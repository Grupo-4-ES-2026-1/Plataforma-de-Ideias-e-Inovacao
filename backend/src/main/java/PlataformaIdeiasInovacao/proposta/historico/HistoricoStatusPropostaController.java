package PlataformaIdeiasInovacao.proposta.historico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import PlataformaIdeiasInovacao.proposta.historico.dto.HistoricoStatusPropostaResponseDTO;

@RestController
@RequestMapping("/propostas")
public class HistoricoStatusPropostaController {

    @Autowired
    private HistoricoStatusPropostaService historicoService;

    @GetMapping("/{id}/historico-status")
    public ResponseEntity<List<HistoricoStatusPropostaResponseDTO>> buscarHistorico(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                historicoService.buscarPorProposta(id)
        );
    }
}