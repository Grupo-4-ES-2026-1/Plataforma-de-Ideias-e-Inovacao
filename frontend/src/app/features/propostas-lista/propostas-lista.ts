import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

//inteface para definir a estrutura de uma proposta
export interface Proposta {
  id: number;
  titulo: string;
  descricao: string;
  categoria: string;
}

@Component({
  selector: 'app-propostas-lista',
  standalone: true,
  imports: [CommonModule, RouterModule], //routermodule é necessário para usar o routerLink no template
  templateUrl: './propostas-lista.component.html',
  styleUrl: './propostas-lista.component.css' //css
})
export class PropostasListaComponent {
  //falsa lista para simular propostas, no futuro será substituída por uma chamada a um serviço que buscará as propostas do backend
  propostas: Proposta[] = [
    { 
      id: 1, 
      titulo: 'Melhoria no Wi-Fi da Biblioteca', 
      descricao: 'Instalar novos roteadores para suportar mais alunos conectados simultaneamente.', 
      categoria: 'Infraestrutura' 
    },
    { 
      id: 2, 
      titulo: 'Criar Laboratório Maker', 
      descricao: 'Disponibilizar impressoras 3D e kits de robótica para os alunos.', 
      categoria: 'Inovação e Tecnologia' 
    }
  ];
}