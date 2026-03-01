import { inject, Injectable, signal } from '@angular/core';
import { TopicsApi } from './topics.api';
import { Topic } from './models/topic.model';
import { TopicWithSubscription } from './models/topic.response';
import { ToastService } from '../../shared/ui/toast/toast.service';
import { ErrorMapper } from '../../core/messages/error-mapper';
import { TopicMessageMapper } from '../../core/messages/topic-message-mapper';
import { MESSAGES } from '../../core/messages/constants';

@Injectable({ providedIn: 'root' })
export class Topics {
  private readonly api = inject(TopicsApi);
  private readonly toast = inject(ToastService);
  private readonly errorMapper = inject(ErrorMapper);
  private readonly topicMessageMapper = inject(TopicMessageMapper);

  private readonly _topics = signal<Topic[]>([]);
  topics = this._topics.asReadonly();

  private readonly _topicsWithSubscription = signal<TopicWithSubscription[]>([]);
  topicsWithSubscription = this._topicsWithSubscription.asReadonly();

  private readonly _error = signal<string | null>(null);
  error = this._error.asReadonly();

  /**
   * Loads all topics for the dropdown (select in article-create) and updates the topics signal.
   */
  getAll() {
    this._error.set(null);

    this.api.getAll().subscribe({
      next: (response) => this._topics.set(response),
      error: () => this._error.set(MESSAGES.TOPICS_LOAD_ERROR),
    });
  }

  /**
   * Loads topics with subscription status for the current user.
   */
  getAllWithSubscription() {
    this._error.set(null);

    this.api.getAllWithSubscription().subscribe({
      next: (response) => this._topicsWithSubscription.set(response),
      error: () => this._error.set(MESSAGES.TOPICS_LOAD_ERROR),
    });
  }

  /**
   * Toggles subscription for a given topic and updates local state + toast.
   */
  toggleSubscription(topic: TopicWithSubscription) {
    const request = topic.subscribed
      ? this.api.unsubscribe(topic.id)
      : this.api.subscribe(topic.id);

    request.subscribe({
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
    });
  }
}
