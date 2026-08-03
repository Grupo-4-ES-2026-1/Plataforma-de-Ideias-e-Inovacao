import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
import { Auth } from '../../core/services/auth';

@Component({
  selector: 'app-register',
  imports: [FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  private readonly auth = inject(Auth);

  nome = '';
  email = '';
  senha = '';
  role = 'USER';

  mensagem = signal('');
  erro = signal('');
  carregando = signal(false);

  cadastrar(): void {
    if (this.carregando()) {
      return;
    }

    this.mensagem.set('');
    this.erro.set('');
    this.carregando.set(true);

    this.auth
      .cadastrar({
        nome: this.nome,
        email: this.email,
        senha: this.senha,
        role: this.role,
      })
      .pipe(
        finalize(() => {
          this.carregando.set(false);
        })
      )
      .subscribe({
        next: () => {
          this.mensagem.set('Usuário cadastrado com sucesso!');

          this.nome = '';
          this.email = '';
          this.senha = '';
          this.role = 'USER';
        },

        error: (erro) => {
          console.error('Erro ao cadastrar usuário:', erro);

          if (erro.status === 0) {
            this.erro.set('Não foi possível conectar ao backend.');
          } else {
            this.erro.set('Não foi possível realizar o cadastro.');
          }
        },
      });
  }
}