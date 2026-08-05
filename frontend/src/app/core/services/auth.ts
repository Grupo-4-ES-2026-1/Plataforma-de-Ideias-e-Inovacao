import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

export interface RegisterRequest {
  nome: string;
  email: string;
  senha: string;
  role: string;
}

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/users';

  cadastrar(dados: RegisterRequest): Observable<unknown> {
    return this.http.post(this.apiUrl, dados);
  }
}