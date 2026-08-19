package PlataformaIdeiasInovacao.voto;

import PlataformaIdeiasInovacao.user.User;
import PlataformaIdeiasInovacao.proposta.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    boolean existsByUsuarioAndProposta(User usuario, Proposta proposta);
}