import { Routes } from '@angular/router';
import { Register } from './features/register/register';
import { Login } from './features/login/login';
import { Home } from './features/home/home';
import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';
import { PropostasListaComponent } from './features/propostas-lista/propostas-lista';
import { PropostaDetalheComponent } from './features/proposta-detalhe/proposta-detalhe';
import { PropostaCadastroComponent } from './features/cadastro-proposta/cadastro-proposta';
import { MinhasPropostasComponent } from './features/minhas-propostas/minhas-propostas';
import { DashboardComponent } from './features/dashboard/dashboard';
import { RankingComponent } from './features/ranking/ranking';

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
    path: 'minhas-propostas',
    component: MinhasPropostasComponent,
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
  },
  {
    // US14 - só quem avalia/gerencia propostas (ADMIN) acessa o dashboard.
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [adminGuard]
  },
  {
    // US13 - só quem avalia propostas (ADMIN) acessa o ranking.
    path: 'ranking',
    component: RankingComponent,
    canActivate: [adminGuard]
  }
];