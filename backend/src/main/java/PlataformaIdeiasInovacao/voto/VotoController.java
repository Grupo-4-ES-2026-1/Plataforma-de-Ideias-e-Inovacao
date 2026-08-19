package PlataformaIdeiasInovacao.voto;

import PlataformaIdeiasInovacao.user.User;
import PlataformaIdeiasInovacao.voto.dto.VotoResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/propostas")
public class VotoController {

    @Autowired
    private VotoService votoService;

    @PostMapping("/{propostaId}/voto")
    public ResponseEntity<VotoResponseDTO> votar(
        @PathVariable Long propostaId,
        Authentication authentication) {

            User usuario = (User) authentication.getPrincipal();

            VotoResponseDTO voto = votoService.votar(propostaId, usuario);

            return ResponseEntity.status(201).body(voto);
    }
}