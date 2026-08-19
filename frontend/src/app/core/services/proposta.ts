import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

export interface PropostaRequest {
  titulo: string;
  descricao: string;
  categoria: string;
}

export interface PropostaResponse extends PropostaRequest {
  id: number;
  status: string;
  autorId?: number;
  autorNome?: string;
  dataCriacao?: string;
}

export interface PropostaPage {
  content: PropostaResponse[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

@Injectable({
  providedIn: 'root',
})
export class PropostaService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/propostas';

  cadastrar(dados: PropostaRequest): Observable<PropostaResponse> {
    return this.http.post<PropostaResponse>(this.apiUrl, dados);
  }

  listar(): Observable<PropostaResponse[]> {
    return this.http.get<PropostaResponse[]>(this.apiUrl);
  }

  buscarPorId(id: number): Observable<PropostaResponse> {
    return this.http.get<PropostaResponse>(`${this.apiUrl}/${id}`);
  }

  buscarMinhasPropostas(
    status?: string,
    dataInicial?: string,
    dataFinal?: string,
    page: number = 0,
    size: number = 5
  ): Observable<PropostaPage> {

    let params = new HttpParams()
      .set('page', page)
      .set('size', size);

    if (status) {
      params = params.set('status', status);
    }

    if (dataInicial) {
      params = params.set('dataInicial', `${dataInicial}T00:00:00`);
    }

    if (dataFinal) {
      params = params.set('dataFinal', `${dataFinal}T23:59:59`);
    }

    return this.http.get<PropostaPage>(
      `${this.apiUrl}/minhas`,
      { params }
    );
  }
}