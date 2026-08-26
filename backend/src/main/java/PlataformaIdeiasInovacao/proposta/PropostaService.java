package PlataformaIdeiasInovacao.proposta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import PlataformaIdeiasInovacao.proposta.historico.HistoricoStatusProposta;
import PlataformaIdeiasInovacao.proposta.historico.HistoricoStatusPropostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import PlataformaIdeiasInovacao.proposta.dto.PropostaRequestDTO;
import PlataformaIdeiasInovacao.proposta.dto.PropostaResponseDTO;
import PlataformaIdeiasInovacao.user.User;
import PlataformaIdeiasInovacao.user.UserRepository;
import PlataformaIdeiasInovacao.voto.VotoService;


@Service
public class PropostaService {

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private HistoricoStatusPropostaRepository historicoStatusRepository;

    @Autowired
    private VotoService votoService;

    @Autowired
    private UserRepository userRepository;

    public PropostaResponseDTO cadastrar(PropostaRequestDTO data) {
        Proposta proposta = new Proposta();

        proposta.setTitulo(data.getTitulo());
        proposta.setDescricao(data.getDescricao());
        proposta.setCategoria(data.getCategoria());
        proposta.setStatus(StatusProposta.SUBMETIDA);
        proposta.setDataCriacao(LocalDateTime.now());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User autor = userRepository.findByEmail(email);

        if (autor == null) {
            throw new RuntimeException("Usuário autenticado não encontrado.");
        }

        proposta.setAutor(autor);

        Proposta salva = propostaRepository.save(proposta);

        return paraDTO(salva);
    }

    public List<PropostaResponseDTO> listarTodos() {
        return propostaRepository.findAll().stream()
                .map(this::paraDTO)
                .toList();
    }

    public Optional<PropostaResponseDTO> buscarPorId(Long id) {
        return propostaRepository.findById(id)
                .map(this::paraDTO);
    }

    public Page<PropostaResponseDTO> buscarMinhasPropostas(
            StatusProposta status,
            LocalDateTime dataInicial,
            LocalDateTime dataFinal,
            Pageable pageable) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User autor = userRepository.findByEmail(email);

        if (autor == null) {
            throw new RuntimeException("Usuário autenticado não encontrado.");
        }

        Page<Proposta> propostas = propostaRepository.buscarMinhasPropostas(
                autor.getId(),
                status,
                dataInicial,
                dataFinal,
                pageable
        );

        return propostas.map(this::paraDTO);
    }

    @Transactional
    public PropostaResponseDTO atualizarStatus(Long id, StatusProposta novoStatus) {

        Proposta proposta = propostaRepository.findById(id)
                .orElseThrow(() ->
                        new PropostaNaoEncontradaException("Proposta não encontrada."));

        StatusProposta statusAnterior = proposta.getStatus();

        if (!statusAnterior.podeTransicionarPara(novoStatus)) {
            throw new IllegalArgumentException("Transição de status inválida.");
        }

        proposta.setStatus(novoStatus);

        Proposta atualizada = propostaRepository.save(proposta);

        HistoricoStatusProposta historico =
                new HistoricoStatusProposta(
                        atualizada,
                        statusAnterior,
                        novoStatus
                );

        historicoStatusRepository.save(historico);

        return paraDTO(atualizada);
    }

    private PropostaResponseDTO paraDTO(Proposta proposta) {
        PropostaResponseDTO dto = new PropostaResponseDTO();

        dto.setId(proposta.getId());
        dto.setTitulo(proposta.getTitulo());
        dto.setDescricao(proposta.getDescricao());
        dto.setCategoria(proposta.getCategoria());
        dto.setStatus(proposta.getStatus().name());
        dto.setDataCriacao(proposta.getDataCriacao());
        dto.setNumeroDeVotos(votoService.contarVotos(proposta.getId()));

        if (proposta.getAutor() != null) {
            dto.setAutorNome(proposta.getAutor().getNome());
            dto.setAutorId(proposta.getAutor().getId());
        }

        return dto;
    }
}