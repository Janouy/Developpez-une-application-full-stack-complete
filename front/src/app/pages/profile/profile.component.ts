import { Component, OnDestroy, OnInit } from '@angular/core';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Observable, Subject, startWith, switchMap, takeUntil } from 'rxjs';
import { UserService } from '../../services/user.service';
import { User } from '../../interfaces/user.interface';
import { LoaderComponent } from '../../components/loader/loader.component';
import { HeaderComponent } from '../../components/header/header.component';
import { SubjectsListInnerComponent } from '../../components/subjects-list-inner/subjects-list-inner.component';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-profile',
  imports: [
    ReactiveFormsModule,
    CommonModule,
    LoaderComponent,
    HeaderComponent,
    SubjectsListInnerComponent,
    FaIconComponent,
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
})
export class ProfileComponent implements OnInit, OnDestroy {
  faEye = faEye;
  faEyeSlash = faEyeSlash;
  me$!: Observable<User>;
  userForm!: FormGroup;
  onError = false;
  onSuccess = false;
  passwordRegex!: RegExp;
  hidePassword!: Boolean;

  private destroy$!: Subject<boolean>;
  private refresh$ = new Subject<void>();

  constructor(
    private userService: UserService,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    this.destroy$ = new Subject<boolean>();
    this.passwordRegex =
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&._\-])[A-Za-z\d@$!%*?&._\-]{8,}$/;
    this.me$ = this.refresh$.pipe(
      startWith(void 0),
      switchMap(() => this.userService.getUser())
    );
    this.userForm = this.formBuilder.group(
      {
        name: [null, [Validators.required, Validators.minLength(4)]],
        email: [null, [Validators.required, Validators.email]],
        newPassword: [null, [Validators.pattern(this.passwordRegex)]],
      },
      { updateOn: 'blur' }
    );
    this.me$.pipe(takeUntil(this.destroy$)).subscribe((user) => {
      if (user) {
        this.userForm.patchValue({
          name: user.name,
          email: user.email,
        });
      }
    });
  }

  onUpdateUser(): void {
    this.onSuccess = false;
    this.onError = false;
    if (this.userForm.invalid) return;
    const userRequest = this.userForm.value as User;
    this.userService.updateUser(userRequest).subscribe({
      next: () => {
        this.refresh$.next();
        this.onSuccess = true;
      },
      error: () => {
        this.onError = true;
      },
    });
  }
  ngOnDestroy(): void {
    this.destroy$.next(true);
  }
}
