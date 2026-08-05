package PlataformaIdeiasInovacao.user;

import PlataformaIdeiasInovacao.user.dto.RegisterRequestDTO;
import PlataformaIdeiasInovacao.user.dto.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> cadastrar(@RequestBody RegisterRequestDTO dto) {

        User novoUsuario = userService.cadastrar(dto);

        UserResponseDTO response = new UserResponseDTO();

        response.setId(novoUsuario.getId());
        response.setNome(novoUsuario.getNome());
        response.setEmail(novoUsuario.getEmail());
        response.setRole(novoUsuario.getRole());

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listarTodos() {

        return ResponseEntity.ok(userService.listarTodosDTO());

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> buscarPorId(@PathVariable Long id) {

        return userService.buscarPorIdDTO(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        userService.excluir(id);

        return ResponseEntity.noContent().build();

    }

}