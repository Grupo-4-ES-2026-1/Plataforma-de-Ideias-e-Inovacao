import { ComponentFixture, TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { RankingComponent } from './ranking';

describe('RankingComponent', () => {
  let component: RankingComponent;
  let fixture: ComponentFixture<RankingComponent>;
  let httpMock: HttpTestingController;

  const apiUrl =
    'https://plataforma-de-ideias-e-inovacao.onrender.com/propostas';

  const rankingUrl = `${apiUrl}/ranking`;

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

  function responderCategorias(): void {
    const req = httpMock.expectOne(
      request => request.url === apiUrl
    );

    expect(req.request.method).toBe('GET');

    req.flush([
      {
        id: 1,
        titulo: 'Proposta Tecnologia',
        descricao: 'Descrição',
        categoria: 'TECNOLOGIA',
        status: 'SUBMETIDA',
        numeroDeVotos: 5,
      },
      {
        id: 2,
        titulo: 'Proposta Saúde',
        descricao: 'Descrição',
        categoria: 'SAUDE',
        status: 'SUBMETIDA',
        numeroDeVotos: 8,
      },
    ]);
  }

  it('should create', () => {
    fixture.detectChanges();

    responderCategorias();

    const reqRanking = httpMock.expectOne(
      request => request.url === rankingUrl
    );

    reqRanking.flush([]);

    expect(component).toBeTruthy();
  });

  it('should load ranking preserving backend order', () => {
    fixture.detectChanges();

    responderCategorias();

    const reqRanking = httpMock.expectOne(
      request => request.url === rankingUrl
    );

    expect(reqRanking.request.method).toBe('GET');

    reqRanking.flush([
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
      {
        id: 1,
        titulo: 'Proposta A',
        descricao: 'Descrição A',
        categoria: 'TECNOLOGIA',
        status: 'SUBMETIDA',
        numeroDeVotos: 3,
      },
    ]);

    const ranking = component.propostas();

    expect(ranking.length).toBe(3);
    expect(ranking[0].id).toBe(2);
    expect(ranking[1].id).toBe(3);
    expect(ranking[2].id).toBe(1);
    expect(component.carregando()).toBe(false);
  });

  it('should send categoria to ranking endpoint', () => {
    fixture.detectChanges();

    responderCategorias();

    const reqInicial = httpMock.expectOne(
      request => request.url === rankingUrl
    );

    reqInicial.flush([]);

    component.filtroCategoria = 'TECNOLOGIA';
    component.aplicarFiltros();

    const reqFiltro = httpMock.expectOne(
      request => request.url === rankingUrl
    );

    expect(reqFiltro.request.params.get('categoria'))
      .toBe('TECNOLOGIA');

    reqFiltro.flush([
      {
        id: 1,
        titulo: 'Proposta Tecnologia',
        descricao: 'Descrição',
        categoria: 'TECNOLOGIA',
        status: 'SUBMETIDA',
        numeroDeVotos: 5,
      },
    ]);

    expect(component.propostas().length).toBe(1);
    expect(component.propostas()[0].categoria)
      .toBe('TECNOLOGIA');
  });

  it('should send dataInicial to ranking endpoint', () => {
    fixture.detectChanges();

    responderCategorias();

    const reqInicial = httpMock.expectOne(
      request => request.url === rankingUrl
    );

    reqInicial.flush([]);

    component.dataInicial = '2026-09-01';
    component.aplicarFiltros();

    const reqFiltro = httpMock.expectOne(
      request => request.url === rankingUrl
    );

    expect(reqFiltro.request.params.get('dataInicial'))
      .toBe('2026-09-01T00:00:00');

    reqFiltro.flush([
      {
        id: 2,
        titulo: 'Proposta Nova',
        descricao: 'Descrição',
        categoria: 'TECNOLOGIA',
        status: 'SUBMETIDA',
        numeroDeVotos: 8,
        dataCriacao: '2026-09-01T10:00:00',
      },
    ]);

    expect(component.propostas().length).toBe(1);
    expect(component.propostas()[0].id).toBe(2);
  });

  it('should send dataFinal to ranking endpoint', () => {
    fixture.detectChanges();

    responderCategorias();

    const reqInicial = httpMock.expectOne(
      request => request.url === rankingUrl
    );

    reqInicial.flush([]);

    component.dataFinal = '2026-08-31';
    component.aplicarFiltros();

    const reqFiltro = httpMock.expectOne(
      request => request.url === rankingUrl
    );

    expect(reqFiltro.request.params.get('dataFinal'))
      .toBe('2026-08-31T23:59:59');

    reqFiltro.flush([
      {
        id: 1,
        titulo: 'Proposta Antiga',
        descricao: 'Descrição',
        categoria: 'TECNOLOGIA',
        status: 'SUBMETIDA',
        numeroDeVotos: 5,
        dataCriacao: '2026-08-01T10:00:00',
      },
    ]);

    expect(component.propostas().length).toBe(1);
    expect(component.propostas()[0].id).toBe(1);
  });

  it('should send all filters together', () => {
    fixture.detectChanges();

    responderCategorias();

    const reqInicial = httpMock.expectOne(
      request => request.url === rankingUrl
    );

    reqInicial.flush([]);

    component.filtroCategoria = 'TECNOLOGIA';
    component.dataInicial = '2026-09-01';
    component.dataFinal = '2026-09-30';

    component.aplicarFiltros();

    const reqFiltro = httpMock.expectOne(
      request => request.url === rankingUrl
    );

    expect(reqFiltro.request.params.get('categoria'))
      .toBe('TECNOLOGIA');

    expect(reqFiltro.request.params.get('dataInicial'))
      .toBe('2026-09-01T00:00:00');

    expect(reqFiltro.request.params.get('dataFinal'))
      .toBe('2026-09-30T23:59:59');

    reqFiltro.flush([]);
  });

  it('should set erro when ranking request fails', () => {
    fixture.detectChanges();

    responderCategorias();

    const reqRanking = httpMock.expectOne(
      request => request.url === rankingUrl
    );

    reqRanking.error(
      new ProgressEvent('erro de rede')
    );

    expect(component.erro()).toBeTruthy();
    expect(component.carregando()).toBe(false);
  });
});