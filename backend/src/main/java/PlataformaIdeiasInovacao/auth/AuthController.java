package PlataformaIdeiasInovacao.auth;

import PlataformaIdeiasInovacao.user.User;
import PlataformaIdeiasInovacao.user.UserService;
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
    public ResponseEntity<User> register(@RequestBody User user) {

        System.out.println(">>> Register chamado!");

        User novoUsuario = userService.cadastrar(user);

        return ResponseEntity.status(201).body(novoUsuario);
    }

}