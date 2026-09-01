import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { RankingComponent } from './ranking';

describe('RankingComponent', () => {
  let component: RankingComponent;
  let fixture: ComponentFixture<RankingComponent>;
  let httpMock: HttpTestingController;

  const apiUrl =
    'https://plataforma-de-ideias-e-inovacao.onrender.com/propostas';

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RankingComponent, HttpClientTestingModule],
      providers: [provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(RankingComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    fixture.detectChanges();

    const req = httpMock.expectOne(req => req.url === apiUrl);
    req.flush([]);

    expect(component).toBeTruthy();
  });

  it('should load propostas and order by numeroDeVotos descending', () => {
    fixture.detectChanges();

    const req = httpMock.expectOne(req => req.url === apiUrl);

    expect(req.request.method).toBe('GET');

    req.flush([
      {
        id: 1,
        titulo: 'Proposta A',
        descricao: 'Descrição A',
        categoria: 'TECNOLOGIA',
        status: 'SUBMETIDA',
        numeroDeVotos: 3,
      },
      {
        id: 2,
        titulo: 'Proposta B',
        descricao: 'Descrição B',
        categoria: 'EDUCACAO',
        status: 'SUBMETIDA',
        numeroDeVotos: 10,
      },
      {
        id: 3,
        titulo: 'Proposta C',
        descricao: 'Descrição C',
        categoria: 'SAUDE',
        status: 'SUBMETIDA',
        numeroDeVotos: 6,
      },
    ]);

    const ranking = component.propostas();

    expect(ranking.length).toBe(3);
    expect(ranking[0].id).toBe(2);
    expect(ranking[1].id).toBe(3);
    expect(ranking[2].id).toBe(1);
    expect(component.carregando()).toBe(false);
  });

  it('should set erro when backend request fails', () => {
    fixture.detectChanges();

    const req = httpMock.expectOne(req => req.url === apiUrl);

    req.error(new ProgressEvent('erro de rede'));

    expect(component.erro()).toBeTruthy();
    expect(component.carregando()).toBe(false);
  });
});