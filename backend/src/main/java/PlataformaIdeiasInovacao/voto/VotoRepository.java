package PlataformaIdeiasInovacao.voto;

import java.time.LocalDateTime;

import PlataformaIdeiasInovacao.user.User;
import PlataformaIdeiasInovacao.proposta.Proposta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    boolean existsByUsuarioAndProposta(User usuario, Proposta proposta);

    long countByProposta(Proposta proposta);

    @Query("""
        SELECT COUNT(v)
        FROM Voto v
        JOIN v.proposta p
        WHERE (:categoria IS NULL OR p.categoria = :categoria)
        AND (:dataInicial IS NULL OR p.dataCriacao >= :dataInicial)
        AND (:dataFinal IS NULL OR p.dataCriacao <= :dataFinal)
    """)
    long contarVotosComFiltros(
            @Param("categoria") String categoria,
            @Param("dataInicial") LocalDateTime dataInicial,
            @Param("dataFinal") LocalDateTime dataFinal
    );
}