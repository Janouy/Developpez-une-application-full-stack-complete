import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Subject } from '../interfaces/subject.interface';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class SubjectService {
  private pathService = environment.apiUrl;

  constructor(private httpClient: HttpClient) {}

  public getSubjects(): Observable<Subject[]> {
    return this.httpClient.get<Subject[]>(`${this.pathService}/subjects`);
  }
}
