import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CuentaService } from '../../services/cuenta.service';
import { ClienteService } from '../../services/cliente.service';
import { Cliente } from '../../models/cliente.model';

@Component({
  selector: 'app-cuenta-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './cuenta-form.component.html',
  styleUrl: './cuenta-form.component.css'
})
export class CuentaFormComponent implements OnInit {
  private fb = inject(FormBuilder);

  form = this.fb.group({
    tipoCuenta: ['CUENTA_AHORROS', Validators.required],
    saldo: [0, [Validators.required, Validators.min(0)]],
    clienteId: [null as number | null, Validators.required]
  });

  clientes: Cliente[] = [];
  guardando = false;
  error = '';

  constructor(
    private cuentaService: CuentaService,
    private clienteService: ClienteService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.clienteService.listar().subscribe({
      next: (data) => (this.clientes = data),
      error: (err) => (this.error = err.error?.mensaje || 'No se pudo cargar la lista de clientes.')
    });
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.guardando = true;
    this.error = '';
    const valor = this.form.getRawValue();

    this.cuentaService
      .crear({
        tipoCuenta: valor.tipoCuenta as any,
        saldo: Number(valor.saldo),
        clienteId: Number(valor.clienteId)
      })
      .subscribe({
        next: () => this.router.navigate(['/cuentas']),
        error: (err) => {
          this.error = err.error?.mensaje || 'No se pudo crear la cuenta.';
          this.guardando = false;
        }
      });
  }
}
