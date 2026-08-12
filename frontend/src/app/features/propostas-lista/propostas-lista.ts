import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { PropostaService, PropostaResponse } from '../../core/services/proposta';

@Component({
  selector: 'app-propostas-lista',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './propostas-lista.html',
  styleUrl: './propostas-lista.css'
})
export class PropostasListaComponent implements OnInit {
  private readonly propostaService = inject(PropostaService);

  propostas = signal<PropostaResponse[]>([]);
  carregando = signal(true);
  erro = signal('');

  ngOnInit(): void {
    this.propostaService.listar().subscribe({
      next: (dados) => {
        this.propostas.set(dados);
        this.carregando.set(false);
      },
      error: () => {
        this.erro.set('Não foi possível carregar as propostas.');
        this.carregando.set(false);
      }
    });
  }
}