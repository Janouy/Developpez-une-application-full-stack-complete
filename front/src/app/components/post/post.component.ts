import { Component, Input } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { Post } from '../../interfaces/post.interface';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post',
  imports: [CommonModule, DatePipe],
  templateUrl: './post.component.html',
  styleUrl: './post.component.scss',
})
export class PostComponent {
  constructor(private router: Router) {}
  @Input() post!: Post;
  @Input() showSubject = false;
  @Input() background = 'bg-neutral-50';
  @Input() content =
    'overflow-hidden text-ellipsis [display:-webkit-box] [-webkit-line-clamp:5] [-webkit-box-orient:vertical] mt-3';

  onViewPost() {
    this.router.navigateByUrl(`posts/${this.post.id}`);
  }
}
