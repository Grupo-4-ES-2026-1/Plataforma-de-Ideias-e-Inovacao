package PlataformaIdeiasInovacao.proposta.historico;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoStatusPropostaRepository
        extends JpaRepository<HistoricoStatusProposta, Long> {

    List<HistoricoStatusProposta>
            findByPropostaIdOrderByDataAsc(Long propostaId);
}