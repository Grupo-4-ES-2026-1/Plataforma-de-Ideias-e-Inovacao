import { Routes } from '@angular/router';
import { Register } from './features/register/register';
import { Login } from './features/login/login';
import { Home } from './features/home/home';
import { authGuard } from './guards/auth.guard';

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
    canActivate: [authGuard]
  },
  {
    path: 'propostas/:id',
    canActivate: [authGuard]
  }
];
