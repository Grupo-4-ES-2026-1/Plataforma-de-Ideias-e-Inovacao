package PlataformaIdeiasInovacao.voto;

import PlataformaIdeiasInovacao.proposta.Proposta;
import PlataformaIdeiasInovacao.user.User;
import jakarta.persistence.*;

@Entity
@Table(
    name = "votos",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"usuario_id", "proposta_id"})
    }
)
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "proposta_id", nullable = false)
    private Proposta proposta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public Proposta getProposta() {
        return proposta;
    }

    public void setProposta(Proposta proposta) {
        this.proposta = proposta;
    }
}