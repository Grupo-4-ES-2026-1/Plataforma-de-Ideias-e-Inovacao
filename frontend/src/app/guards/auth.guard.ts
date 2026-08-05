import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth';

export const authGuard: CanActivateFn = (route, state) => {
  //injeta o serviço de autenticação e o roteador do Angular
  const authService = inject(AuthService);
  const router = inject(Router);

  //verifica se o usuario está logado
  if (authService.autenticado()) {
    return true; 
  }

  //se o usuário não estiver autenticado, ele volta automaticamente para a tela de login
  router.navigate(['/login']);
  return false; //se não, bloqueia o acesso a rota
};