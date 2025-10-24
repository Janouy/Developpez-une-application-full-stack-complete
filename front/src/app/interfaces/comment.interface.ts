export interface Comment {
  id: number;
  postId: number;
  authorId: number;
  authorName: String;
  content: String;
  createdAt: Date;
}
