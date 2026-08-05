import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  email = '';
  senha = '';
  erro = '';
  carregando = false;

  login() {
    this.erro = '';
    this.carregando = true;

    this.authService.login(this.email, this.senha).subscribe((sucesso) => {
      this.carregando = false;

      if (sucesso) {
        this.router.navigate(['/home']);
      } else {
        this.erro = 'Email ou senha inválidos.';
      }
    });
  }
}
