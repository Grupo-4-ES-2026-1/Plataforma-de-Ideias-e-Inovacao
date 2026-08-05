import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-nav',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './nav.html',
  styleUrl: './nav.css',
})
export class Nav {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  get autenticado() {
    return this.auth.autenticado();
  }

  get usuarioLogado() {
    return this.auth.usuarioLogado();
  }

  logout(): void {
    this.auth.logout().subscribe(() => {
      this.router.navigate(['/login']);
    });
  }
}
