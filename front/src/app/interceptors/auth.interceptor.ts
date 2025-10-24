import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { environment } from '../../environments/environment';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private auth: AuthService) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const token = this.auth.getToken();
    const isPublic = this.isPublicRoute(req.url);
    const isApiCall = req.url.startsWith(environment.apiUrl);

    if (!token || isPublic || !isApiCall || req.headers.has('Authorization')) {
      return next.handle(req);
    }
    const authReq = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` },
    });

    return next.handle(authReq).pipe(
      catchError((err) => {
        if (err.status === 401) {
          this.auth.logout();
        }
        return throwError(() => err);
      })
    );
  }

  private isPublicRoute(url: string): boolean {
    return url.endsWith('/auth/login') || url.endsWith('/auth/register');
  }
}
