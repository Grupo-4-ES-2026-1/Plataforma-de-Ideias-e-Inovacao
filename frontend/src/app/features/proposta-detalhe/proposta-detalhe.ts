import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { catchError, of } from 'rxjs';
import { PropostaService, PropostaResponse, HistoricoStatusItem } from '../../core/services/proposta';
import { AuthService } from '../../services/auth'; // Import do AuthService
import { StatusProposta, getLabelStatus, getProximosStatusDisponiveis } from '../../core/models/statusProposta';

export interface Comentario {
  id: number;
  autor: string;
  texto: string;
  data: Date;
}

@Component({
  selector: 'app-proposta-detalhe',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './proposta-detalhe.html',
  styleUrl: './proposta-detalhe.css'
})
export class PropostaDetalheComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly propostaService = inject(PropostaService);
  private readonly authService = inject(AuthService);

  proposta = signal<PropostaResponse | null>(null);
  carregando = signal(true);
  erro = signal('');

  comentarios = signal<Comentario[]>([]);

  // --- US10 #101 / US11 #104 / US12 #107 ---
  private propostaId = 0;

  historico = signal<HistoricoStatusItem[]>([]);
  carregandoHistorico = signal(true);

  alterandoStatus = signal(false);
  erroStatus = signal('');

  votando = signal(false);
  erroVoto = signal('');
  votoRegistrado = signal(false);

  /** Só é possível votar em propostas ainda em avaliação (mesma regra do backend). Quem avalia (ADMIN) não vota. */
  podeVotar = computed(() => {
    const p = this.proposta();
    return !!p
      && (p.status === 'SUBMETIDA' || p.status === 'EM_ANALISE')
      && !this.votoRegistrado()
      && this.authService.roleAtual() !== 'ADMIN';
  });

  /** #107 - só quem avalia propostas (ADMIN) pode alterar o status. */
  podeAlterarStatus = computed(() => this.authService.roleAtual() === 'ADMIN');

  /** #107 - próximos status possíveis a partir do status atual da proposta. */
  proximosStatus = computed(() => {
    const p = this.proposta();
    return p ? getProximosStatusDisponiveis(p.status) : [];
  });

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = idParam ? Number(idParam) : NaN;

    if (!id || Number.isNaN(id)) {
      this.erro.set('Proposta não encontrada.');
      this.carregando.set(false);
      this.carregandoHistorico.set(false);
      return;
    }

    this.propostaId = id;

    this.propostaService.buscarPorId(id).subscribe({
      next: (dados) => {
        this.proposta.set(dados);
        this.carregando.set(false);
      },
      error: () => {
        this.erro.set('Não foi possível carregar os detalhes da proposta.');
        this.carregando.set(false);
      }
    });

    this.carregarHistorico(id);
  }

  /**
   * US11 #104 - carrega o histórico de status da proposta.
   * Enquanto o endpoint #103 não existe no backend, cai num fallback
   * vazio (em vez de quebrar a tela) para não travar o desenvolvimento
   * do front nem o backend.
   */
  private carregarHistorico(id: number): void {
    this.carregandoHistorico.set(true);

    this.propostaService.buscarHistoricoStatus(id).pipe(
      catchError(() => of<HistoricoStatusItem[]>([]))
    ).subscribe((itens) => {
      this.historico.set(itens);
      this.carregandoHistorico.set(false);
    });
  }

  /**
   * US10 #101 - dispara a alteração de status da proposta.
   * Ao sucesso: atualiza o status exibido e recarrega o histórico.
   */
  alterarStatus(novoStatus: StatusProposta): void {
    if (!this.propostaId || this.alterandoStatus()) return;

    this.alterandoStatus.set(true);
    this.erroStatus.set('');

    this.propostaService.atualizarStatus(this.propostaId, novoStatus).subscribe({
      next: (atualizada) => {
        this.proposta.set(atualizada);
        this.alterandoStatus.set(false);
        this.carregarHistorico(this.propostaId);
      },
      error: () => {
        this.erroStatus.set('Não foi possível atualizar o status agora. Tente novamente.');
        this.alterandoStatus.set(false);
      }
    });
  }

  getLabelStatus(status: string): string {
    return getLabelStatus(status);
  }

  votar(): void {
    if (!this.propostaId || this.votando() || !this.podeVotar()) return;

    this.votando.set(true);
    this.erroVoto.set('');

    this.propostaService.votar(this.propostaId).subscribe({
      next: () => {
        this.votando.set(false);
        this.votoRegistrado.set(true);

        const atual = this.proposta();
        if (atual) {
          this.proposta.set({
            ...atual,
            numeroDeVotos: atual.numeroDeVotos + 1
          });
        }
      },
      error: (erro) => {
        this.votando.set(false);
        this.erroVoto.set(
          erro?.error && typeof erro.error === 'string'
            ? erro.error
            : 'Não foi possível registrar seu voto agora. Tente novamente.'
        );
      }
    });
  }

  //função para salvar o novo comentário
  adicionarComentario(texto: string): void {
    if (!texto.trim()) return; // Impede comentários vazios

    const usuarioAtual = this.authService.usuarioLogado();
    const nomeAutor = usuarioAtual ? usuarioAtual : 'Usuário Logado';

    const novoComentario: Comentario = {
      id: Date.now(),
      autor: nomeAutor,
      texto: texto,
      data: new Date()
    };

    this.comentarios.update(comentariosAtuais => [...comentariosAtuais, novoComentario]);
  }
}