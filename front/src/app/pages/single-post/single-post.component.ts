import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../../components/header/header.component';
import { BackArrowComponent } from '../../components/back-arrow/back-arrow.component';
import { PostComponent } from '../../components/post/post.component';
import { CommentsComponent } from '../../components/comments/comments.component';
import { Post } from '../../interfaces/post.interface';
import { PostsService } from '../../services/posts.service';

@Component({
  selector: 'app-single-post',
  imports: [
    CommonModule,
    BackArrowComponent,
    HeaderComponent,
    PostComponent,
    CommentsComponent,
  ],
  templateUrl: './single-post.component.html',
  styleUrl: './single-post.component.scss',
})
export class SinglePostComponent implements OnInit {
  post$!: Observable<Post>;
  postId!: number;

  constructor(
    private postsService: PostsService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.postId = this.route.snapshot.params['id'];
    this.post$ = this.postsService.getPostById(this.postId);
  }
}
