import { Component, input, output } from "@angular/core";
import { TopicCard } from "../card/topic-card";
import { TopicWithSubscription } from "../../models/topic.response";

@Component({
  selector: 'app-topics-list',
  imports: [TopicCard],
  template: `
    <div class="topics-grid">
      @for (t of topics(); track t.id) {
        <app-topic-card [topic]="t" (toggle)="toggle.emit($event)"></app-topic-card>
      }
    </div>
  `,
  styles: [
    `
      .topics-grid {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 1.5rem;
        margin-top: 1rem;

        @media (max-width: 600px) {
          grid-template-columns: 1fr;
        }
      }
    `,
  ]
})
export class TopicList {
  // List of topics with subscription status to display
  topics = input.required<TopicWithSubscription[]>();
  // Emits when the user toggles subscription for a topic
  toggle = output<TopicWithSubscription>()
}
