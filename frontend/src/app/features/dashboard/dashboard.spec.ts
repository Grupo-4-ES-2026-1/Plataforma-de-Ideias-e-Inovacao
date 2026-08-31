import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';
import { DashboardComponent } from './dashboard';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
  let httpMock: HttpTestingController;

  const apiUrl = 'https://plataforma-de-ideias-e-inovacao.onrender.com/propostas';

  // Dados fictícios simulando o que viria do backend
  const mockPropostas = [
    { id: 1, titulo: 'Nova Rede', categoria: 'Infraestrutura', status: 'SUBMETIDA', dataCriacao: new Date().toISOString(), numeroDeVotos: 10 },
    { id: 2, titulo: 'App Estudantes', categoria: 'Tecnologia', status: 'APROVADA', dataCriacao: new Date(new Date().setDate(new Date().getDate() - 20)).toISOString(), numeroDeVotos: 20 },
    { id: 3, titulo: 'Nova Biblioteca', categoria: 'Infraestrutura', status: 'REJEITADA', dataCriacao: '2025-10-15T10:00:00', numeroDeVotos: 5 }
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardComponent, HttpClientTestingModule, FormsModule]
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('deve carregar as métricas iniciais e extrair categorias únicas', () => {
    fixture.detectChanges(); // Dispara o ngOnInit

    const req = httpMock.expectOne(req => req.url === apiUrl);
    expect(req.request.method).toBe('GET');
    req.flush(mockPropostas);

    expect(component.carregando()).toBe(false);
    expect(component.totalPropostas()).toBe(3); 
    expect(component.totalVotos()).toBe(35); 
    expect(component.taxaAprovacao()).toBe(33); 
    expect(component.categoriasDisponiveis()).toEqual(['Infraestrutura', 'Tecnologia']); 
  });

  it('deve recalcular as métricas ao filtrar por Categoria', () => {
    fixture.detectChanges();
    httpMock.expectOne(req => req.url === apiUrl).flush(mockPropostas);

    component.filtroCategoria = 'Tecnologia';
    component.aplicarFiltros();

    expect(component.totalPropostas()).toBe(1); 
    expect(component.totalVotos()).toBe(20);
    expect(component.taxaAprovacao()).toBe(100); 
  });

  it('deve recalcular as métricas ao filtrar por Período (Últimos 7 dias)', () => {
    fixture.detectChanges();
    httpMock.expectOne(req => req.url === apiUrl).flush(mockPropostas);

    component.filtroPeriodo = '7dias';
    component.aplicarFiltros();

    expect(component.totalPropostas()).toBe(1); 
    expect(component.distribuicao().submetida).toBe(1);
    expect(component.distribuicao().aprovada).toBe(0);
  });

  it('deve exibir mensagem de erro caso o backend falhe', () => {
    fixture.detectChanges();
    const req = httpMock.expectOne(req => req.url === apiUrl);
    req.error(new ProgressEvent('Erro de conexão'));

    expect(component.erro()).toBeTruthy();
    expect(component.carregando()).toBe(false);
  });
});