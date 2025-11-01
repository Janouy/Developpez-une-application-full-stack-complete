import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubjectsListInnerComponent } from './subjects-list-inner.component';

describe('SubjectsListInnerComponent', () => {
  let component: SubjectsListInnerComponent;
  let fixture: ComponentFixture<SubjectsListInnerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubjectsListInnerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubjectsListInnerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
