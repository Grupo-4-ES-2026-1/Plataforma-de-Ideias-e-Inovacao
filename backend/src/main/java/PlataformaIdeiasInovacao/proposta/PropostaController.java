package PlataformaIdeiasInovacao.proposta;

import PlataformaIdeiasInovacao.proposta.dto.*;
import PlataformaIdeiasInovacao.user.User;
import PlataformaIdeiasInovacao.user.dto.UserResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proposta")
public class PropostaController {

    @Autowired
   private PropostaService propostaService;

    @PostMapping("/register")
    public ResponseEntity<PropostaResponseDTO> register(@RequestBody @Valid PropostaRequestDTO data){
        PropostaResponseDTO response = this.propostaService.register(data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/propostas")
    public ResponseEntity<List<PropostaResponseDTO>> listarTodos() {
        List<PropostaResponseDTO> lista = propostaService.listarTodos().stream()
                .map(this::paraDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropostaResponseDTO> buscarPorId(@PathVariable Long id) {
        return PropostaService.buscarPorId(id)
                .map(this::paraDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    //conversor de Proposta para PropostaResponseDTO
    private PropostaResponseDTO paraDTO(Proposta proposta) {
        PropostaResponseDTO dto = new PropostaResponseDTO();
        // falta: Realizar a conversão
        return dto;
    }
}
