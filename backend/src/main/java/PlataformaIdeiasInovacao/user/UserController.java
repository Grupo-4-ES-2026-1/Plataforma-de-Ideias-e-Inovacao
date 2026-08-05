package PlataformaIdeiasInovacao.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> cadastrar(@RequestBody User user) {

        User novoUsuario = userService.cadastrar(user);

        return ResponseEntity.status(201).body(novoUsuario);
    }
    

    @GetMapping
    public ResponseEntity<List<User>> listarTodos() {

        return ResponseEntity.ok(userService.listarTodos());

    }

    @GetMapping("/{id}")
    public ResponseEntity<User> buscarPorId(@PathVariable Long id) {

        return userService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        userService.excluir(id);

        return ResponseEntity.noContent().build();

    }

}