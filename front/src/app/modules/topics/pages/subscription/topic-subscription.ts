import { Component, inject, OnInit } from '@angular/core';
import { TopicList } from '../../components/list/topic-list';
import { Topics } from '../../services/topics';
import { TopicWithSubscription } from '../../models/topic.response';

/** Displays the user's topics with subscription status and handles toggle actions. */
@Component({
  selector: 'app-topics-subscription',
  template: `
    @if (topicError()) {
      <p class="error">{{ topicError() }}</p>
    } @else {
      <app-topics-list [topics]="topics()" (toggled)="onToggle($event)"></app-topics-list>
    }
  `,
  imports: [TopicList],
})
export class TopicSubscription implements OnInit {
  private readonly topicsFacade = inject(Topics);

  // Expose topics and error signals to the template
  topics = this.topicsFacade.topicsWithSubscription;
  topicError = this.topicsFacade.error;

  ngOnInit() {
    // Load topics with subscription status on component subscription
    this.topicsFacade.getAllWithSubscription().subscribe();
  }

  /**
   * Handles subscription toggle events coming from the topics list.
   */
  onToggle(topic: TopicWithSubscription) {
    this.topicsFacade.toggleSubscription(topic).subscribe();
  }
}
