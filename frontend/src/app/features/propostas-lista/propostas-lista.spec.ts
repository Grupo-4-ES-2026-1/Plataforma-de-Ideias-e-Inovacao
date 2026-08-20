import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { PropostasListaComponent } from './propostas-lista';

describe('PropostasListaComponent', () => {
  let component: PropostasListaComponent;
  let fixture: ComponentFixture<PropostasListaComponent>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PropostasListaComponent, HttpClientTestingModule],
      providers: [provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(PropostasListaComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    fixture.detectChanges();
    const req = httpMock.expectOne('https://plataforma-de-ideias-e-inovacao.onrender.com/propostas');
    req.flush([]);
    expect(component).toBeTruthy();
  });

  it('should load propostas from backend on init', () => {
    fixture.detectChanges();

    const req = httpMock.expectOne('https://plataforma-de-ideias-e-inovacao.onrender.com/propostas');
    expect(req.request.method).toBe('GET');

    req.flush([
      { id: 1, titulo: 'Proposta teste', descricao: 'desc', categoria: 'TECNOLOGIA', status: 'SUBMETIDA' },
    ]);

    expect(component.propostas().length).toBe(1);
    expect(component.carregando()).toBe(false);
  });

  it('should set erro when backend request fails', () => {
    fixture.detectChanges();

    const req = httpMock.expectOne('https://plataforma-de-ideias-e-inovacao.onrender.com/propostas');
    req.error(new ProgressEvent('erro de rede'));

    expect(component.erro()).toBeTruthy();
    expect(component.carregando()).toBe(false);
  });
});