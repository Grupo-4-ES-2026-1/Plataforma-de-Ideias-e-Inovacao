package PlataformaIdeiasInovacao.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email); // Mudou de UserDetails para User
    boolean existsByEmail(String email);
}