import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';

@Component({
  selector: 'app-proposta-detalhe',
  standalone: true,
  imports: [CommonModule, RouterModule], 
  templateUrl: './proposta-detalhe.html',
  styleUrl: './proposta-detalhe.css'
})
export class PropostaDetalheComponent implements OnInit {
  //objeto vazio para iniciar a tela
  proposta = {
    id: 0,
    titulo: 'Carregando...',
    descricao: 'Carregando...',
    categoria: 'Carregando...'
  };

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    //aqui pega a id da proposta que está na url, por exemplo: /propostas/1, pega o 1
    const idDaUrl = this.route.snapshot.paramMap.get('id');

    //simula uma busca no banco de dados
    if (idDaUrl === '1') {
      this.proposta = { id: 1, titulo: 'Melhoria no Wi-Fi da Biblioteca', descricao: 'Instalar novos roteadores para suportar mais alunos conectados simultaneamente.', categoria: 'Infraestrutura' };
    } else if (idDaUrl === '2') {
      this.proposta = { id: 2, titulo: 'Criar Laboratório Maker', descricao: 'Disponibilizar impressoras 3D e kits de robótica para os alunos.', categoria: 'Inovação e Tecnologia' };
    } else {
      this.proposta = { id: 999, titulo: 'Proposta não encontrada', descricao: 'Não foi possível carregar os detalhes.', categoria: 'Erro' };
    }
  }
}