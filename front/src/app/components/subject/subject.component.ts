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
  @Input() canUnsubscribe = true;
  @Output() onSubscribing = new EventEmitter<void>();
  onError = false;

  constructor(private subjectService: SubjectService) {}

  onSubscribe(subjectId: number): void {
    if (!this.subject.subscribed) {
      this.subjectService.subjectSubscribe(subjectId).subscribe({
        next: () => {
          this.onSubscribing.emit();
        },
        error: (_) => (this.onError = true),
      });
    } else if (this.subject.subscribed && this.canUnsubscribe) {
      this.subjectService.subjectUnsubscribe(subjectId).subscribe({
        next: () => {
          this.onSubscribing.emit();
        },
        error: (_) => (this.onError = true),
      });
    }
  }
}
