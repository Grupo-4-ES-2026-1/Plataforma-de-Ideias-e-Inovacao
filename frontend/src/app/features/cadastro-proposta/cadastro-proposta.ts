import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { PropostaService } from '../../core/services/proposta';

@Component({
  selector: 'app-proposta-cadastro',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './cadastro-proposta.html',
  styleUrl: './cadastro-proposta.css'
})
export class PropostaCadastroComponent {
  private readonly fb = inject(FormBuilder);
  private readonly propostaService = inject(PropostaService);

  formulario = this.fb.group({
    titulo: ['', Validators.required],
    descricao: ['', Validators.required],
    categoria: ['', Validators.required]
  });

  carregando = signal(false);
  mensagem = signal('');
  erro = signal('');

  cadastrar() {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    this.carregando.set(true);
    this.mensagem.set('');
    this.erro.set('');

    this.propostaService.cadastrar(this.formulario.value as any).subscribe({
      next: () => {
        this.mensagem.set('Proposta cadastrada com sucesso!');
        this.formulario.reset();
        this.carregando.set(false);
      },
      error: () => {
        this.erro.set('Erro ao cadastrar a proposta. Verifique os dados e tente novamente.');
        this.carregando.set(false);
      }
    });
  }
}