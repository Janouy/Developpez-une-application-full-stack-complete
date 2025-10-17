import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-back-arrow',
  imports: [RouterLink, FaIconComponent],
  templateUrl: './back-arrow.component.html',
  styleUrl: './back-arrow.component.scss',
})
export class BackArrowComponent {
  faArrowLeft = faArrowLeft;
}
