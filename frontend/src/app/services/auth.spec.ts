import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { AuthService } from './auth';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.removeItem('jwt_token');
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should login and store token', () => {
    const token = 'header.eyJzdWIiOiJ1c3VhcmlvQGV4YW1wbGUuY29tIiwicm9sZSI6IlVTRVIifQ.signature';

    service.login('usuario@example.com', 'senha').subscribe((resultado) => {
      expect(resultado).toBe(true);
      expect(service.autenticado()).toBe(true);
      expect(service.usuarioLogado()).toBe('usuario@example.com');
      expect(service.roleAtual()).toBe('USER');
    });

    const req = httpMock.expectOne('http://localhost:8080/auth/login');
    expect(req.request.method).toBe('POST');
    req.flush({ token });
  });

  it('should return false on login failure', () => {
    service.login('usuario@example.com', 'senha-errada').subscribe((resultado) => {
      expect(resultado).toBe(false);
      expect(service.autenticado()).toBe(false);
    });

    const req = httpMock.expectOne('http://localhost:8080/auth/login');
    req.flush({ message: 'credenciais invalidas' }, { status: 401, statusText: 'Unauthorized' });
  });

  it('should clear local data on logout', () => {
    localStorage.setItem('jwt_token', 'algum-token');

    service.logout().subscribe();

    const req = httpMock.expectOne('http://localhost:8080/auth/logout');
    req.flush({});

    expect(service.autenticado()).toBe(false);
    expect(localStorage.getItem('jwt_token')).toBeNull();
  });
});