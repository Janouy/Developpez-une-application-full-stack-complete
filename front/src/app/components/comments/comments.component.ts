import { Component, OnInit, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable, Subject, startWith, switchMap } from 'rxjs';
import { CommentComponent } from '../comment/comment.component';
import { CommentFormComponent } from '../comment-form/comment-form.component';
import { PostsService } from '../../services/posts.service';
import { Comment } from '../../interfaces/comment.interface';

@Component({
  selector: 'app-comments',
  imports: [CommonModule, CommentComponent, CommentFormComponent],
  templateUrl: './comments.component.html',
  styleUrl: './comments.component.scss',
})
export class CommentsComponent implements OnInit {
  @Input() postId!: number;
  comments$!: Observable<Comment[]>;

  private refresh$ = new Subject<void>();
  private destroy$!: Subject<boolean>;

  constructor(private postsService: PostsService) {}

  ngOnInit(): void {
    this.destroy$ = new Subject<boolean>();
    this.comments$ = this.refresh$.pipe(
      startWith(void 0),
      switchMap(() => this.postsService.getPostComments(this.postId))
    );
  }

  onCommentAdded(): void {
    this.refresh$.next();
  }

  ngOnDestroy(): void {
    this.destroy$.next(true);
  }
}
