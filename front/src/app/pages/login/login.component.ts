import { Component, OnInit } from '@angular/core';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { LoginRequest } from '../../interfaces/loginRequest.interface';
import { Session } from '../../interfaces/session.interface';
import { AuthService } from '../../services/auth.service';
import { HeaderComponent } from '../../components/header/header.component';
import { HeaderMiniComponent } from '../../components/header-mini/header-mini.component';
import { BackArrowComponent } from '../../components/back-arrow/back-arrow.component';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-login',
  imports: [
    ReactiveFormsModule,
    CommonModule,
    HeaderComponent,
    HeaderMiniComponent,
    FaIconComponent,
    BackArrowComponent,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  faEye = faEye;
  faEyeSlash = faEyeSlash;
  loginForm!: FormGroup;
  onError = false;
  hidePassword!: Boolean;
  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.hidePassword = true;
    this.loginForm = this.formBuilder.group(
      {
        login: [null, [Validators.required]],

        password: [null, [Validators.required]],
      },
      { updateOn: 'blur' }
    );
  }

  onLogin(): void {
    if (this.loginForm.invalid) return;
    const loginRequest = this.loginForm.value as LoginRequest;
    this.authService.login(loginRequest).subscribe({
      next: (session: Session) => {
        this.authService.saveToken(session);
        this.router.navigate(['/posts']);
      },
      error: (_) => (this.onError = true),
    });
  }
}
