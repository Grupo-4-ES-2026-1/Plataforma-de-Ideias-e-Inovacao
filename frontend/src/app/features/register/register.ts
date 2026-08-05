import { Component, inject, signal } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  ReactiveFormsModule,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { finalize } from 'rxjs';
import { Auth } from '../../core/services/auth';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  private readonly auth = inject(Auth);
  private readonly formBuilder = inject(FormBuilder);

  mensagem = signal('');
  erro = signal('');
  carregando = signal(false);

  formulario = this.formBuilder.group(
    {
      nome: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(6)]],
      confirmacaoSenha: ['', [Validators.required]],
      role: ['USER', [Validators.required]],
    },
    {
      validators: [this.senhasIguais],
    }
  );

  cadastrar(): void {
    if (this.carregando()) {
      return;
    }

    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    const { nome, email, senha, role } = this.formulario.getRawValue();

    this.mensagem.set('');
    this.erro.set('');
    this.carregando.set(true);

    this.auth
      .cadastrar({
        nome: nome ?? '',
        email: email ?? '',
        senha: senha ?? '',
        role: role ?? 'USER',
      })
      .pipe(
        finalize(() => {
          this.carregando.set(false);
        })
      )
      .subscribe({
        next: () => {
          this.mensagem.set('Usuário cadastrado com sucesso!');

          this.formulario.reset({
            nome: '',
            email: '',
            senha: '',
            confirmacaoSenha: '',
            role: 'USER',
          });
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

  private senhasIguais(control: AbstractControl): ValidationErrors | null {
    const senha = control.get('senha')?.value;
    const confirmacaoSenha = control.get('confirmacaoSenha')?.value;

    return senha === confirmacaoSenha
      ? null
      : { senhasDiferentes: true };
  }
}