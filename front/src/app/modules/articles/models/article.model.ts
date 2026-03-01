export interface Article {
  id: string;
  title: string;
  content: string;
  createdAt: string;

  authorUsername: string;

  topic: {
    id: string | null;
    name: string;
  };
}
