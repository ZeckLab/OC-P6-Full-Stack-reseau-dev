import { Component, inject, input } from '@angular/core';
import { Article } from '../../models/article.model';
import { ArticleHeader } from '../header/article-header';
import { Router } from '@angular/router';

@Component({
  selector: 'app-article-card',
  imports: [ArticleHeader],
  template: `
    <article class="card compact" (click)="goToDetail(article())">
      <app-article-header [article]="article()" [compact]="true" />

      <p class="content">
        {{ article().content }}
      </p>
    </article>
  `,
  styles: [
    `
      .card {
        background: #f5f5f5;
        border-radius: 0.5rem;
        border: 1px solid #dcdcdc;
        padding: 1rem 1.25rem;
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.06);
        display: flex;
        flex-direction: column;
        gap: 8px;
        cursor: pointer;
      }

      .content {
        font-size: 15px;
        color: #444;
        line-height: 1.4;

        display: -webkit-box;
        -webkit-line-clamp: 5;
        line-clamp: 5;
        -webkit-box-orient: vertical;
        overflow: hidden;
        min-height: 6.85rem;
      }

      app-article-header {
        pointer-events: none;
      }
    `,
  ],
})
export class ArticleCard {
  article = input.required<Article>();
  private readonly router = inject(Router);

  goToDetail(article: Article) {
    this.router.navigate(['/articles', article.id], {
      state: { article },
    });
  }
}
