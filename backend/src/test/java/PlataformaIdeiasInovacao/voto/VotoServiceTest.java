package PlataformaIdeiasInovacao.voto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import PlataformaIdeiasInovacao.proposta.Proposta;
import PlataformaIdeiasInovacao.proposta.PropostaRepository;
import PlataformaIdeiasInovacao.proposta.StatusProposta;
import PlataformaIdeiasInovacao.user.User;
import PlataformaIdeiasInovacao.voto.dto.VotoResponseDTO;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private PropostaRepository propostaRepository;

    @InjectMocks
    private VotoService votoService;

    private Proposta criarProposta(Long id, StatusProposta status) {
        Proposta proposta = new Proposta();
        proposta.setId(id);
        proposta.setStatus(status);
        return proposta;
    }

    private User criarUsuario() {
        User usuario = new User("Usuário de Teste", "usuario@teste.com", "senha123");
        usuario.setId(1L);
        return usuario;
    }

    @Test
    void deveRegistrarVotoQuandoPropostaEstaSubmetida() {
        Proposta proposta = criarProposta(1L, StatusProposta.SUBMETIDA);
        User usuario = criarUsuario();

        when(propostaRepository.findById(1L)).thenReturn(java.util.Optional.of(proposta));
        when(votoRepository.existsByUsuarioAndProposta(usuario, proposta)).thenReturn(false);
        when(votoRepository.save(any(Voto.class))).thenAnswer(invocation -> {
            Voto voto = invocation.getArgument(0);
            voto.setId(10L);
            return voto;
        });

        VotoResponseDTO resultado = votoService.votar(1L, usuario);

        assertThat(resultado.getId()).isEqualTo(10L);
        assertThat(resultado.getPropostaId()).isEqualTo(1L);
        assertThat(resultado.getUsuarioId()).isEqualTo(1L);
    }

    @Test
    void deveRegistrarVotoQuandoPropostaEstaEmAnalise() {
        Proposta proposta = criarProposta(2L, StatusProposta.EM_ANALISE);
        User usuario = criarUsuario();

        when(propostaRepository.findById(2L)).thenReturn(java.util.Optional.of(proposta));
        when(votoRepository.existsByUsuarioAndProposta(usuario, proposta)).thenReturn(false);
        when(votoRepository.save(any(Voto.class))).thenAnswer(invocation -> {
            Voto voto = invocation.getArgument(0);
            voto.setId(11L);
            return voto;
        });

        VotoResponseDTO resultado = votoService.votar(2L, usuario);

        assertThat(resultado.getId()).isEqualTo(11L);
    }

    @Test
    void naoDeveRegistrarVotoQuandoPropostaNaoEstaEmStatusVotavel() {
        Proposta proposta = criarProposta(3L, StatusProposta.APROVADA);
        User usuario = criarUsuario();

        when(propostaRepository.findById(3L)).thenReturn(java.util.Optional.of(proposta));

        assertThatThrownBy(() -> votoService.votar(3L, usuario))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("SUBMETIDA ou EM_ANALISE");
    }

    @Test
    void naoDeveRegistrarVotoQuandoPropostaNaoExiste() {
        User usuario = criarUsuario();

        when(propostaRepository.findById(99L)).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> votoService.votar(99L, usuario))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("não encontrada");
    }

    @Test
    void naoDeveRegistrarVotoQuandoUsuarioJaVotou() {
        Proposta proposta = criarProposta(1L, StatusProposta.SUBMETIDA);
        User usuario = criarUsuario();

        when(propostaRepository.findById(1L)).thenReturn(java.util.Optional.of(proposta));
        when(votoRepository.existsByUsuarioAndProposta(usuario, proposta)).thenReturn(true);

        assertThatThrownBy(() -> votoService.votar(1L, usuario))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("já votou");
    }

    @Test
    void deveContarVotosDeUmaProposta() {
        Proposta proposta = criarProposta(1L, StatusProposta.EM_ANALISE);

        when(propostaRepository.findById(1L)).thenReturn(java.util.Optional.of(proposta));
        when(votoRepository.countByProposta(proposta)).thenReturn(5L);

        long total = votoService.contarVotos(1L);

        assertThat(total).isEqualTo(5L);
    }

    @Test
    void naoDeveContarVotosQuandoPropostaNaoExiste() {
        when(propostaRepository.findById(99L)).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> votoService.contarVotos(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("não encontrada");
    }

    @Test
    void naoDeveRegistrarVotoQuandoUsuarioForAdmin() {
        User admin = criarUsuario();
        admin.setRole("ADMIN");

        assertThatThrownBy(() -> votoService.votar(1L, admin))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("conflito de interesse");
    }
}