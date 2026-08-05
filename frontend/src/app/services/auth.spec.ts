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
    
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should login and store token', () => {
    const token = 'header.eyJzdWIiOiJ1c3VhcmlvQGV4YW1wbGUuY29tIiwicm9sZSI6IlVTRVIifQ.signature';

    service.login('user@example.com', 'senha').subscribe((resultado) => {
      expect(resultado).toBe(true);
      expect(service.autenticado()).toBe(true);
      expect(service.usuarioLogado()).toBe('usuario@example.com');
      expect(service.roleAtual()).toBe('USER');
    });

    const req = httpMock.expectOne('/auth/login');
    expect(req.request.method).toBe('POST');
    req.flush({ token });
  });
});
