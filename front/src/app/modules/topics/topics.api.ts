import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Topic } from './models/topic.model';
import { TopicWithSubscription } from './models/topic.response';
import { ApiResponse } from '../../core/messages/api.response';

@Injectable({
  providedIn: 'root',
})
export class TopicsApi {
  private readonly http = inject(HttpClient);
  private readonly topicsUrl = '/topics';

  /**
   * Fetches all available topics
   */
  getAll(): Observable<Topic[]> {
    return this.http.get<Topic[]>(this.topicsUrl);
  }

  /**
   * Fetches topics with subscription status for the current authenticated user
   */
  getAllWithSubscription(): Observable<TopicWithSubscription[]> {
    return this.http.get<TopicWithSubscription[]>(`${this.topicsUrl}/me`);
  }

  /**
   * Subscribes the current user to the given topic
   */
  subscribe(topicId: string): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.topicsUrl}/${topicId}/subscribe`, {});
  }

  /**
   * Unsubscribes the current user from the given topic
   */
  unsubscribe(topicId: string): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.topicsUrl}/${topicId}/unsubscribe`, {});
  }
}
