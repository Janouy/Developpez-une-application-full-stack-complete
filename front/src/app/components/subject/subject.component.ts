import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SubjectService } from '../../services/subjects.service';
import { SubjectInterface } from '../../interfaces/subjectInterface.interface';

@Component({
  selector: 'app-subject',
  imports: [CommonModule],
  templateUrl: './subject.component.html',
  styleUrl: './subject.component.scss',
})
export class SubjectComponent {
  @Input() subject!: SubjectInterface;
  @Output() onSubscribing = new EventEmitter<void>();
  onError = false;

  constructor(private subjectService: SubjectService) {}

  onSubscribe(subjectId: number): void {
    this.subjectService.subjectSubscribe(subjectId).subscribe({
      next: () => {
        this.onSubscribing.emit();
      },
      error: (_) => (this.onError = true),
    });
  }
}
