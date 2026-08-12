import { Routes } from '@angular/router';
import { Register } from './features/register/register';
import { Login } from './features/login/login';
import { Home } from './features/home/home';
import { authGuard } from './guards/auth.guard';
import { PropostasListaComponent } from './features/propostas-lista/propostas-lista';
import { PropostaDetalheComponent } from './features/proposta-detalhe/proposta-detalhe';
import { PropostaCadastroComponent } from './features/cadastro-proposta/cadastro-proposta';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: Login
  },
  {
    path: 'register',
    component: Register
  },
  {
    path: 'home',
    component: Home,
    canActivate: [authGuard]
  },
  {
    path: 'propostas',
    component: PropostasListaComponent,
    canActivate: [authGuard]
  },
  {
    path: 'propostas/nova',
    component: PropostaCadastroComponent,
    canActivate: [authGuard]
  },
  {
    path: 'propostas/:id',
    component: PropostaDetalheComponent,
    canActivate: [authGuard]
  }
];