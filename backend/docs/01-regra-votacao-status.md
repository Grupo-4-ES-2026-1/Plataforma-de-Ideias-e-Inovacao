# Decisão Arquitetural: Independência entre Votação e Status

**Contexto:** 
Precisávamos definir se o volume de votos em uma ideia deveria avançar seu status automaticamente no sistema.

**Decisão:**
O módulo de votação foi desenhado para ser puramente quantitativo. Fica proibida a transição automática de status baseada em contagem de votos. Os endpoints de VotoService e as regras de negócio em PropostaService devem permanecer estritamente independentes.

**Justificativa:**
Isso previne que propostas populares, mas inviáveis, avancem no fluxo de aprovação sem a devida análise qualitativa de um administrador.