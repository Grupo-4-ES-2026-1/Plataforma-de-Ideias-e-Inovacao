import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PropostaResponse, PropostaService } from '../../core/services/proposta';

@Component({
  selector: 'app-minhas-propostas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './minhas-propostas.html',
  styleUrl: './minhas-propostas.css'
})
export class MinhasPropostasComponent {

  private readonly propostaService = inject(PropostaService);

  propostas = signal<PropostaResponse[]>([]);
  carregando = signal(true);
  erro = signal('');

  status = '';
  dataInicial = '';
  dataFinal = '';

  pagina = signal(0);
  tamanhoPagina = 5;
  totalPaginas = signal(0);

  ngOnInit(): void {
    this.buscar();
  }

  buscar(): void {
    this.carregando.set(true);
    this.erro.set('');

    this.propostaService.buscarMinhasPropostas(
      this.status || undefined,
      this.dataInicial || undefined,
      this.dataFinal || undefined,
      this.pagina(),
      this.tamanhoPagina
    ).subscribe({
      next: (resultado) => {
        this.propostas.set(resultado.content);
        this.totalPaginas.set(resultado.totalPages);
        this.carregando.set(false);
      },
      error: (erro) => {
        console.error(erro);
        this.erro.set('Não foi possível carregar suas propostas.');
        this.carregando.set(false);
      }
    });
  }

  filtrar(): void {
    this.pagina.set(0);
    this.buscar();
  }

  paginaAnterior(): void {
    if (this.pagina() > 0) {
      this.pagina.update(p => p - 1);
      this.buscar();
    }
  }

  proximaPagina(): void {
    if (this.pagina() < this.totalPaginas() - 1) {
      this.pagina.update(p => p + 1);
      this.buscar();
    }
  }
}