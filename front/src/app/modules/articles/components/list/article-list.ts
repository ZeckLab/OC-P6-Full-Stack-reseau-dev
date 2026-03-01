import { Component, input, OnChanges, SimpleChanges } from '@angular/core';
import { Article } from '../../models/article.model';
import { ArticleCard } from '../card/article-card';

@Component({
  selector: 'app-article-list',
  imports: [ArticleCard],
  template: `
    <div class="articles">
      @if (!articles() || articles()!.length === 0) {
        <p class="empty">Aucun article pour le moment.</p>
      } @else {
        <div class="articles-list">
          @for (article of articles(); track article.id) {
            <app-article-card [article]="article" />
          }
        </div>
      }
    </div>
  `,
  styles: [
    `
      .articles-list {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 2rem;
        width: 100%;

        @media (max-width: 600px) {
          grid-template-columns: 1fr;
          gap: 1.25rem;
        }
      }
    `,
  ],
})
export class ArticleList {
  articles = input<Article[] | null>(null);
}
