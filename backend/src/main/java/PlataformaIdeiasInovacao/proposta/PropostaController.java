package PlataformaIdeiasInovacao.proposta;

import PlataformaIdeiasInovacao.proposta.dto.PropostaRequestDTO;
import PlataformaIdeiasInovacao.proposta.dto.PropostaResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    @Autowired
    private PropostaService propostaService;

    @PostMapping
    public ResponseEntity<PropostaResponseDTO> cadastrar(@RequestBody @Valid PropostaRequestDTO data) {
        PropostaResponseDTO response = propostaService.cadastrar(data);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PropostaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(propostaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropostaResponseDTO> buscarPorId(@PathVariable Long id) {
        return propostaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}