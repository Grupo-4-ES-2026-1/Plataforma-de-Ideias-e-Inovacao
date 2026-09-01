import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterModule } from '@angular/router';
import { PropostaResponse, PropostaService } from '../../core/services/proposta';

@Component({
  selector: 'app-ranking',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './ranking.html',
  styleUrl: './ranking.css'
})
export class RankingComponent implements OnInit {
  private readonly propostaService = inject(PropostaService);

  propostas = signal<PropostaResponse[]>([]);
  carregando = signal(true);
  erro = signal('');

  ngOnInit(): void {
    this.carregarRanking();
  }

  carregarRanking(): void {
    this.carregando.set(true);
    this.erro.set('');

    this.propostaService.listar('', '').subscribe({
      next: (dados) => {
        const ranking = [...dados].sort(
          (a, b) => b.numeroDeVotos - a.numeroDeVotos
        );

        this.propostas.set(ranking);
        this.carregando.set(false);
      },
      error: () => {
        this.erro.set('Não foi possível carregar o ranking.');
        this.carregando.set(false);
      }
    });
  }
}