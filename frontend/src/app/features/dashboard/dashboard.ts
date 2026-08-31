import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { PropostaService, PropostaResponse } from '../../core/services/proposta';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {
  private readonly propostaService = inject(PropostaService);

  carregando = signal(true);
  erro = signal('');

  // Sinais das métricas
  totalPropostas = signal(0);
  distribuicao = signal({ submetida: 0, emAnalise: 0, aprovada: 0, rejeitada: 0 });
  taxaAprovacao = signal(0);
  totalVotos = signal(0);

  // Variáveis para os Filtros
  todasPropostas: PropostaResponse[] = []; // Guarda o "cache" de dados do backend
  categoriasDisponiveis = signal<string[]>([]);
  filtroCategoria = '';
  filtroPeriodo = 'todos';

  ngOnInit(): void {
    this.propostaService.listar().subscribe({
      next: (propostas) => {
        this.todasPropostas = propostas;
        
        // Extrai magicamente as categorias únicas para preencher o select
        const categorias = [...new Set(propostas.map(p => p.categoria).filter(Boolean))];
        this.categoriasDisponiveis.set(categorias);

        // Dispara o cálculo pela primeira vez
        this.aplicarFiltros();
        
        this.carregando.set(false);
      },
      error: () => {
        this.erro.set('Não foi possível carregar as métricas do dashboard.');
        this.carregando.set(false);
      }
    });
  }

  //método disparado quando o usuário altera o select
  aplicarFiltros(): void {
    let filtradas = [...this.todasPropostas];

    // 1. aplica o filtro de Categoria
    if (this.filtroCategoria) {
      filtradas = filtradas.filter(p => p.categoria === this.filtroCategoria);
    }

    // 2. aplica o filtro de Período
    if (this.filtroPeriodo !== 'todos') {
      const hoje = new Date();
      let dataLimite = new Date();

      if (this.filtroPeriodo === '7dias') {
        dataLimite.setDate(hoje.getDate() - 7);
      } else if (this.filtroPeriodo === '30dias') {
        dataLimite.setDate(hoje.getDate() - 30);
      } else if (this.filtroPeriodo === 'ano') {
        dataLimite.setFullYear(hoje.getFullYear(), 0, 1); // 1º de janeiro deste ano
      }

      filtradas = filtradas.filter(p => {
        if (!p.dataCriacao) return false;
        return new Date(p.dataCriacao) >= dataLimite;
      });
    }

    // 3. recalcula todos os cards com os dados filtrados
    this.calcularMetricas(filtradas);
  }

  // separa o cálculo matemático para reaproveitar a lógica
  private calcularMetricas(propostas: PropostaResponse[]): void {
    this.totalPropostas.set(propostas.length);

    const contagem = { submetida: 0, emAnalise: 0, aprovada: 0, rejeitada: 0 };
    let somaVotos = 0;
    
    propostas.forEach(p => {
      if (p.status === 'SUBMETIDA') contagem.submetida++;
      if (p.status === 'EM_ANALISE') contagem.emAnalise++;
      if (p.status === 'APROVADA') contagem.aprovada++;
      if (p.status === 'REJEITADA') contagem.rejeitada++;
      
      somaVotos += (p.numeroDeVotos || 0);
    });
    
    this.distribuicao.set(contagem);
    this.totalVotos.set(somaVotos);

    const total = propostas.length;
    const taxa = total > 0 ? (contagem.aprovada / total) * 100 : 0;
    this.taxaAprovacao.set(Math.round(taxa)); 
  }
}