import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../interfaces/user.interface';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private pathService = environment.apiUrl;

  constructor(private httpClient: HttpClient) {}

  public getUser(): Observable<User> {
    return this.httpClient.get<User>(`${this.pathService}/user/me`);
  }

  public updateUser(updatedUser: User): Observable<void> {
    return this.httpClient.patch<void>(
      `${this.pathService}/user/me`,
      updatedUser
    );
  }
}
