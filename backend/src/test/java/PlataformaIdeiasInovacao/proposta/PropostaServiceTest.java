package PlataformaIdeiasInovacao.proposta;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import PlataformaIdeiasInovacao.proposta.dto.PropostaRequestDTO;
import PlataformaIdeiasInovacao.proposta.dto.PropostaResponseDTO;
import PlataformaIdeiasInovacao.user.User;
import PlataformaIdeiasInovacao.user.UserRepository;
import PlataformaIdeiasInovacao.voto.VotoService;

@ExtendWith(MockitoExtension.class)
class PropostaServiceTest {

    @Mock
    private PropostaRepository propostaRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VotoService votoService;

    @InjectMocks
    private PropostaService propostaService;

    private static final String EMAIL_AUTOR = "autor@exemplo.com";

    @BeforeEach
    void autenticarUsuario() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(EMAIL_AUTOR, null)
        );
    }

    @AfterEach
    void limparContextoDeSeguranca() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveCadastrarPropostaComUsuarioAutenticado() {
        PropostaRequestDTO requestDTO = criarRequestDTO();

        User autor = new User("Autor Teste", EMAIL_AUTOR, "hash");
        autor.setId(1L);

        when(userRepository.findByEmail(EMAIL_AUTOR)).thenReturn(autor);

        when(propostaRepository.save(any(Proposta.class))).thenAnswer(invocation -> {
            Proposta proposta = invocation.getArgument(0);
            proposta.setId(10L);
            return proposta;
        });

        when(votoService.contarVotos(any(Long.class))).thenReturn(0L);

        PropostaResponseDTO response = propostaService.cadastrar(requestDTO);

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getTitulo()).isEqualTo("Melhoria no Wi-Fi");
        assertThat(response.getStatus()).isEqualTo("SUBMETIDA");
        assertThat(response.getAutorId()).isEqualTo(1L);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioAutenticadoNaoExisteNoBanco() {
        PropostaRequestDTO requestDTO = criarRequestDTO();

        when(userRepository.findByEmail(EMAIL_AUTOR)).thenReturn(null);

        assertThatThrownBy(() -> propostaService.cadastrar(requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("não encontrado");
    }

    @Test
    void deveListarTodasAsPropostasComOrdenacaoPadrao() {
        Proposta proposta1 = criarProposta(1L, "Proposta 1");
        Proposta proposta2 = criarProposta(2L, "Proposta 2");

        Sort ordenacaoEsperada =
                Sort.by(Sort.Direction.DESC, "dataCriacao");

        when(propostaRepository.findAll(ordenacaoEsperada))
                .thenReturn(List.of(proposta1, proposta2));

        when(votoService.contarVotos(any(Long.class))).thenReturn(0L);

        List<PropostaResponseDTO> resultado =
                propostaService.listarTodos(null, null);

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getTitulo()).isEqualTo("Proposta 1");
        assertThat(resultado.get(1).getTitulo()).isEqualTo("Proposta 2");

        verify(propostaRepository).findAll(ordenacaoEsperada);
    }

    @Test
    void deveFiltrarPropostasPorStatus() {
        Proposta proposta = criarProposta(1L, "Proposta Aprovada");
        proposta.setStatus(StatusProposta.APROVADA);

        Sort ordenacaoEsperada =
                Sort.by(Sort.Direction.DESC, "dataCriacao");

        when(propostaRepository.findByStatus(
                StatusProposta.APROVADA,
                ordenacaoEsperada
        )).thenReturn(List.of(proposta));

        when(votoService.contarVotos(1L)).thenReturn(0L);

        List<PropostaResponseDTO> resultado =
                propostaService.listarTodos(
                        StatusProposta.APROVADA,
                        null
                );

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getStatus()).isEqualTo("APROVADA");

        verify(propostaRepository).findByStatus(
                StatusProposta.APROVADA,
                ordenacaoEsperada
        );
    }

    @Test
    void deveOrdenarPorTituloAscendente() {
        Proposta proposta = criarProposta(1L, "Proposta A");

        Sort ordenacaoEsperada =
                Sort.by(Sort.Direction.ASC, "titulo");

        when(propostaRepository.findAll(ordenacaoEsperada))
                .thenReturn(List.of(proposta));

        when(votoService.contarVotos(1L)).thenReturn(0L);

        List<PropostaResponseDTO> resultado =
                propostaService.listarTodos(
                        null,
                        "titulo,asc"
                );

        assertThat(resultado).hasSize(1);

        verify(propostaRepository)
                .findAll(ordenacaoEsperada);
    }

    @Test
    void deveOrdenarPorDataCriacaoDescendente() {
        Proposta proposta = criarProposta(1L, "Proposta Teste");

        Sort ordenacaoEsperada =
                Sort.by(Sort.Direction.DESC, "dataCriacao");

        when(propostaRepository.findAll(ordenacaoEsperada))
                .thenReturn(List.of(proposta));

        when(votoService.contarVotos(1L)).thenReturn(0L);

        List<PropostaResponseDTO> resultado =
                propostaService.listarTodos(
                        null,
                        "dataCriacao,desc"
                );

        assertThat(resultado).hasSize(1);

        verify(propostaRepository)
                .findAll(ordenacaoEsperada);
    }

    @Test
    void deveUsarAscendenteQuandoDirecaoNaoForInformada() {
        Proposta proposta = criarProposta(1L, "Proposta Teste");

        Sort ordenacaoEsperada =
                Sort.by(Sort.Direction.ASC, "titulo");

        when(propostaRepository.findAll(ordenacaoEsperada))
                .thenReturn(List.of(proposta));

        when(votoService.contarVotos(1L)).thenReturn(0L);

        propostaService.listarTodos(null, "titulo");

        verify(propostaRepository)
                .findAll(ordenacaoEsperada);
    }

    @Test
    void deveBuscarPropostaPorIdQuandoExiste() {
        Proposta proposta = criarProposta(5L, "Proposta Encontrada");

        when(propostaRepository.findById(5L))
                .thenReturn(Optional.of(proposta));

        when(votoService.contarVotos(5L))
                .thenReturn(0L);

        Optional<PropostaResponseDTO> resultado =
                propostaService.buscarPorId(5L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTitulo())
                .isEqualTo("Proposta Encontrada");
    }

    @Test
    void deveRetornarVazioQuandoPropostaNaoExiste() {
        when(propostaRepository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<PropostaResponseDTO> resultado =
                propostaService.buscarPorId(99L);

        assertThat(resultado).isEmpty();
    }

    @Test
    void deveAtualizarStatusQuandoTransicaoForValida() {
        Proposta proposta = criarProposta(1L, "Proposta Teste");

        proposta.setStatus(StatusProposta.SUBMETIDA);

        when(propostaRepository.findById(1L))
                .thenReturn(Optional.of(proposta));

        when(propostaRepository.save(any(Proposta.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(votoService.contarVotos(1L))
                .thenReturn(0L);

        PropostaResponseDTO response =
                propostaService.atualizarStatus(
                        1L,
                        StatusProposta.EM_ANALISE
                );

        assertThat(response.getStatus())
                .isEqualTo("EM_ANALISE");

        assertThat(proposta.getStatus())
                .isEqualTo(StatusProposta.EM_ANALISE);
    }

    @Test
    void deveLancarExcecaoQuandoTransicaoForInvalida() {
        Proposta proposta = criarProposta(1L, "Proposta Teste");

        proposta.setStatus(StatusProposta.SUBMETIDA);

        when(propostaRepository.findById(1L))
                .thenReturn(Optional.of(proposta));

        assertThatThrownBy(() ->
                propostaService.atualizarStatus(
                        1L,
                        StatusProposta.IMPLANTADA
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Transição de status inválida.");
    }

    @Test
    void deveLancarExcecaoQuandoPropostaNaoForEncontradaAoAtualizarStatus() {
        when(propostaRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                propostaService.atualizarStatus(
                        999L,
                        StatusProposta.EM_ANALISE
                )
        )
                .isInstanceOf(PropostaNaoEncontradaException.class)
                .hasMessage("Proposta não encontrada.");
    }

    private PropostaRequestDTO criarRequestDTO() {
        PropostaRequestDTO dto = new PropostaRequestDTO();

        dto.setTitulo("Melhoria no Wi-Fi");
        dto.setDescricao("Instalar novos roteadores");
        dto.setCategoria("TECNOLOGIA");

        return dto;
    }

    private Proposta criarProposta(Long id, String titulo) {
        User autor = new User(
                "Autor de Teste",
                "autor@teste.com",
                "senha123"
        );

        autor.setId(1L);

        Proposta proposta = new Proposta();

        proposta.setId(id);
        proposta.setTitulo(titulo);
        proposta.setDescricao("Descrição qualquer");
        proposta.setCategoria("TECNOLOGIA");
        proposta.setStatus(StatusProposta.SUBMETIDA);
        proposta.setAutor(autor);

        return proposta;
    }
}