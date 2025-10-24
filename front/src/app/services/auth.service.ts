import { HttpClient } from '@angular/common/http';
import { Injectable, signal, computed } from '@angular/core';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { Session } from '../interfaces/session.interface';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private pathService = environment.apiUrl;
  public session: Session | undefined;

  private _token = signal<string | null>(this.getToken());

  isLoggedIn = computed(() => !!this._token());

  constructor(private httpClient: HttpClient, private router: Router) {}

  public getToken(): string | null {
    return localStorage.getItem('token');
  }

  public register(registerRequest: RegisterRequest): Observable<void> {
    return this.httpClient.post<void>(
      `${this.pathService}/auth/register`,
      registerRequest
    );
  }

  public login(loginRequest: LoginRequest): Observable<Session> {
    return this.httpClient.post<Session>(
      `${this.pathService}/auth/login`,
      loginRequest
    );
  }
  public logout(): void {
    localStorage.removeItem('token');
    this._token.set(null);
    this.router.navigate(['']);
  }

  public saveToken(session: Session): void {
    localStorage.setItem('token', session.token);
    this._token.set(session.token);
  }
}
