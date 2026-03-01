import { Component, input, output } from '@angular/core';
import { TopicWithSubscription } from '../../models/topic.response';

@Component({
  selector: 'app-topic-card',
  template: `
    <div class="card">
      <h3>{{ topic().name }}</h3>
      <p>{{ topic().description }}</p>

      <button
        [class.subscribed]="topic().subscribed"
        [disabled]="topic().subscribed"
        (click)="onToggle()"
      >
        {{ topic().subscribed ? 'Déjà abonné' : 'S’abonner' }}
      </button>
    </div>
  `,
  styles: [
    `
      .card {
        padding: 1rem 1.5rem;
        border-radius: 0.5rem;
        background: white;
        border: 1px solid #dcdcdc;
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.06);
        height: 11rem;
        background-color: #f5f5f5;
        display: flex;
        flex-direction: column;
        justify-content: space-between;

        @media (max-width: 900px) {
          height: 12.5rem;
        }

        @media (max-width: 600px) {
          height: auto;
          min-height: 11rem;
        }

        h3 {
          margin: 0;
          font-size: 1rem;
          font-weight: 700;

          @media (max-width: 900px) {
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
            min-height: 2.3rem;
          }

          @media (max-width: 600px) {
            display: block;
            -webkit-line-clamp: unset;
            -webkit-box-orient: unset;
            overflow: visible;
            min-height: auto;
          }
        }

        p {
          font-size: 0.875rem;
          line-height: 1.4;
          font-weight: 400;
          display: -webkit-box;
          -webkit-line-clamp: 3;
          -webkit-box-orient: vertical;
          overflow: hidden;
          min-height: 3.7rem;
        }

        button {
          align-self: center;
          padding: 0.6rem 1rem;
          border-radius: 0.5rem;
          border: none;
          font-weight: 600;
          cursor: pointer;
          transition: 0.2s ease;

          &.subscribed {
            background: #939393;
            color: white;
            cursor: not-allowed;
          }

          &:not(.subscribed) {
            background: #7763c5;
            color: white;
          }

          &:disabled {
            opacity: 0.7;
            cursor: not-allowed;
          }
        }
      }
    `,
  ],
})
export class TopicCard {
  // Topic with subscription status to render
  topic = input.required<TopicWithSubscription>();
  // Emits when the user requests to toggle subscription for this topic
  toggle = output<TopicWithSubscription>();

  onToggle() {
    if (!this.topic().subscribed) {
      this.toggle.emit(this.topic());
    }
  }
}
