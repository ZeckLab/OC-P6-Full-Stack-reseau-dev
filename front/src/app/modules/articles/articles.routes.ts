import { Routes } from '@angular/router';

export const ARTICLES_ROUTES: Routes = [
  {
    path: 'feed',
    loadComponent: () => import('./pages/feed/feed').then(m => m.Feed),
  },
  { path: 'new',
    loadComponent: () => import('./pages/create/article-create') .then(m => m.ArticleCreate),
  },
  {
    path: ':id',
    loadComponent: () => import('./pages/detail/article-detail').then(m => m.ArticleDetail),
  },
  
];