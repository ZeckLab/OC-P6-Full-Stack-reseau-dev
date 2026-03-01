import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { ArticleResponse } from './models/article.response';
import { CreateArticleRequest } from './models/article.request';

@Injectable({
  providedIn: 'root',
})
export class ArticlesApi {
  private readonly http = inject(HttpClient);
  private readonly articleUrl = '/articles';

  getFeed(): Observable<ArticleResponse[]> {
    return this.http.get<ArticleResponse[]>("/feed");
  }

  getById(id: string): Observable<ArticleResponse> {
    return this.http.get<ArticleResponse>(`${this.articleUrl}/${id}`);
  }

  create(request: CreateArticleRequest): Observable<ArticleResponse> {
    return this.http.post<ArticleResponse>(`${this.articleUrl}`, request);
  }
}
