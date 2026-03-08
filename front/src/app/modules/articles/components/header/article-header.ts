import { Component, input } from '@angular/core';
import { Article } from '../../models/article.model';
import { DatePipe } from '@angular/common';

/** Displays an article header with configurable layout (title, metadata, topic, sizes). */
@Component({
  selector: 'app-article-header',
  imports: [DatePipe],
  template: `
    <h2 class="title" [class.compact]="compact()" [style.font-size]="titleSize()">
      {{ article().title }}
    </h2>
    <div class="meta" [style.font-size]="metaSize()">
      <span class="date">{{ article().createdAt | date: 'dd/MM/yyyy' }}</span>
      <span class="author">{{ article().authorUsername }}</span>
      @if (showTopic()) {
        <span class="topic">{{ article().topic.name }}</span>
      }
    </div>
  `,
  styles: [
    `
      .title {
        font-weight: 700;
        margin: 0;
      } 

      .title.compact {
        display: -webkit-box;
        -webkit-line-clamp: 2;
        line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
        min-height: 2.75rem;
      }

      .meta { 
        display: flex;
        flex-wrap: wrap;
        column-gap: 1rem;
        row-gap: 0.5rem;
        font-weight: 400;
        padding-top: 0.5rem;
      }
    `,
  ],
})
export class ArticleHeader {
  showTopic = input<boolean>(false);
  article = input.required<Article>();
  compact = input<boolean>(false); //

  titleSize = input<string>('16px');
  metaSize = input<string>('14px');
}
