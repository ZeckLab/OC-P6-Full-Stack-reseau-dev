import { computed, inject, Injectable, signal } from '@angular/core';
import { ArticlesApi } from './articles.api';
import { Article } from './models/article.model';
import { ArticleResponse } from './models/article.response';
import { CreateArticleRequest } from './models/article.request';
import { map } from 'rxjs';
import { MESSAGES } from '../../core/messages/constants';

@Injectable({ providedIn: 'root' })
export class Articles {
  private readonly api = inject(ArticlesApi);

  private readonly _articles = signal<Article[]>([]);
  articles$ = computed(() => {
    const sort = this._sort();
    const list = [...this._articles()];
    return sort === 'desc'
      ? list.sort((a, b) => b.createdAt.localeCompare(a.createdAt))
      : list.sort((a, b) => a.createdAt.localeCompare(b.createdAt));
  });

  private readonly _sort = signal<'asc' | 'desc'>('desc');
  sortDirection = this._sort.asReadonly();

  private readonly _selected = signal<Article | null>(null);
  selectedArticle = this._selected.asReadonly();

  private readonly _error = signal<string | null>(null);
  error = this._error.asReadonly();

  setSelected(article: Article) {
    this._selected.set(article);
  }

  toggleSort() {
    this._sort.update((s) => (s === 'desc' ? 'asc' : 'desc'));
  }

  getFeed() {
    this.api.getFeed().subscribe({
      next: (response) => {
        this._error.set(null);
        this._articles.set(response.map((item) => this.mapToArticle(item)));
      },
      error: () => {
        this._error.set(MESSAGES.ARTICLES_LOAD_ERROR);
        this._articles.set([]);
      },
    });
  }

  getById(id: string) {
    this.api.getById(id).subscribe({
      next: (item) => {
        this._error.set(null);
        this._selected.set(this.mapToArticle(item));
      },
      error: () => {
        this._selected.set(null);
        this._error.set('not-found');
      },
    });
  }

  createArticle(data: CreateArticleRequest) {
    return this.api.create(data).pipe(
      map((response) => {
        const article = this.mapToArticle(response);
        this._articles.update((list) => [article, ...list]);
        return article;
      }),
    );
  }

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
