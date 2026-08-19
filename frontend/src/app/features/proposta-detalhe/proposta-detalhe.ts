import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { PropostaService, PropostaResponse } from '../../core/services/proposta';
import { AuthService } from '../../services/auth'; // Import do AuthService

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

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = idParam ? Number(idParam) : NaN;

    if (!id || Number.isNaN(id)) {
      this.erro.set('Proposta não encontrada.');
      this.carregando.set(false);
      return;
    }

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