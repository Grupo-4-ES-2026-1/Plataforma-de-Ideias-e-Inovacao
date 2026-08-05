package PlataformaIdeiasInovacao.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User cadastrar(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado.");
        }
        
        if (user.getSenha() == null || user.getSenha().length() < 8) {
            throw new RuntimeException("A senha deve possuir pelo menos 8 caracteres.");
        }

        user.setSenha(passwordEncoder.encode(user.getSenha()));

        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
        }

        return userRepository.save(user);
    }

    public List<User> listarTodos() {
        return userRepository.findAll();
    }

    public Optional<User> buscarPorId(Long id) {
        return userRepository.findById(id);
    }

    public void excluir(Long id) {
        userRepository.deleteById(id);
    }
}
