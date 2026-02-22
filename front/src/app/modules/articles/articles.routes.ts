import { Routes } from '@angular/router';

export const ARTICLES_ROUTES: Routes = [
  {
    path: 'feed',
    loadComponent: () => import('./pages/feed/feed').then(m => m.Feed),
  },
];