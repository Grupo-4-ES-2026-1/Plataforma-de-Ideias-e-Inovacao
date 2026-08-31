package PlataformaIdeiasInovacao.proposta;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {

    List<Proposta> findByAutorId(Long autorId);

    // US12 / #105
    // Permite filtrar as propostas por status e aplicar ordenação.
    List<Proposta> findByStatus(StatusProposta status, Sort sort);

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
            @Param("status") StatusProposta status,
            @Param("dataInicial") LocalDateTime dataInicial,
            @Param("dataFinal") LocalDateTime dataFinal,
            Pageable pageable
    );

    @Query("""
        SELECT p FROM Proposta p
        LEFT JOIN Voto v ON v.proposta = p
        WHERE p.status IN (:status1, :status2)
        AND (:categoria IS NULL OR p.categoria = :categoria)
        AND (:dataInicial IS NULL OR p.dataCriacao >= :dataInicial)
        AND (:dataFinal IS NULL OR p.dataCriacao <= :dataFinal)
        GROUP BY p.id
        ORDER BY COUNT(v.id) DESC, p.dataCriacao ASC
    """)
    List<Proposta> buscarRankingComFiltros(
            @Param("status1") StatusProposta status1,
            @Param("status2") StatusProposta status2,
            @Param("categoria") String categoria,
            @Param("dataInicial") LocalDateTime dataInicial,
            @Param("dataFinal") LocalDateTime dataFinal
    );
}