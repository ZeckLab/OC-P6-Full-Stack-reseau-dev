import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { guestOnlyGuard } from './core/guards/guest.guard';

export const routes: Routes = [
  {
    path: '',
    canActivate: [guestOnlyGuard],
    loadChildren: () => import('./modules/home/home.routes').then((m) => m.HOME_ROUTES),
  },
  {
    path: 'auth',
    canActivate: [guestOnlyGuard],
    loadChildren: () => import('./modules/auth/auth.routes').then((m) => m.AUTH_ROUTES),
  },
  {
    path: 'articles',
    canActivate: [authGuard],
    loadChildren: () => import('./modules/articles/articles.routes').then((m) => m.ARTICLES_ROUTES),
  },
  {
    path: 'topics',
    canActivate: [authGuard],
    loadChildren: () => import('./modules/topics/topics.routes').then((m) => m.TOPICS_ROUTES),
  },
  {
    path: '**',
    loadComponent: () =>
      import('./core/not-found/not-found').then((m) => m.NotFound),
  },
];
