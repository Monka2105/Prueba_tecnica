import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { TransaccionService } from '../../services/transaccion.service';
import { CuentaService } from '../../services/cuenta.service';
import { Cuenta } from '../../models/cuenta.model';

@Component({
  selector: 'app-transaccion-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './transaccion-form.component.html',
  styleUrl: './transaccion-form.component.css'
})
export class TransaccionFormComponent implements OnInit {
  private fb = inject(FormBuilder);

  form = this.fb.group({
    cuentaId: [null as number | null, Validators.required],
    tipoTransaccion: ['CONSIGNACION', Validators.required],
    cuentaRelacionadaId: [null as number | null],
    monto: [null as number | null, [Validators.required, Validators.min(0.01)]],
    descripcion: ['']
  });

  cuentas: Cuenta[] = [];
  guardando = false;
  error = '';

  constructor(
    private transaccionService: TransaccionService,
    private cuentaService: CuentaService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cuentaService.listar().subscribe({
      next: (data) => (this.cuentas = data),
      error: (err) => (this.error = err.error?.mensaje || 'No se pudo cargar la lista de cuentas.')
    });
  }

  get esTransferencia(): boolean {
    return this.form.get('tipoTransaccion')?.value === 'TRANSFERENCIA';
  }

  guardar(): void {
    if (this.esTransferencia) {
      this.form.get('cuentaRelacionadaId')?.addValidators(Validators.required);
    } else {
      this.form.get('cuentaRelacionadaId')?.clearValidators();
    }
    this.form.get('cuentaRelacionadaId')?.updateValueAndValidity();

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.guardando = true;
    this.error = '';
    const valor = this.form.getRawValue();

    this.transaccionService
      .registrar({
        cuentaId: Number(valor.cuentaId),
        tipoTransaccion: valor.tipoTransaccion as any,
        cuentaRelacionadaId: valor.cuentaRelacionadaId ? Number(valor.cuentaRelacionadaId) : null,
        monto: Number(valor.monto),
        descripcion: valor.descripcion || ''
      })
      .subscribe({
        next: () => this.router.navigate(['/transacciones']),
        error: (err) => {
          this.error = err.error?.mensaje || 'No se pudo registrar la transacción.';
          this.guardando = false;
        }
      });
  }
}
