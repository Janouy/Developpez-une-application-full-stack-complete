import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { BackArrowComponent } from '../../components/back-arrow/back-arrow.component';
import { HeaderComponent } from '../../components/header/header.component';
import { Observable, map } from 'rxjs';
import { Subject } from '../../interfaces/subject.interface';
import { PostRequest } from '../../interfaces/postRequest.interface';
import { SubjectService } from '../../services/subjects.service';
import { PostsService } from '../../services/posts.service';

@Component({
  selector: 'app-new-post',
  imports: [
    HeaderComponent,
    ReactiveFormsModule,
    HeaderComponent,
    BackArrowComponent,
    CommonModule,
  ],
  templateUrl: './new-post.component.html',
  styleUrl: './new-post.component.scss',
})
export class NewPostComponent implements OnInit {
  postForm!: FormGroup;
  onError = false;
  subjects$!: Observable<Subject[]>;

  constructor(
    private formBuilder: FormBuilder,
    private subjectService: SubjectService,
    private postsService: PostsService,
    private router: Router
  ) {}
  ngOnInit(): void {
    this.postForm = this.formBuilder.group({
      subjectId: ['', [Validators.required]],
      title: [null, [Validators.required]],
      content: [null, [Validators.required]],
    });
    this.subjects$ = this.subjectService
      .getSubjects()
      .pipe(map((subjects: Subject[]) => subjects.filter((s) => s.subscribed)));
  }

  onAddPost(): void {
    if (this.postForm.invalid) return;
    const postRequest = this.postForm.value as PostRequest;
    this.postsService.addPost(postRequest).subscribe({
      next: () => {
        this.router.navigateByUrl('/');
      },
      error: (_) => (this.onError = true),
    });
  }
}
