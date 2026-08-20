package PlataformaIdeiasInovacao.proposta.dto;

import java.time.LocalDateTime;

public class PropostaResponseDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private String categoria;
    private String status;
    private Long autorId;
    private String autorNome;
    private LocalDateTime dataCriacao;
    private long numeroDeVotos;

    public long getNumeroDeVotos() {
        return numeroDeVotos;
    }

    public void setNumeroDeVotos(long numeroDeVotos) {
        this.numeroDeVotos = numeroDeVotos;
    }

    public String getAutorNome() {
        return autorNome;
    }

    public void setAutorNome(String autorNome) {
        this.autorNome = autorNome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getAutorId() {
        return autorId;
    }

    public void setAutorId(Long autorId) {
        this.autorId = autorId;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}