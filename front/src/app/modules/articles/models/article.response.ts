import { CommentResponse } from "../../comments/models/comment.response";

export interface ArticleResponse {
  id: string;
  title: string;
  content: string;
  authorUsername: string;
  topicName: string;
  createdAt: string;
  comments: CommentResponse[];
}
