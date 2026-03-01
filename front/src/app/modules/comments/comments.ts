import { inject, Injectable, signal } from '@angular/core';
import { CommentsApi } from './comments.api';
import { Comment } from './models/comment.model';
import { CommentResponse } from './models/comment.response';
import { map } from 'rxjs';
import { MESSAGES } from '../../core/messages/constants';

@Injectable({ providedIn: 'root' })
export class Comments {
  private readonly api = inject(CommentsApi);

  private readonly _comments = signal<Comment[]>([]);
  comments = this._comments.asReadonly();

  private readonly _error = signal<string | null>(null);
  error = this._error.asReadonly();

  load(articleId: string) {
    this.api.getByArticle(articleId).subscribe({
      next: (response) => {
        this._error.set(null);
        this._comments.set(response.map(item => this.mapToComment(item)));
      },
      error: () => {
        this._error.set(MESSAGES.COMMENTS_LOAD_ERROR);
        this._comments.set([]);
      }
    });
  }

  create(articleId: string, content: string) {
    return this.api.create(articleId, content).pipe(
      map((response) => {
        const comment = this.mapToComment(response);
        
        // Add the new comment locally to keep the UI responsive
        // and avoid reloading the entire comment list.
        this._comments.update(list => [...list, comment]);
        return comment;
      })
    );
  }

    private mapToComment(response: CommentResponse): Comment {
        return {
        id: response.id,
        content: response.content,
        authorUsername: response.authorUsername,
        createdAt: response.createdAt
        };
    }
}
