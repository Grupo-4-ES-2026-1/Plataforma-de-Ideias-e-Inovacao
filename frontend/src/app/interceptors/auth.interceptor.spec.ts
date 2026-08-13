import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { vi } from 'vitest';

import { authInterceptor } from './auth.interceptor';
import { AuthService } from '../services/auth';

describe('authInterceptor', () => {
  let http: HttpClient;
  let httpMock: HttpTestingController;
  let authService: AuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting(),
      ],
    });

    http = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
    authService = TestBed.inject(AuthService);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should add Authorization header when token exists', () => {
    vi.spyOn(authService, 'recuperarToken').mockReturnValue('token123');

    http.get('http://localhost:8080/propostas').subscribe();

    const req = httpMock.expectOne('http://localhost:8080/propostas');
    expect(req.request.headers.get('Authorization')).toBe('Bearer token123');
    req.flush({});
  });

  it('should not add Authorization header for login request', () => {
    vi.spyOn(authService, 'recuperarToken').mockReturnValue('token123');

    http.post('http://localhost:8080/auth/login', {}).subscribe();

    const req = httpMock.expectOne('http://localhost:8080/auth/login');
    expect(req.request.headers.has('Authorization')).toBe(false);
    req.flush({});
  });

  it('should not add Authorization header when no token exists', () => {
    vi.spyOn(authService, 'recuperarToken').mockReturnValue(null);

    http.get('http://localhost:8080/propostas').subscribe();

    const req = httpMock.expectOne('http://localhost:8080/propostas');
    expect(req.request.headers.has('Authorization')).toBe(false);
    req.flush({});
  });
});