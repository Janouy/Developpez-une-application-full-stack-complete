import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderMiniComponent } from './header-mini.component';

describe('HeaderMiniComponent', () => {
  let component: HeaderMiniComponent;
  let fixture: ComponentFixture<HeaderMiniComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeaderMiniComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HeaderMiniComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
