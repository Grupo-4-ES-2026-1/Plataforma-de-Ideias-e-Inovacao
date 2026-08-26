package PlataformaIdeiasInovacao.proposta.historico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import PlataformaIdeiasInovacao.proposta.PropostaNaoEncontradaException;
import PlataformaIdeiasInovacao.proposta.PropostaRepository;
import PlataformaIdeiasInovacao.proposta.historico.dto.HistoricoStatusPropostaResponseDTO;

@Service
public class HistoricoStatusPropostaService {

    @Autowired
    private HistoricoStatusPropostaRepository historicoRepository;

    @Autowired
    private PropostaRepository propostaRepository;

    public List<HistoricoStatusPropostaResponseDTO> buscarPorProposta(Long propostaId) {

        if (!propostaRepository.existsById(propostaId)) {
            throw new PropostaNaoEncontradaException("Proposta não encontrada.");
        }

        return historicoRepository
                .findByPropostaIdOrderByDataAsc(propostaId)
                .stream()
                .map(this::paraDTO)
                .toList();
    }

    private HistoricoStatusPropostaResponseDTO paraDTO(
            HistoricoStatusProposta historico) {

        HistoricoStatusPropostaResponseDTO dto =
                new HistoricoStatusPropostaResponseDTO();

        dto.setStatusAnterior(historico.getStatusAnterior());
        dto.setStatusNovo(historico.getStatusNovo());
        dto.setData(historico.getData());

        return dto;
    }
}