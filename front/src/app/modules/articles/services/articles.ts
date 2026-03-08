import { computed, inject, Injectable, signal } from '@angular/core';
import { ArticlesApi } from './articles.api';
import { Article } from '../models/article.model';
import { ArticleResponse } from '../models/article.response';
import { CreateArticleRequest } from '../models/article.request';
import { map, tap } from 'rxjs';
import { MESSAGES } from '../../../core/messages/constants';
import { Comments } from '../../comments/services/comments';

/**
 * Articles facade.
 * Centralizes article state, sorting, selection and error handling.
 * Exposes reactive signals consumed by the UI.
 */
@Injectable({ providedIn: 'root' })
export class Articles {
  private readonly api = inject(ArticlesApi);
  private readonly commentsFacade = inject(Comments);

  /** List of articles loaded from the backend */
  private readonly _articles = signal<Article[]>([]);
  articles$ = computed(() => {
    const sort = this._sort();
    const list = [...this._articles()];
    return sort === 'desc'
      ? list.sort((a, b) => b.createdAt.localeCompare(a.createdAt))
      : list.sort((a, b) => a.createdAt.localeCompare(b.createdAt));
  });

  /** Current sort direction (asc/desc) */
  private readonly _sort = signal<'asc' | 'desc'>('desc');
  sortDirection = this._sort.asReadonly();

  /** Currently selected article (detail page) */
  private readonly _selected = signal<Article | null>(null);
  selectedArticle = this._selected.asReadonly();

  /** Error message for UI display */
  private readonly _error = signal<string | null>(null);
  error = this._error.asReadonly();

  /** Selects an article for the detail page */
  setSelected(article: Article) {
    this._selected.set(article);
  }

  /** Toggles sorting direction */
  toggleSort() {
    this._sort.update((s) => (s === 'desc' ? 'asc' : 'desc'));
  }

  /** Loads the article feed and updates state reactively */
  getFeed() {
    return this.api.getFeed().pipe(
      tap({
        next: (response) => {
          this._error.set(null);
          this._articles.set(response.map((item) => this.mapToArticle(item)));
        },
        error: () => {
          this._error.set(MESSAGES.ARTICLES_LOAD_ERROR);
          this._articles.set([]);
        },
      }),
    );
  }

  /** Loads a single article by ID and updates the selected article. */
  getById(id: string) {
    return this.api.getById(id).pipe(
      tap({
        next: (item) => {
          this._error.set(null);
          this._selected.set(this.mapToArticle(item));

          if(item.comments) {
            this.commentsFacade.setComments(item.comments);
          }
        },
        error: () => {
          this._selected.set(null);
          this._error.set('not-found');
        },
      }),
    );
  }

  /** Creates a new article and inserts it at the top of the feed. */
  createArticle(data: CreateArticleRequest) {
    return this.api.create(data).pipe(
      map((response) => {
        const article = this.mapToArticle(response);
        this._articles.update((list) => [article, ...list]);
        return article;
      }),
    );
  }

  /** Maps a backend ArticleResponse to the internal Article model. */
  private mapToArticle(response: ArticleResponse): Article {
    return {
      id: response.id,
      title: response.title,
      content: response.content,
      createdAt: response.createdAt,
      authorUsername: response.authorUsername,
      topic: {
        // Backend does not return the topic ID in ArticleResponse (MVP limitation)
        id: null,
        name: response.topicName,
      },
    };
  }
}
