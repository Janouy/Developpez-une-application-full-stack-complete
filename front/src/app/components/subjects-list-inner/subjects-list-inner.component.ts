import { Component, OnDestroy, OnInit, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable, Subject, startWith, switchMap } from 'rxjs';
import { SubjectService } from '../../services/subjects.service';
import { SubjectComponent } from '../../components/subject/subject.component';
import { LoaderComponent } from '../../components/loader/loader.component';
import { SubjectInterface } from '../../interfaces/subjectInterface.interface';

@Component({
  selector: 'app-subjects-list-inner',
  imports: [CommonModule, SubjectComponent, LoaderComponent],
  templateUrl: './subjects-list-inner.component.html',
  styleUrl: './subjects-list-inner.component.scss',
})
export class SubjectsListInnerComponent implements OnInit, OnDestroy {
  subjects$!: Observable<SubjectInterface[]>;
  @Input() canUnsubscribe = false;

  private refresh$ = new Subject<void>();
  private destroy$!: Subject<boolean>;

  constructor(private subjectService: SubjectService) {}

  ngOnInit(): void {
    this.destroy$ = new Subject<boolean>();
    this.subjects$ = this.refresh$.pipe(
      startWith(void 0),
      switchMap(() => this.subjectService.getSubjects())
    );
  }

  onSubscribing(): void {
    this.refresh$.next();
  }
  ngOnDestroy(): void {
    this.destroy$.next(true);
  }
}
