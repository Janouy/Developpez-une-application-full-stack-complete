import { Component } from '@angular/core';
import { HeaderComponent } from '../../components/header/header.component';
import { SubjectsListInnerComponent } from '../../components/subjects-list-inner/subjects-list-inner.component';

@Component({
  selector: 'app-subjects-list',
  imports: [HeaderComponent, SubjectsListInnerComponent],
  templateUrl: './subjects-list.component.html',
  styleUrl: './subjects-list.component.scss',
})
export class SubjectsListComponent {}
