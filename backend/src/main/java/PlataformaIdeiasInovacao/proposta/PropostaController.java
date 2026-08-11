package PlataformaIdeiasInovacao.proposta;


import PlataformaIdeiasInovacao.auth.dto.RegisterDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proposta")
public class PropostaController {

    @Autowired
   private PropostaService propostaService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid PropostaRequestDTO data){
        this.propostaService.register(data);
        return ResponseEntity.ok().build();
    }
}
