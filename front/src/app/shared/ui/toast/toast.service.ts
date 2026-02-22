import { Injectable, signal } from '@angular/core';

export type ToastType = 'success' | 'error' | 'info';

@Injectable({ providedIn: 'root' })
export class ToastService {
  message = signal<string | null>(null);
  type = signal<ToastType>('info');

  private timeoutId: any;

  show(msg: string, type: ToastType = 'info', duration = 3000) {
    this.message.set(msg);
    this.type.set(type);

    clearTimeout(this.timeoutId);
    this.timeoutId = setTimeout(() => this.message.set(null), duration);
  }
}