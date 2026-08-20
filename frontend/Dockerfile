# Etapa 1: build da aplicação Angular
FROM node:22-alpine AS build
WORKDIR /app

# Copia primeiro só os arquivos de dependência para aproveitar cache
COPY package.json package-lock.json ./
RUN npm ci

# Copia o restante do código-fonte e builda em modo produção
COPY . .
RUN npm run build -- --configuration production

# Etapa 2: imagem final, só com Nginx servindo os arquivos estáticos
FROM nginx:alpine

# Remove a config default do Nginx e usa a nossa (com suporte a rotas do Angular)
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copia os arquivos buildados (Angular 22 gera em dist/frontend/browser)
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
