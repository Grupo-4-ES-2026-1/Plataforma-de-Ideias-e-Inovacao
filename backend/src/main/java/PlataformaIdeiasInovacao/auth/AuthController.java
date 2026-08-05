package PlataformaIdeiasInovacao.auth;

import PlataformaIdeiasInovacao.user.User;
import PlataformaIdeiasInovacao.user.UserService;
import PlataformaIdeiasInovacao.user.dto.RegisterRequestDTO;
import PlataformaIdeiasInovacao.user.dto.UserResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    public AuthController() {
        System.out.println(">>> AuthController carregado!");
    }

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterRequestDTO dto) {

        System.out.println(">>> Register chamado!");

        User novoUsuario = userService.cadastrar(dto);

        UserResponseDTO response = new UserResponseDTO();

        response.setId(novoUsuario.getId());
        response.setNome(novoUsuario.getNome());
        response.setEmail(novoUsuario.getEmail());
        response.setRole(novoUsuario.getRole());

        return ResponseEntity.status(201).body(response);
    }

}