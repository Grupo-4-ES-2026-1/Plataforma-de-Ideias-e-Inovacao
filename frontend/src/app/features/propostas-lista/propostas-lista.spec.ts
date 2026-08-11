import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PropostasLista } from './propostas-lista';

describe('PropostasLista', () => {
  let component: PropostasLista;
  let fixture: ComponentFixture<PropostasLista>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PropostasLista],
    }).compileComponents();

    fixture = TestBed.createComponent(PropostasLista);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
