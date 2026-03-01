// Topic with subscription for the current authenticated user
export interface TopicWithSubscription {
  id: string;
  name: string;
  description: string;
  subscribed: boolean;
}