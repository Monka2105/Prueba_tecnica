import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Cliente } from '../../models/cliente.model';
import { ClienteService } from '../../services/cliente.service';

@Component({
  selector: 'app-cliente-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './cliente-list.component.html',
  styleUrl: './cliente-list.component.css'
})
export class ClienteListComponent implements OnInit {
  clientes: Cliente[] = [];
  cargando = false;
  error = '';

  constructor(private clienteService: ClienteService) {}

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.cargando = true;
    this.error = '';
    this.clienteService.listar().subscribe({
      next: (data) => {
        this.clientes = data;
        this.cargando = false;
      },
      error: (err) => {
        this.error = err.error?.mensaje || 'No se pudo cargar la lista de clientes.';
        this.cargando = false;
      }
    });
  }

  eliminar(cliente: Cliente): void {
    if (!cliente.id) {
      return;
    }
    const confirmado = confirm(`¿Eliminar al cliente ${cliente.nombres} ${cliente.apellidos}?`);
    if (!confirmado) {
      return;
    }
    this.clienteService.eliminar(cliente.id).subscribe({
      next: () => this.cargar(),
      error: (err) => {
        this.error = err.error?.mensaje || 'No se pudo eliminar el cliente.';
      }
    });
  }
}
