package PlataformaIdeiasInovacao.proposta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import PlataformaIdeiasInovacao.proposta.historico.HistoricoStatusProposta;
import PlataformaIdeiasInovacao.proposta.historico.HistoricoStatusPropostaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private final PropostaRepository propostaRepository;
    private final HistoricoStatusPropostaRepository historicoStatusRepository;
    private final VotoService votoService;
    private final UserRepository userRepository;

    public PropostaService(
            PropostaRepository propostaRepository,
            HistoricoStatusPropostaRepository historicoStatusRepository,
            VotoService votoService,
            UserRepository userRepository) {
        this.propostaRepository = propostaRepository;
        this.historicoStatusRepository = historicoStatusRepository;
        this.votoService = votoService;
        this.userRepository = userRepository;
    }

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

    public List<PropostaResponseDTO> listarTodos(
            StatusProposta status,
            String sort) {

        Sort ordenacao = criarOrdenacao(sort);

        List<Proposta> propostas;

        if (status == null) {
            propostas = propostaRepository.findAll(ordenacao);
        } else {
            propostas = propostaRepository.findByStatus(status, ordenacao);
        }

        return propostas.stream()
                .map(this::paraDTO)
                .toList();
    }

    private Sort criarOrdenacao(String sort) {

        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "dataCriacao");
        }

        String[] partes = sort.split(",");

        String campo = partes[0];

        Sort.Direction direcao = Sort.Direction.ASC;

        if (partes.length > 1
                && partes[1].equalsIgnoreCase("desc")) {

            direcao = Sort.Direction.DESC;
        }

        return Sort.by(direcao, campo);
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

    // Dentro da classe PropostaService, adicione:

    public List<PropostaResponseDTO> obterRanking() {
        List<Proposta> propostasRanking = propostaRepository.buscarRankingPorStatus(
                StatusProposta.SUBMETIDA,
                StatusProposta.EM_ANALISE
        );

        return propostasRanking.stream()
                .map(this::paraDTO)
                .toList();
    }
}