import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

export interface RegisterRequest {
  nome: string;
  email: string;
  senha: string;
}

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'https://plataforma-de-ideias-e-inovacao.onrender.com/auth/register';

  cadastrar(dados: RegisterRequest): Observable<unknown> {
    const payload = {
      nome: dados.nome,
      email: dados.email,
      password: dados.senha,
    };
    return this.http.post(this.apiUrl, payload);
  }
}
