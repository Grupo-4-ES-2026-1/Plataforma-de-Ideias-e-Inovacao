import { HttpClient } from '@angular/common/http';
import { Injectable, computed, signal } from '@angular/core';
import { catchError, map, Observable, of, tap } from 'rxjs';

interface Credenciais {
  email: string;
  senha: string;
}

interface AuthResponse {
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly TOKEN_KEY = 'jwt_token';
  private readonly loginEndpoint = '/auth/login';

  usuarioLogado = signal<string | null>(null);
  roleAtual = signal<string | null>(null);
  autenticado = computed(() => this.usuarioLogado() !== null);

  constructor(private http: HttpClient) {
    this.carregarEstadoDeAutenticacao();
  }

  login(email: string, senha: string): Observable<boolean> {
    const credenciais: Credenciais = { email, senha };

    return this.http.post<AuthResponse>(this.loginEndpoint, credenciais).pipe(
      tap((response) => this.salvarToken(response.token)),
      map(() => true),
      catchError(() => of(false)),
    );
  }

  logout(): void {
  if (typeof localStorage !== 'undefined') {
    localStorage.removeItem(this.TOKEN_KEY);
  }
  this.usuarioLogado.set(null);
  this.roleAtual.set(null);
  }

  recuperarToken(): string | null {
    return typeof localStorage !== 'undefined'
      ? localStorage.getItem(this.TOKEN_KEY)
      : null;
  }

  private carregarEstadoDeAutenticacao(): void {
    const token = this.recuperarToken();
    if (token) {
      this.processarToken(token);
    }
  }

  private salvarToken(token: string): void {
    if (typeof localStorage !== 'undefined') {
      localStorage.setItem(this.TOKEN_KEY, token);
    }
    this.processarToken(token);
  }



  private processarToken(token: string): void {
    const payload = this.decodificarToken(token);
    if (!payload) {
      this.usuarioLogado.set(null);
      this.roleAtual.set(null);
      return;
    }

    const nome = payload['sub'] ?? payload['nome'];
    const role = payload['role'] ?? payload['roles'];

    this.usuarioLogado.set(typeof nome === 'string' ? nome : null);
    this.roleAtual.set(typeof role === 'string' ? role : null);
  }

  private decodificarToken(token: string): Record<string, unknown> | null {
    try {
      const partes = token.split('.');
      if (partes.length !== 3) {
        return null;
      }

      const carga = partes[1]
        .replace(/-/g, '+')
        .replace(/_/g, '/');
      const texto = atob(carga);
      return JSON.parse(texto);
    } catch {
      return null;
    }
  }
}

