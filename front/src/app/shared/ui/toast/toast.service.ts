import { Injectable, signal } from '@angular/core';

export type ToastType = 'success' | 'error' | 'info';

@Injectable({ providedIn: 'root' })
export class ToastService {
  // Current toast message (null = hidden)
  message = signal<string | null>(null);

  // Current toast type (success, error, info)
  type = signal<ToastType>('info');

  private timeoutId: any;

  /**
   * Displays a toast message for a given duration.
   */
  show(msg: string, type: ToastType = 'info', duration = 3000) {
    this.message.set(msg);
    this.type.set(type);

    clearTimeout(this.timeoutId);
    this.timeoutId = setTimeout(() => this.message.set(null), duration);
  }
}