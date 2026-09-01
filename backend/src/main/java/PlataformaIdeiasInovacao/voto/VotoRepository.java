package PlataformaIdeiasInovacao.voto;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import PlataformaIdeiasInovacao.proposta.Proposta;
import PlataformaIdeiasInovacao.user.User;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    boolean existsByUsuarioAndProposta(User usuario, Proposta proposta);

    long countByProposta(Proposta proposta);

    @Query("""
        SELECT COUNT(v)
        FROM Voto v
        JOIN v.proposta p
        WHERE p.categoria = COALESCE(:categoria, p.categoria)
        AND p.dataCriacao >= COALESCE(:dataInicial, p.dataCriacao)
        AND p.dataCriacao <= COALESCE(:dataFinal, p.dataCriacao)
    """)
    long contarVotosComFiltros(
            @Param("categoria") String categoria,
            @Param("dataInicial") LocalDateTime dataInicial,
            @Param("dataFinal") LocalDateTime dataFinal
    );
}