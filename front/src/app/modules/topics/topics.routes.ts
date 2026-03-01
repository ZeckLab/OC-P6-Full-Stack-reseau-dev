import { Routes } from '@angular/router';

export const TOPICS_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/subscription/topic-subscription').then(m => m.TopicSubscription),
  },
];
