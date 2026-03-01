import { Injectable } from '@angular/core';
import { MappedMessage } from './mapped.message';
import { ApiResponse } from './api.response';

@Injectable({ providedIn: 'root' })
export class TopicMessageMapper {
  /**
   * Maps a backend topic subscription response to a localized toast message.
   */
  mapMessage(response: ApiResponse): MappedMessage {
    const message = response.message;

    if (message === 'Subscribed to topic successfully')
      return { message: 'Abonnement réussi', type: 'success' };

    if (message === 'Unsubscribed from topic successfully')
      return { message: 'Désabonnement réussi', type: 'success' };

    if (message === 'Already subscribed to topic') return { message: 'Déjà abonné', type: 'info' };

    if (message === 'Already unsubscribed from topic')
      return { message: 'Déjà désabonné', type: 'info' };

    return { message: 'Action effectuée', type: 'info' };
  }
}
