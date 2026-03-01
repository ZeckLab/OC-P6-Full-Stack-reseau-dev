import { Component, inject, OnInit } from '@angular/core';
import { Articles } from '../../articles';
import { ArticleList } from '../../components/list/article-list';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-feed',
  imports: [ArticleList, RouterModule],
  templateUrl: './feed.html',
  styleUrl: './feed.scss',
})
export class Feed implements OnInit {
  private readonly articlesFacade = inject(Articles);

  articles = this.articlesFacade.articles$;
  sortDirection = this.articlesFacade.sortDirection;
  error = this.articlesFacade.error;

  ngOnInit() {
    this.articlesFacade.getFeed();
  }

  toggleSort() {
    this.articlesFacade.toggleSort();
  }
}
