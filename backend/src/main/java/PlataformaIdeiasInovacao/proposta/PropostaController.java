package PlataformaIdeiasInovacao.proposta;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import PlataformaIdeiasInovacao.proposta.dto.PropostaRequestDTO;
import PlataformaIdeiasInovacao.proposta.dto.PropostaResponseDTO;
import PlataformaIdeiasInovacao.proposta.dto.StatusPropostaRequestDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    private final PropostaService propostaService;

    public PropostaController(PropostaService propostaService) {
        this.propostaService = propostaService;
    }

    @PostMapping
    public ResponseEntity<PropostaResponseDTO> cadastrar(
            @RequestBody @Valid PropostaRequestDTO data) {

        PropostaResponseDTO response = propostaService.cadastrar(data);

        return ResponseEntity.ok(response);
    }

   @GetMapping
public ResponseEntity<List<PropostaResponseDTO>> listarTodos(
        @RequestParam(required = false) StatusProposta status,
        @RequestParam(required = false) String sort) {

    return ResponseEntity.ok(
            propostaService.listarTodos(status, sort)
    );
}

    @GetMapping("/minhas")
    public ResponseEntity<Page<PropostaResponseDTO>> buscarMinhasPropostas(
            @RequestParam(required = false) StatusProposta status,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime dataInicial,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime dataFinal,

            Pageable pageable) {

        return ResponseEntity.ok(
                propostaService.buscarMinhasPropostas(
                        status,
                        dataInicial,
                        dataFinal,
                        pageable
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropostaResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return propostaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PropostaResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestBody StatusPropostaRequestDTO data) {

        PropostaResponseDTO response =
                propostaService.atualizarStatus(id, data.getStatus());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<PropostaResponseDTO>> obterRanking() {
        List<PropostaResponseDTO> ranking = propostaService.obterRanking();
        return ResponseEntity.ok(ranking);
    }
}