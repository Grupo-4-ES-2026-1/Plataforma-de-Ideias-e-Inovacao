import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-home',
  standalone: true,
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {
  private readonly auth = inject(AuthService);

  get usuarioLogado() {
    return this.auth.usuarioLogado();
  }

  get roleAtual() {
    return this.auth.roleAtual();
  }
}
