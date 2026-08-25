/**
 * Modelo provisório de status de proposta (US10/US11/US12).
 */

export enum StatusProposta {
  SUBMETIDA = 'SUBMETIDA',
  EM_ANALISE = 'EM_ANALISE',
  APROVADA = 'APROVADA',
  IMPLANTADA = 'IMPLANTADA',
  REJEITADA = 'REJEITADA',
}

export const STATUS_LABEL: Record<string, string> = {
  [StatusProposta.SUBMETIDA]: 'Submetida',
  [StatusProposta.EM_ANALISE]: 'Em análise',
  [StatusProposta.APROVADA]: 'Aprovada',
  [StatusProposta.IMPLANTADA]: 'Implantada',
  [StatusProposta.REJEITADA]: 'Rejeitada',
};


const TRANSICOES_PERMITIDAS: Record<string, StatusProposta[]> = {
  [StatusProposta.SUBMETIDA]: [StatusProposta.EM_ANALISE, StatusProposta.REJEITADA],
  [StatusProposta.EM_ANALISE]: [StatusProposta.APROVADA, StatusProposta.REJEITADA],
  [StatusProposta.APROVADA]: [StatusProposta.IMPLANTADA],
  [StatusProposta.IMPLANTADA]: [],
  [StatusProposta.REJEITADA]: [],
};

/** Retorna o rótulo amigável para um status. */
export function getLabelStatus(status: string): string {
  return STATUS_LABEL[status] ?? status;
}

/**
 * Retorna os próximos status para os quais a proposta pode ser movida
 */
export function getProximosStatusDisponiveis(statusAtual: string): StatusProposta[] {
  return TRANSICOES_PERMITIDAS[statusAtual] ?? [];
}

/** Indica se a proposta já está em um status final (sem novas transições). */
export function isStatusFinal(status: string): boolean {
  return getProximosStatusDisponiveis(status).length === 0;
}