import { Component, computed, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-nav',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
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

  iniciais = computed(() => {
    const nome = this.auth.usuarioLogado();
    if (!nome) {
      return '';
    }
    const partes = nome.trim().split(/\s+/);
    const primeira = partes[0]?.[0] ?? '';
    const ultima = partes.length > 1 ? partes[partes.length - 1][0] : '';
    return (primeira + ultima).toUpperCase();
  });

  logout(): void {
    this.auth.logout().subscribe(() => {
      this.router.navigate(['/login']);
    });
  }
}