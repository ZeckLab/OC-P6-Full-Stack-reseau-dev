import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Articles } from '../../articles';
import { ArticleHeader } from '../../components/header/article-header';
import { Comments } from '../../../comments/comments';
import { CommentComponent } from '../../../comments/comment/comment';

@Component({
  selector: 'app-article-detail',
  imports: [ArticleHeader, CommentComponent],
  templateUrl: './article-detail.html',
  styleUrls: ['./article-detail.scss'],
})
export class ArticleDetail {
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly articlesFacade = inject(Articles);
  private readonly commentsFacade = inject(Comments);

  article = this.articlesFacade.selectedArticle;
  comments = this.commentsFacade.comments;
  commentsError = this.commentsFacade.error;

  newComment = signal('');

  error = this.articlesFacade.error;

  constructor() {
    // If the article was passed through router state (coming from the feed),
    // we reuse it to avoid an unnecessary API call.
    // If not (page refresh or direct URL access), we fetch it from the backend.
    const stateArticle = history.state['article'];

    const id = this.route.snapshot.paramMap.get('id')!;

    if (stateArticle) {
      this.articlesFacade.setSelected(stateArticle);
    } else {
      this.articlesFacade.getById(id);
    }

    this.commentsFacade.load(id);
  }

  goBack() {
    this.router.navigate(['/articles/feed']);
  }

  sendComment() {
    const content = this.newComment().trim();
    if (!content) return;

    const article = this.articlesFacade.selectedArticle();
    if (!article) return;

    this.commentsFacade.create(article.id, content).subscribe(() => {
      this.newComment.set('');
    });
  }
}
