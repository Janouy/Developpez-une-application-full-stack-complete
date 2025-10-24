import { Component, Input } from '@angular/core';
import { Comment } from '../../interfaces/comment.interface';

@Component({
  selector: 'app-comment',
  imports: [],
  templateUrl: './comment.component.html',
  styleUrl: './comment.component.scss',
})
export class CommentComponent {
  @Input() comment!: Comment;
}
