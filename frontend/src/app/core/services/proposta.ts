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

/**  - item do histórico de status de uma proposta. */
export interface HistoricoStatusItem {
  status: string;
  dataAlteracao: string;
}

@Injectable({
  providedIn: 'root',
})
export class PropostaService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'https://plataforma-de-ideias-e-inovacao.onrender.com/propostas';

  cadastrar(dados: PropostaRequest): Observable<PropostaResponse> {
    return this.http.post<PropostaResponse>(this.apiUrl, dados);
  }

  listar(status: string = '', sort: string = 'recentes'): Observable<PropostaResponse[]> {
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

  /**
   * Endpoint ainda não implementado no backend. Assim que
   * estiver disponível, esta chamada passa a funcionar sem mudanças aqui.
   */
  atualizarStatus(id: number, novoStatus: string): Observable<PropostaResponse> {
    return this.http.patch<PropostaResponse>(`${this.apiUrl}/${id}/status`, { novoStatus });
  }

  /**
   * Endpoint ainda não implementado no backend). Assim que
   * estiver disponível, esta chamada passa a funcionar sem mudanças aqui.
   */
  buscarHistoricoStatus(id: number): Observable<HistoricoStatusItem[]> {
    return this.http.get<HistoricoStatusItem[]>(`${this.apiUrl}/${id}/historico-status`);
  }
}