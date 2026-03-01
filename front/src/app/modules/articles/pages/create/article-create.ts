import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ErrorMapper } from '../../../../core/messages/error-mapper';
import { ToastService } from '../../../../shared/ui/toast/toast.service';
import { Articles } from '../../articles';
import { Topics } from '../../../topics/topics';

@Component({
  selector: 'app-article-create',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './article-create.html',
  styleUrl: './article-create.scss'
})
export class ArticleCreate implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly articlesFacade = inject(Articles);
  private readonly errorMapper = inject(ErrorMapper);
  private readonly toast = inject(ToastService);
  private readonly topicsFacade = inject(Topics);

  // Reactive article creation form with custom validators
  readonly form = this.fb.group({
    title: ['', [Validators.required, Validators.minLength(3)]],
    content: ['', [Validators.required, Validators.minLength(10)]],
    topicId: ['', Validators.required],
  });

  topics = this.topicsFacade.topics;

  ngOnInit() {
    this.topicsFacade.getAll();
  }

  // Handles form submission and triggers article creation
  submit() {
    if (this.form.invalid) return;

    const { title, content, topicId } = this.form.getRawValue();

    this.articlesFacade.createArticle({
      title: title!,
      content: content!,
      topicId: Number(topicId)
    }).subscribe({
      next: (created) => {
        this.toast.show('Article créé avec succès !', 'success')
        this.router.navigate(['/articles', created.id], { state: { article: created } });
      },
      error: (err) => {
        const errorMessage = this.errorMapper.mapArticleError(err);
        this.toast.show(errorMessage, 'error');
      }
    });
  }

  // Navigate back to the home page
  goBack() {
    this.router.navigate(['/']);
  }
}