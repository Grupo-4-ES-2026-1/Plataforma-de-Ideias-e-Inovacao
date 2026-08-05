package PlataformaIdeiasInovacao.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import PlataformaIdeiasInovacao.user.dto.RegisterRequestDTO;
import PlataformaIdeiasInovacao.user.dto.UserResponseDTO;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User cadastrar(RegisterRequestDTO dto) {

        User user = new User();

        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setSenha(dto.getSenha());
        user.setRole(dto.getRole());

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

    public List<UserResponseDTO> listarTodosDTO() {

        return userRepository.findAll().stream().map(user -> {

            UserResponseDTO dto = new UserResponseDTO();

            dto.setId(user.getId());
            dto.setNome(user.getNome());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole());

            return dto;

        }).toList();

    }

    public Optional<UserResponseDTO> buscarPorIdDTO(Long id) {

        return userRepository.findById(id).map(user -> {

            UserResponseDTO dto = new UserResponseDTO();

            dto.setId(user.getId());
            dto.setNome(user.getNome());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole());

            return dto;

        });

    }
}
