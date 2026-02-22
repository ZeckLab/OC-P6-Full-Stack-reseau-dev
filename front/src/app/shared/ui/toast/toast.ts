import { Component, inject } from '@angular/core';
import { ToastService } from './toast.service';

@Component({
  selector: 'app-toast',
  standalone: true,
  template: `
    @if (toast.message()) {
      <div
        class="toast"
        [class.success]="toast.type() === 'success'"
        [class.error]="toast.type() === 'error'"
        [class.info]="toast.type() === 'info'"
      >
        {{ toast.message() }}
      </div>
    }
  `,
  styles: [
    `
      .toast {
        position: fixed;
        top: 1.25rem;
        left: 50%;
        transform: translateX(-50%);
        background: #323232;
        color: white;
        padding: 0.75rem 1.25rem;
        border-radius: 0.375rem;
        box-shadow: 0 0.125rem 0.625rem rgba(0, 0, 0, 0.2);
        animation: fadeInOut 3s ease forwards;
        z-index: 9999;
        transition: opacity 0.2s ease;

        @media (max-width: 600px) {
          top: 0.625rem;
          padding: 0.625rem 1rem;
          font-size: 0.875rem;
        }
      }

      .toast.success {
        background: #4caf50;
      }
      .toast.error {
        background: #f44336;
      }
      .toast.info {
        background: #2196f3;
      }

      @keyframes fadeInOut {
        0% {
          opacity: 0;
          transform: translate(-50%, -0.625rem);
        }
        10% {
          opacity: 1;
          transform: translate(-50%, 0);
        }
        90% {
          opacity: 1;
        }
        100% {
          opacity: 0;
          transform: translate(-50%, 0.625rem);
        }
      }
    `,
  ],
})
export class Toast {
  toast = inject(ToastService);
}
