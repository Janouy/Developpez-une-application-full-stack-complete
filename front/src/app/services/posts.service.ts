import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Post } from '../interfaces/post.interface';
import { Comment } from '../interfaces/comment.interface';
import { CommentRequest } from '../interfaces/commentRequest.interface';
import { PostRequest } from '../interfaces/postRequest.interface';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class PostsService {
  private pathService = environment.apiUrl;

  constructor(private httpClient: HttpClient) {}

  public getSubscribedPosts(): Observable<Post[]> {
    return this.httpClient.get<Post[]>(`${this.pathService}/posts/feed`);
  }

  public getPostById(id: number): Observable<Post> {
    return this.httpClient.get<Post>(`${this.pathService}/posts/${id}`);
  }

  public getPostComments(id: number): Observable<Comment[]> {
    return this.httpClient.get<Comment[]>(
      `${this.pathService}/posts/${id}/comments`
    );
  }

  public addComment(
    newComment: CommentRequest,
    postId: number
  ): Observable<void> {
    return this.httpClient.post<void>(
      `${this.pathService}/posts/${postId}/comments`,
      newComment
    );
  }

  public addPost(newPost: PostRequest): Observable<void> {
    return this.httpClient.post<void>(`${this.pathService}/posts`, newPost);
  }
}
