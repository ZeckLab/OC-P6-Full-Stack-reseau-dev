import { Component, input } from '@angular/core';
import { Comment } from '../models/comment.model';
import { DatePipe } from '@angular/common';

/** Displays a single comment with author and date. */
@Component({
  selector: 'app-comment',
  imports: [DatePipe],
  template: `
    <div class="comment">
      <div class="header">
        <span class="username">{{ comment().authorUsername }}</span>
        <span class="date">{{ comment().createdAt | date: 'dd/MM/yyyy' }}</span>
        <span class="hour">{{ comment().createdAt | date: 'HH:mm' }}</span>
      </div>

      <p class="content">{{ comment().content }}</p>
    </div>
  `,
  styles: [
    `
      .comment {
        padding: 1rem;
        display: grid;
        grid-template-columns: auto 1fr;
        gap: 1rem;
        align-items: start;
        padding: 1rem;

        @media (max-width: 600px) {
          display: flex;
          flex-direction: column;
          justify-content: flex-start;
          gap: 0.5rem;
        }

        .header {
          display: flex;
          flex-direction: column;
          align-items: flex-end;
          gap: 0.15rem;
          font-size: 0.85rem;
          color: #666;

          @media (max-width: 600px) {
            flex-wrap: 1;
            flex-direction: row;
            justify-content: flex-start;
            gap: 1rem;
            width: 100%;
          }
        }

        .username {
          font-weight: 600;
          color: #333;
        }

        .content {
          margin: 0;
          font-size: 1rem;
          line-height: 1.4;
          border-radius: 10px;
          background: #f4f4f4;
          color: #222;
          width: 100%;
          padding: 1rem;
        }
      }
    `,
  ],
})
export class CommentComponent {
  comment = input.required<Comment>();
}
