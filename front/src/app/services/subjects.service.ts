import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SubjectInterface } from '../interfaces/subjectInterface.interface';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class SubjectService {
  private pathService = environment.apiUrl;

  constructor(private httpClient: HttpClient) {}

  public getSubjects(): Observable<SubjectInterface[]> {
    return this.httpClient.get<SubjectInterface[]>(
      `${this.pathService}/subjects`
    );
  }

  public subjectSubscribe(subjectId: number): Observable<void> {
    return this.httpClient.post<void>(
      `${this.pathService}/subjects/${subjectId}/subscribe`,
      null
    );
  }

  public subjectUnsubscribe(subjectId: number): Observable<void> {
    return this.httpClient.delete<void>(
      `${this.pathService}/subjects/${subjectId}/subscribe`
    );
  }
}
