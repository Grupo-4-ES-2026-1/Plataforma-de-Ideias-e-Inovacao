import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';

import { AuthInterceptor } from './auth.interceptor';
import { AUTH_TOKEN_KEY } from '../services/auth';

describe('AuthInterceptor', () => {
  let http: HttpClient;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        {
          provide: HTTP_INTERCEPTORS,
          useClass: AuthInterceptor,
          multi: true,
        },
      ],
    });

    http = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    localStorage.removeItem(AUTH_TOKEN_KEY);
    httpMock.verify();
  });

  it('should add Authorization header when token exists and request is not login', () => {
    localStorage.setItem(AUTH_TOKEN_KEY, 'token123');

    http.get('/api/protected').subscribe();

    const req = httpMock.expectOne('/api/protected');
    expect(req.request.headers.get('Authorization')).toBe('Bearer token123');
    req.flush({});
  });

  it('should not add Authorization header for login request', () => {
    localStorage.setItem(AUTH_TOKEN_KEY, 'token123');

    http.post('/auth/login', {}).subscribe();

    const req = httpMock.expectOne('/auth/login');
    expect(req.request.headers.has('Authorization')).toBe(false);
    req.flush({});
  });

  it('should skip header when Authorization already exists', () => {
    localStorage.setItem(AUTH_TOKEN_KEY, 'token123');

    http.get('/api/protected', {
      headers: { Authorization: 'Bearer existing' },
    }).subscribe();

    const req = httpMock.expectOne('/api/protected');
    expect(req.request.headers.get('Authorization')).toBe('Bearer existing');
    req.flush({});
  });
});
