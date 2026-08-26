package PlataformaIdeiasInovacao.proposta.historico;

import java.time.LocalDateTime;

import PlataformaIdeiasInovacao.proposta.Proposta;
import PlataformaIdeiasInovacao.proposta.StatusProposta;
import jakarta.persistence.*;

@Entity
@Table(name = "historico_status_proposta")
public class HistoricoStatusProposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "proposta_id", nullable = false)
    private Proposta proposta;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_anterior", nullable = false)
    private StatusProposta statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_novo", nullable = false)
    private StatusProposta statusNovo;

    @Column(nullable = false)
    private LocalDateTime data;

    public HistoricoStatusProposta() {
    }

    public HistoricoStatusProposta(
            Proposta proposta,
            StatusProposta statusAnterior,
            StatusProposta statusNovo) {

        this.proposta = proposta;
        this.statusAnterior = statusAnterior;
        this.statusNovo = statusNovo;
        this.data = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Proposta getProposta() {
        return proposta;
    }

    public void setProposta(Proposta proposta) {
        this.proposta = proposta;
    }

    public StatusProposta getStatusAnterior() {
        return statusAnterior;
    }

    public void setStatusAnterior(StatusProposta statusAnterior) {
        this.statusAnterior = statusAnterior;
    }

    public StatusProposta getStatusNovo() {
        return statusNovo;
    }

    public void setStatusNovo(StatusProposta statusNovo) {
        this.statusNovo = statusNovo;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
}