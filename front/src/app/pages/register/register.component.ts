import { Component, OnInit } from '@angular/core';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';
import { AuthService } from '../../services/auth.service';
import { HeaderComponent } from '../../components/header/header.component';
import { HeaderMiniComponent } from '../../components/header-mini/header-mini.component';
import { BackArrowComponent } from '../../components/back-arrow/back-arrow.component';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-register',
  imports: [
    ReactiveFormsModule,
    CommonModule,
    HeaderComponent,
    HeaderMiniComponent,
    FaIconComponent,
    BackArrowComponent,
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit {
  faEye = faEye;
  faEyeSlash = faEyeSlash;
  registerForm!: FormGroup;
  onError = false;
  passwordRegex!: RegExp;
  hidePassword!: Boolean;
  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.passwordRegex =
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&._\-])[A-Za-z\d@$!%*?&._\-]{8,}$/;
    this.hidePassword = true;
    this.registerForm = this.formBuilder.group(
      {
        name: [null, [Validators.required, Validators.minLength(4)]],
        email: [null, [Validators.required, Validators.email]],
        password: [
          null,
          [Validators.required, Validators.pattern(this.passwordRegex)],
        ],
      },
      { updateOn: 'blur' }
    );
  }

  onRegister(): void {
    if (this.registerForm.invalid) return;

    const registerRequest = this.registerForm.value as RegisterRequest;

    this.authService.register(registerRequest).subscribe({
      next: () => {
        const login = this.registerForm.value.email;
        const password = this.registerForm.value.password;
        this.authService.login({ login, password }).subscribe({
          next: (response) => {
            this.authService.saveToken(response);
            this.router.navigate(['/posts']);
          },
          error: () => (this.onError = true),
        });
      },
      error: () => (this.onError = true),
    });
  }
}
