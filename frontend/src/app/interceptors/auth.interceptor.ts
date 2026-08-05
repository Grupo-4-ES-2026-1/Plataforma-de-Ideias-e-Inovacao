import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.recuperarToken();

  //caso a requisição já tem um cabeçalho Authorization ou é a rota de login, deixa passar
  if (req.headers.has('Authorization') || req.url.endsWith('/auth/login')) {
    return next(req);
  }

  //se tiver token, clona a requisição e adiciona o cabeçalho
  if (token) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(authReq);
  }

  //se não tem token, vai para o prox
  return next(req);
};
