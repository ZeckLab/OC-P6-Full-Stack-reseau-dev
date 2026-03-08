import { inject, Injectable, signal } from '@angular/core';
import { TopicsApi } from './topics.api';
import { Topic } from '../models/topic.model';
import { TopicWithSubscription } from '../models/topic.response';
import { ToastService } from '../../../shared/ui/toast/toast.service';
import { ErrorMapper } from '../../../core/messages/error-mapper';
import { TopicMessageMapper } from '../../../core/messages/topic-message-mapper';
import { MESSAGES } from '../../../core/messages/constants';
import { tap } from 'rxjs';

/** Manages topics, user subscriptions and related state for the UI. */
@Injectable({ providedIn: 'root' })
export class Topics {
  private readonly api = inject(TopicsApi);
  private readonly toast = inject(ToastService);
  private readonly errorMapper = inject(ErrorMapper);
  private readonly topicMessageMapper = inject(TopicMessageMapper);

  /** List of all topics (used for dropdowns and article creation). */
  private readonly _topics = signal<Topic[]>([]);
  topics = this._topics.asReadonly();

  /** Topics enriched with the user's subscription status (used in subscription page). */
  private readonly _topicsWithSubscription = signal<TopicWithSubscription[]>([]);
  topicsWithSubscription = this._topicsWithSubscription.asReadonly();

  private readonly _error = signal<string | null>(null);
  error = this._error.asReadonly();

  /**
   * Loads all topics for the dropdown (select in article-create) and updates the topics signal.
   */
  getAll() {
    this._error.set(null);

    return this.api.getAll().pipe(
      tap({
        next: (response) => this._topics.set(response),
        error: () => this._error.set(MESSAGES.TOPICS_LOAD_ERROR),
      }),
    );
  }

  /**
   * Loads topics with subscription status for the current user.
   */
  getAllWithSubscription() {
    this._error.set(null);

    return this.api.getAllWithSubscription().pipe(
      tap({
        next: (response) => this._topicsWithSubscription.set(response),
        error: () => this._error.set(MESSAGES.TOPICS_LOAD_ERROR),
      }),
    );
  }

  /**
   * Toggles subscription for a given topic and updates local state + toast.
   */
  toggleSubscription(topic: TopicWithSubscription) {
    const request = topic.subscribed
      ? this.api.unsubscribe(topic.id)
      : this.api.subscribe(topic.id);

    return request.pipe(
      tap({
        next: (response) => {
          const { message, type } = this.topicMessageMapper.mapMessage(response);
          this.toast.show(message, type);
          this._topicsWithSubscription.update((list) =>
            list.map((t) => (t.id === topic.id ? { ...t, subscribed: !t.subscribed } : t)),
          );
        },
        error: (err) => {
          console.warn(this.errorMapper.mapTopicError(err));
          this.toast.show(MESSAGES.SERVER_ERROR, 'error');
        },
      }),
    );
  }
}
