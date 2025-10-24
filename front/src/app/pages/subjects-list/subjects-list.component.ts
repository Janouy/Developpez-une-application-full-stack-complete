import { Component, OnDestroy, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable, Subject, startWith, switchMap } from 'rxjs';
import { SubjectService } from '../../services/subjects.service';
import { HeaderComponent } from '../../components/header/header.component';
import { SubjectComponent } from '../../components/subject/subject.component';
import { LoaderComponent } from '../../components/loader/loader.component';
import { SubjectInterface } from '../../interfaces/subjectInterface.interface';

@Component({
  selector: 'app-subjects-list',
  imports: [CommonModule, HeaderComponent, SubjectComponent, LoaderComponent],
  templateUrl: './subjects-list.component.html',
  styleUrl: './subjects-list.component.scss',
})
export class SubjectsListComponent implements OnInit, OnDestroy {
  subjects$!: Observable<SubjectInterface[]>;

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
