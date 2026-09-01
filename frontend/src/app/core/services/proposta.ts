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
  numeroDeVotos: number;
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

/** Item do histórico de status de uma proposta. */
export interface HistoricoStatusItem {
  statusAnterior: string;
  statusNovo: string;
  data: string;
}

@Injectable({
  providedIn: 'root',
})
export class PropostaService {
  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    'https://plataforma-de-ideias-e-inovacao.onrender.com/propostas';

  cadastrar(dados: PropostaRequest): Observable<PropostaResponse> {
    return this.http.post<PropostaResponse>(this.apiUrl, dados);
  }

  listar(
    status: string = '',
    sort: string = 'dataCriacao,desc'
  ): Observable<PropostaResponse[]> {
    let params = new HttpParams();

    if (status) {
      params = params.set('status', status);
    }

    if (sort) {
      params = params.set('sort', sort);
    }

    return this.http.get<PropostaResponse[]>(this.apiUrl, { params });
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
      params = params.set(
        'dataInicial',
        `${dataInicial}T00:00:00`
      );
    }

    if (dataFinal) {
      params = params.set(
        'dataFinal',
        `${dataFinal}T23:59:59`
      );
    }

    return this.http.get<PropostaPage>(
      `${this.apiUrl}/minhas`,
      { params }
    );
  }

  obterRanking(
    categoria?: string,
    dataInicial?: string,
    dataFinal?: string
  ): Observable<PropostaResponse[]> {
    let params = new HttpParams();

    if (categoria) {
      params = params.set('categoria', categoria);
    }

    if (dataInicial) {
      params = params.set(
        'dataInicial',
        `${dataInicial}T00:00:00`
      );
    }

    if (dataFinal) {
      params = params.set(
        'dataFinal',
        `${dataFinal}T23:59:59`
      );
    }

    return this.http.get<PropostaResponse[]>(
      `${this.apiUrl}/ranking`,
      { params }
    );
  }

  atualizarStatus(
    id: number,
    novoStatus: string
  ): Observable<PropostaResponse> {
    return this.http.patch<PropostaResponse>(
      `${this.apiUrl}/${id}/status`,
      { status: novoStatus }
    );
  }

  buscarHistoricoStatus(
    id: number
  ): Observable<HistoricoStatusItem[]> {
    return this.http.get<HistoricoStatusItem[]>(
      `${this.apiUrl}/${id}/historico-status`
    );
  }

  votar(id: number): Observable<{ id: number; propostaId: number; usuarioId: number }> {
    return this.http.post<{ id: number; propostaId: number; usuarioId: number }>(
      `${this.apiUrl}/${id}/voto`,
      {}
    );
  }
}