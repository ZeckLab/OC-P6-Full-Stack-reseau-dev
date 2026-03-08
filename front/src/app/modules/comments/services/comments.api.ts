import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommentResponse } from '../models/comment.response';

@Injectable({ providedIn: 'root' })
export class CommentsApi {
  private readonly http = inject(HttpClient);

  getByArticle(articleId: string): Observable<CommentResponse[]> {
    return this.http.get<CommentResponse[]>(`/articles/${articleId}/comments`);
  }

  create(articleId: string, content: string): Observable<CommentResponse> {
    return this.http.post<CommentResponse>(`/articles/${articleId}/comments`, {
      content
    });
  }
}
