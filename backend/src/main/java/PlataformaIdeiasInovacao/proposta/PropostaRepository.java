package PlataformaIdeiasInovacao.proposta;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {

    List<Proposta> findByAutorId(Long autorId);

    @Query("""
        SELECT p FROM Proposta p
        WHERE p.autor.id = :autorId
        AND (:status IS NULL OR p.status = :status)
        AND (:dataInicial IS NULL OR p.dataCriacao >= :dataInicial)
        AND (:dataFinal IS NULL OR p.dataCriacao <= :dataFinal)
        ORDER BY p.dataCriacao DESC
    """)
    Page<Proposta> buscarMinhasPropostas(
            @Param("autorId") Long autorId,
            @Param("status") String status,
            @Param("dataInicial") LocalDateTime dataInicial,
            @Param("dataFinal") LocalDateTime dataFinal,
            Pageable pageable
    );
}