import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { Session } from '../interfaces/session.interface';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private token!: string;
  private pathService = environment.apiUrl;
  public isLogged = false;
  public session: Session | undefined;

  constructor(private httpClient: HttpClient) {}

  public getToken(): string {
    return this.token;
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

  public saveToken(session: Session): void {
    localStorage.setItem('token', session.token);
    this.isLogged = true;
  }
}
