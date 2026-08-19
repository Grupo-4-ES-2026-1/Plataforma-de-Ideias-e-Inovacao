package PlataformaIdeiasInovacao.proposta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

@Service
public class PropostaService {

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private UserRepository userRepository;

    public PropostaResponseDTO cadastrar(PropostaRequestDTO data) {
        Proposta proposta = new Proposta();

        proposta.setTitulo(data.getTitulo());
        proposta.setDescricao(data.getDescricao());
        proposta.setCategoria(data.getCategoria());
        proposta.setStatus("SUBMETIDA");
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
            String status,
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

    private PropostaResponseDTO paraDTO(Proposta proposta) {
        PropostaResponseDTO dto = new PropostaResponseDTO();

        dto.setId(proposta.getId());
        dto.setTitulo(proposta.getTitulo());
        dto.setDescricao(proposta.getDescricao());
        dto.setCategoria(proposta.getCategoria());
        dto.setStatus(proposta.getStatus());
        dto.setDataCriacao(proposta.getDataCriacao());
        dto.setAutorNome(proposta.getAutor().getNome());

        if (proposta.getAutor() != null) {
            dto.setAutorId(proposta.getAutor().getId());
        }

        return dto;
    }
}
