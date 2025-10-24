import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { PostsService } from '../../services/posts.service';
import { CommentRequest } from '../../interfaces/commentRequest.interface';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { faPaperPlane } from '@fortawesome/free-regular-svg-icons';

@Component({
  selector: 'app-comment-form',
  imports: [ReactiveFormsModule, FaIconComponent],
  templateUrl: './comment-form.component.html',
  styleUrl: './comment-form.component.scss',
})
export class CommentFormComponent implements OnInit {
  faPaperPlane = faPaperPlane;
  commentForm!: FormGroup;
  onError = false;
  @Input() postId!: number;
  @Output() commentAdded = new EventEmitter<void>();

  constructor(
    private postsService: PostsService,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    this.commentForm = this.formBuilder.group({
      content: [null, [Validators.required]],
    });
  }

  onAddComment(): void {
    if (this.commentForm.invalid) return;
    const commentRequest = this.commentForm.value as CommentRequest;
    this.postsService.addComment(commentRequest, this.postId).subscribe({
      next: () => {
        this.commentForm.reset();
        this.commentAdded.emit();
      },
      error: (_) => (this.onError = true),
    });
  }
}
