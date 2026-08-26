import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { PropostasListaComponent } from './propostas-lista';

describe('PropostasListaComponent', () => {
  let component: PropostasListaComponent;
  let fixture: ComponentFixture<PropostasListaComponent>;
  let httpMock: HttpTestingController;

  //a URL base que queremos monitorar nos testes
  const apiUrl = 'https://plataforma-de-ideias-e-inovacao.onrender.com/propostas';

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      //adicionado o FormsModule nos imports do teste
      imports: [PropostasListaComponent, HttpClientTestingModule, FormsModule],
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
    //usamos uma função para ignorar os parâmetros da URL (ex: ?sort=recentes)
    const req = httpMock.expectOne(req => req.url === apiUrl);
    req.flush([]);
    expect(component).toBeTruthy();
  });

  it('should load propostas from backend on init', () => {
    fixture.detectChanges();

    const req = httpMock.expectOne(req => req.url === apiUrl);
    expect(req.request.method).toBe('GET');

    req.flush([
      { id: 1, titulo: 'Proposta teste', descricao: 'desc', categoria: 'TECNOLOGIA', status: 'SUBMETIDA' },
    ]);

    expect(component.propostas().length).toBe(1);
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