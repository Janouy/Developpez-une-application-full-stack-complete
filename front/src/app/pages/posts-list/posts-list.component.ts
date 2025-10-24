import { Component, OnDestroy, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable, Subject } from 'rxjs';
import { RouterLink } from '@angular/router';
import { map } from 'rxjs/operators';
import { Post } from '../../interfaces/post.interface';
import { PostsService } from '../../services/posts.service';
import { PostComponent } from '../../components/post/post.component';
import { HeaderComponent } from '../../components/header/header.component';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { faArrowDown, faArrowUp } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-posts-list',
  imports: [
    CommonModule,
    PostComponent,
    HeaderComponent,
    FaIconComponent,
    RouterLink,
  ],
  templateUrl: './posts-list.component.html',
  styleUrl: './posts-list.component.scss',
})
export class PostsListComponent implements OnInit, OnDestroy {
  posts$!: Observable<Post[]>;
  faArrowDown = faArrowDown;
  faArrowUp = faArrowUp;
  isChronologicallySorted = signal<boolean>(true);

  private destroy$!: Subject<boolean>;

  constructor(private postsService: PostsService) {}
  ngOnInit(): void {
    this.destroy$ = new Subject<boolean>();
    this.posts$ = this.postsService.getSubscribedPosts();
  }

  ngOnDestroy(): void {
    this.destroy$.next(true);
  }

  sortPosts(): void {
    this.posts$ = this.posts$.pipe(map((posts) => [...posts].reverse()));
    this.isChronologicallySorted.update((value) => !value);
  }
}
