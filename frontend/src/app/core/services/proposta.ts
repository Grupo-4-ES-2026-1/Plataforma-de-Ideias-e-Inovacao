import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

export interface PropostaRequest {
  titulo: string;
  descricao: string;
  categoria: string;
}

export interface PropostaResponse extends PropostaRequest {
  id: number;
}

@Injectable({
  providedIn: 'root',
})
export class PropostaService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/propostas';

  cadastrar(dados: PropostaRequest): Observable<unknown> {
    return this.http.post(this.apiUrl, dados);
  }

  listar(): Observable<PropostaResponse[]> {
    return this.http.get<PropostaResponse[]>(this.apiUrl);
  }

  buscarPorId(id: number): Observable<PropostaResponse> {
    return this.http.get<PropostaResponse>(`${this.apiUrl}/${id}`);
  }
}