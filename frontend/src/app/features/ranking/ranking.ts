import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PropostaResponse, PropostaService } from '../../core/services/proposta';

@Component({
  selector: 'app-ranking',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './ranking.html',
  styleUrl: './ranking.css'
})
export class RankingComponent implements OnInit {
  private readonly propostaService = inject(PropostaService);

  propostas = signal<PropostaResponse[]>([]);
  categoriasDisponiveis = signal<string[]>([]);
  carregando = signal(true);
  erro = signal('');

  todasPropostas: PropostaResponse[] = [];

  filtroCategoria = '';
  dataInicial = '';
  dataFinal = '';

  ngOnInit(): void {
    this.carregarRanking();
  }

  carregarRanking(): void {
    this.carregando.set(true);
    this.erro.set('');

    this.propostaService.listar('', '').subscribe({
      next: (dados) => {
        this.todasPropostas = [...dados];

        const categorias = [
          ...new Set(
            dados
              .map((proposta) => proposta.categoria)
              .filter(Boolean)
          )
        ];

        this.categoriasDisponiveis.set(categorias);
        this.aplicarFiltros();
        this.carregando.set(false);
      },
      error: () => {
        this.erro.set('Não foi possível carregar o ranking.');
        this.carregando.set(false);
      }
    });
  }

  aplicarFiltros(): void {
    let filtradas = [...this.todasPropostas];

    if (this.filtroCategoria) {
      filtradas = filtradas.filter(
        (proposta) => proposta.categoria === this.filtroCategoria
      );
    }

    if (this.dataInicial) {
      const inicio = new Date(`${this.dataInicial}T00:00:00`);

      filtradas = filtradas.filter((proposta) => {
        if (!proposta.dataCriacao) {
          return false;
        }

        return new Date(proposta.dataCriacao) >= inicio;
      });
    }

    if (this.dataFinal) {
      const fim = new Date(`${this.dataFinal}T23:59:59`);

      filtradas = filtradas.filter((proposta) => {
        if (!proposta.dataCriacao) {
          return false;
        }

        return new Date(proposta.dataCriacao) <= fim;
      });
    }

    filtradas.sort(
      (a, b) => b.numeroDeVotos - a.numeroDeVotos
    );

    this.propostas.set(filtradas);
  }
}