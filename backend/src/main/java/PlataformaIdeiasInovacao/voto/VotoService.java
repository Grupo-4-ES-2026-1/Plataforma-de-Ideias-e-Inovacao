package PlataformaIdeiasInovacao.voto;

import PlataformaIdeiasInovacao.proposta.Proposta;
import PlataformaIdeiasInovacao.proposta.PropostaRepository;
import PlataformaIdeiasInovacao.user.User;
import PlataformaIdeiasInovacao.voto.dto.VotoResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotoService {

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private PropostaRepository propostaRepository;

    public VotoResponseDTO votar(Long propostaId, User usuario) {

        Proposta proposta = propostaRepository.findById(propostaId)
                .orElseThrow(() -> new RuntimeException("Proposta não encontrada."));

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