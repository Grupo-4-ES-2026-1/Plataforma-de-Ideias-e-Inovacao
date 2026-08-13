import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth';
import { PropostaService } from '../../core/services/proposta';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  private readonly auth = inject(AuthService);
  private readonly propostaService = inject(PropostaService);

  totalPropostas = signal<number | null>(null);

  get usuarioLogado() {
    return this.auth.usuarioLogado();
  }

  ngOnInit(): void {
    this.propostaService.listar().subscribe({
      next: (propostas) => this.totalPropostas.set(propostas.length),
      error: () => this.totalPropostas.set(null),
    });
  }
}