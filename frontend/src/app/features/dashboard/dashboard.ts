import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PropostaService } from '../../core/services/proposta';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {
  private readonly propostaService = inject(PropostaService);

  carregando = signal(true);
  erro = signal('');

  
  totalPropostas = signal(0);//sinais para guardar as métricas
  distribuicao = signal({ submetida: 0, emAnalise: 0, aprovada: 0, rejeitada: 0 });
  taxaAprovacao = signal(0);
  totalVotos = signal(0);//sinal para o engajamento (total de votos)

  ngOnInit(): void {
    //busca todas as propostas para calcular as estatísticas
    this.propostaService.listar().subscribe({
      next: (propostas) => {
        // 1. calcula o total geral
        this.totalPropostas.set(propostas.length);

        // 2. calcula a distribuição por status
        const contagem = { submetida: 0, emAnalise: 0, aprovada: 0, rejeitada: 0 };
        
        propostas.forEach(p => {
          if (p.status === 'SUBMETIDA') contagem.submetida++;
          if (p.status === 'EM_ANALISE') contagem.emAnalise++;
          if (p.status === 'APROVADA') contagem.aprovada++;
          if (p.status === 'REJEITADA') contagem.rejeitada++;
        });
        
        this.distribuicao.set(contagem);
        
        //calcula a porcentagem de aprovação
        const total = propostas.length;
        const taxa = total > 0 ? (contagem.aprovada / total) * 100 : 0;
        
        //Math.round arredonda para não ficar com números quebrados 
        this.taxaAprovacao.set(Math.round(taxa)); 
        //calcula o engajamento somando os votos de todas as propostas
        const somaVotos = propostas.reduce((acumulador, proposta) => {
          return acumulador + (proposta.numeroDeVotos || 0);
        }, 0);
        
        this.totalVotos.set(somaVotos);
        
        this.carregando.set(false);
      },
      error: () => {
        this.erro.set('Não foi possível carregar as métricas do dashboard.');
        this.carregando.set(false);
      }
    });
  }
}