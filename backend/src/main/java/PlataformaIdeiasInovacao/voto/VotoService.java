package PlataformaIdeiasInovacao.voto;

import org.springframework.stereotype.Service;

import PlataformaIdeiasInovacao.proposta.Proposta;
import PlataformaIdeiasInovacao.proposta.PropostaRepository;
import PlataformaIdeiasInovacao.proposta.StatusProposta;
import PlataformaIdeiasInovacao.user.User;
import PlataformaIdeiasInovacao.voto.dto.VotoResponseDTO;

@Service
public class VotoService {

    private final VotoRepository votoRepository;
    private final PropostaRepository propostaRepository;

    public VotoService(VotoRepository votoRepository, PropostaRepository propostaRepository) {
        this.votoRepository = votoRepository;
        this.propostaRepository = propostaRepository;
    }

    public VotoResponseDTO votar(Long propostaId, User usuario) {

        Proposta proposta = propostaRepository.findById(propostaId)
                .orElseThrow(() -> new RuntimeException("Proposta não encontrada."));

        if (proposta.getStatus() != StatusProposta.SUBMETIDA
                && proposta.getStatus() != StatusProposta.EM_ANALISE) {
            throw new IllegalArgumentException(
                "Só é possível votar em propostas com status SUBMETIDA ou EM_ANALISE.");
        }

        if (votoRepository.existsByUsuarioAndProposta(usuario, proposta)) {
            throw new RuntimeException("Usuário já votou nesta proposta.");
        }

        Voto voto = new Voto();
        voto.setUsuario(usuario);
        voto.setProposta(proposta);

        Voto salvo = votoRepository.save(voto);

        VotoResponseDTO response = new VotoResponseDTO();

        response.setId(salvo.getId());
        response.setPropostaId(salvo.getProposta().getId());
        response.setUsuarioId(salvo.getUsuario().getId());

        return response;
    }
    public long contarVotos(Long propostaId) {
        Proposta proposta = propostaRepository.findById(propostaId)
                .orElseThrow(() -> new RuntimeException("Proposta não encontrada."));

        return votoRepository.countByProposta(proposta);
    }
}