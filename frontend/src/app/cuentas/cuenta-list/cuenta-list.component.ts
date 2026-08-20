import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Cuenta } from '../../models/cuenta.model';
import { CuentaService } from '../../services/cuenta.service';

@Component({
  selector: 'app-cuenta-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './cuenta-list.component.html',
  styleUrl: './cuenta-list.component.css'
})
export class CuentaListComponent implements OnInit {
  cuentas: Cuenta[] = [];
  cargando = false;
  error = '';
  clienteId: number | null = null;

  constructor(private cuentaService: CuentaService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.queryParamMap.subscribe((params) => {
      const clienteId = params.get('clienteId');
      this.clienteId = clienteId ? Number(clienteId) : null;
      this.cargar();
    });
  }

  cargar(): void {
    this.cargando = true;
    this.error = '';
    const peticion = this.clienteId
      ? this.cuentaService.obtenerPorCliente(this.clienteId)
      : this.cuentaService.listar();

    peticion.subscribe({
      next: (data) => {
        this.cuentas = data;
        this.cargando = false;
      },
      error: (err) => {
        this.error = err.error?.mensaje || 'No se pudo cargar la lista de cuentas.';
        this.cargando = false;
      }
    });
  }

  limpiarFiltro(): void {
    this.clienteId = null;
    this.cargar();
  }

  activar(cuenta: Cuenta): void {
    if (!cuenta.id) return;
    this.cuentaService.activar(cuenta.id).subscribe({
      next: () => this.cargar(),
      error: (err) => (this.error = err.error?.mensaje || 'No se pudo activar la cuenta.')
    });
  }

  inactivar(cuenta: Cuenta): void {
    if (!cuenta.id) return;
    this.cuentaService.inactivar(cuenta.id).subscribe({
      next: () => this.cargar(),
      error: (err) => (this.error = err.error?.mensaje || 'No se pudo inactivar la cuenta.')
    });
  }

  cancelar(cuenta: Cuenta): void {
    if (!cuenta.id) return;
    const confirmado = confirm(`¿Cancelar la cuenta ${cuenta.numeroCuenta}? Esta acción no se puede deshacer.`);
    if (!confirmado) return;
    this.cuentaService.cancelar(cuenta.id).subscribe({
      next: () => this.cargar(),
      error: (err) => (this.error = err.error?.mensaje || 'No se pudo cancelar la cuenta.')
    });
  }
}
