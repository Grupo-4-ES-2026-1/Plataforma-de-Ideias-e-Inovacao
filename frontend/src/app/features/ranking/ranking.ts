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
        this.aplicarFiltroCategoria();
        this.carregando.set(false);
      },
      error: () => {
        this.erro.set('Não foi possível carregar o ranking.');
        this.carregando.set(false);
      }
    });
  }

  aplicarFiltroCategoria(): void {
    let filtradas = [...this.todasPropostas];

    if (this.filtroCategoria) {
      filtradas = filtradas.filter(
        (proposta) => proposta.categoria === this.filtroCategoria
      );
    }

    filtradas.sort(
      (a, b) => b.numeroDeVotos - a.numeroDeVotos
    );

    this.propostas.set(filtradas);
  }
}