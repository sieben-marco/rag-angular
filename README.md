# Angular 22 Assistant - Trabalho Final Tópicos de IA

Chatbot RAG (Retrieval Augmented Generation) para responder dúvidas sobre Angular, baseado no Step 6 do tutorial Quarkus LangChain4j.

## O que foi modificado em relação ao tutorial original

### Arquivos modificados

| Arquivo | Mudança |
| ------- | ------- |
| `pom.xml` | Adicionadas dependências: `langchain4j-document-parser-apache-tika`, `quarkus-langchain4j-ai-gemini`, `quarkus-hibernate-orm-panache`, `quarkus-jdbc-postgresql` |
| `application.properties` | Configuração do Gemini, datasource PostgreSQL, Hibernate ORM e Dev Services com porta fixa |
| `RagRetriever.java` | Injeta `RagLoggingContentInjector` para logging dos chunks |
| `CustomerSupportAgent.java` | System Message adaptada para Angular (contexto em PT-BR) |
| `CustomerSupportAgentWebSocket.java` | Welcome message adaptada para PT-BR |

### Arquivos novos

| Arquivo | Descrição |
| ------- | --------- |
| `entity/RagChunkLog.java` | Entidade Panache para persistir os chunks recuperados |
| `RagChunkLogService.java` | Service com `@Transactional` para salvar logs no banco |
| `RagLoggingContentInjector.java` | ContentInjector que loga chunks e injeta contexto no prompt |

## Como rodar

```bash
cd section-1/step-06
./mvnw quarkus:dev

## ou

cd section-1/step-06
mvn quarkus:dev

## ou 

cd section-1/step-06
quarkus dev
```

O app estará disponível em <http://localhost:8080>.

### Pré-requisitos

- Java 21+
- Docker ou Podman (para Dev Services do PostgreSQL)
- Chave API configurada no arquivo `.env`:

```txt
GEMINI_API_KEY=sua-chave-aqui
```

Para usar outras LLMs, além de instalar a dependência da LLM no `pom.xml`, você também precisa configurar a chave API no arquivo `.env` e adicionar as configurações no `application.properties`.

## Configuração do banco de dados

O projeto usa **PostgreSQL** via Quarkus Dev Services (automaticamente via Docker).

### Credenciais de conexão

| Campo | Valor |
| ----- | ----- |
| Host | `127.0.0.1` |
| Porta | `5432` |
| Usuário | `quarkus` |
| Senha | `quarkus` |
| Banco | `quarkus` |

### Visualizando os logs via VS Code

Com o app rodando (`./mvnw quarkus:dev`):

1. Instale a extensão **PostgreSQL** da Microsoft (`ms-ossdata.vscode-pgsql`)
2. Clique em "Add Connection" no painel da extensão
3. Use as credendiais de conexão (desabilite o ssl)
4. Conecte e visualize a tabela `rag_chunk_log` no banco `quarkus`

### Visualizando os logs via linha de comando

Com o app rodando (`./mvnw quarkus:dev`), em outro terminal:

```bash
psql -h localhost -p 5432 -U quarkus -d quarkus
```

```sql
-- Ver todos os logs
SELECT * FROM rag_chunk_log ORDER BY retrieval_timestamp DESC;

-- Ver logs de uma pergunta específica
SELECT user_question, chunk_text, similarity_score, source_document, retrieval_timestamp
FROM rag_chunk_log
WHERE user_question ILIKE '%signals%';

-- Contar logs por documento de origem
SELECT source_document, COUNT(*) as total
FROM rag_chunk_log
GROUP BY source_document;
```

## Contexto do RAG

O chatbot responde sobre **Angular** usando uma base de conhecimento abrangente:

### Documentação e Guias

| Arquivo | Formato | Conteúdo |
| ------- | ------- | -------- |
| `principais-conceitos-doc-llms-full.md` | MD | Documentação completa Angular (componentes, templates, signals, inputs, outputs, DI, forms, routing, SSR, etc.) |
| `principais-links-doc-llms.md` | MD | Índice de links oficiais da documentação Angular |

### Boas Práticas e Padrões

| Arquivo | Formato | Conteúdo |
| ------- | ------- | -------- |
| `angular-best-practices.md` | MD | Boas práticas Angular (TypeScript, componentes, signals, templates, services) |
| `guidelines.md` | MD | Diretrizes de código e persona do desenvolvedor Angular moderno |
| `copilot-instructions.md` | MD | Instruções para AI assistants (GitHub Copilot) |
| `airules.md` | MD | Regras de IA para desenvolvimento Angular |

### Ferramentas e Ecossistema

| Arquivo | Formato | Conteúdo |
| ------- | ------- | -------- |
| `angular-mcp.txt` | TXT | Angular CLI MCP Server (ferramentas para AI agents: ai_tutor, devserver, search_documentation, etc.) |
| `angular-skills.txt` | TXT | Angular Agent Skills (angular-developer, angular-new-app) |

### Novidades e Migração

| Arquivo | Formato | Conteúdo |
| ------- | ------- | -------- |
| `Novidades do Angular 22.docx` | DOCX | Novidades do Angular 22 |
| `Arquitetura do Angular.docx` | DOCX | Arquitetura do Angular |
| `Evolução Histórica e o Legado do AngularJS.docx` | DOCX | Evolução histórica do Angular |
| `Angular CLI no Angular 22.pdf` | PDF | Angular CLI no Angular 22 |
| `Guia de Migração para o Angular 22.pdf` | PDF | Guia de migração para Angular 22 |
| `Diretrizes Estratégicas para Líderes de Engenharia Angular.pdf` | PDF | Diretrizes estratégicas para líderes de engenharia |

## Perguntas sugeridas para teste

### Fundamentos Angular

- "O que é Angular?"
- "Como criar um novo projeto Angular?"
- "O que são componentes no Angular?"
- "Como funciona Dependency Injection no Angular?"

### Novidades do Angular 22

- "Quais são as novidades do Angular 22?"
- "O que são Signal Forms?"
- "O que mudou no change detection do Angular 22?"
- "O que é o decorador @Service?"
- "OnPush é o padrão agora?"

### Signals e Reatividade

- "O que são Signals no Angular?"
- "Como usar computed() e effect()?"
- "O que é linkedSignal?"
- "O que são resource e httpResource?"

### Componentes e Templates

- "O que são Standalone Components?"
- "Como usar @if, @for e @switch nos templates?"
- "O que é @defer no Angular?"
- "Como fazer two-way binding com model inputs?"

### Boas Práticas

- "Quais são as boas práticas para componentes Angular?"
- "Devo usar ngClass ou class bindings?"
- "Qual a diferença entre Reactive Forms e Template-driven Forms?"
- "Como implementar lazy loading de rotas?"

### Angular CLI e MCP

- "Quais comandos do ng generate existem?"
- "Como configurar o Angular CLI MCP Server?"
- "O que são Angular Agent Skills?"

### Migração

- "Como migrar de NgModules para Standalone Components?"
- "Como migrar para OnPush change detection?"
- "O que preciso mudar ao atualizar para Angular 22?"

### Perguntas fora do contexto (para testar o filtro)

- "Qual o preço do dólar?" (deve redirecionar para <https://angular.dev>)
