import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Transaccion } from '../../models/transaccion.model';
import { TransaccionService } from '../../services/transaccion.service';

@Component({
  selector: 'app-transaccion-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './transaccion-list.component.html',
  styleUrl: './transaccion-list.component.css'
})
export class TransaccionListComponent implements OnInit {
  transacciones: Transaccion[] = [];
  cargando = false;
  error = '';
  cuentaId: number | null = null;

  constructor(private transaccionService: TransaccionService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.queryParamMap.subscribe((params) => {
      const cuentaId = params.get('cuentaId');
      this.cuentaId = cuentaId ? Number(cuentaId) : null;
      this.cargar();
    });
  }

  cargar(): void {
    this.cargando = true;
    this.error = '';
    const peticion = this.cuentaId
      ? this.transaccionService.obtenerPorCuenta(this.cuentaId)
      : this.transaccionService.listar();

    peticion.subscribe({
      next: (data) => {
        this.transacciones = data;
        this.cargando = false;
      },
      error: (err) => {
        this.error = err.error?.mensaje || 'No se pudo cargar la lista de transacciones.';
        this.cargando = false;
      }
    });
  }

  limpiarFiltro(): void {
    this.cuentaId = null;
    this.cargar();
  }
}
