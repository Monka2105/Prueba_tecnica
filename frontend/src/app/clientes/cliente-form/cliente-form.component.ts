import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ClienteService } from '../../services/cliente.service';

@Component({
  selector: 'app-cliente-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './cliente-form.component.html',
  styleUrl: './cliente-form.component.css'
})
export class ClienteFormComponent implements OnInit {
  private fb = inject(FormBuilder);

  form = this.fb.group({
    tipoDocumento: ['CC', Validators.required],
    numeroDocumento: ['', Validators.required],
    nombres: ['', [Validators.required, Validators.minLength(2)]],
    apellidos: ['', [Validators.required, Validators.minLength(2)]],
    email: ['', [Validators.required, Validators.email]],
    fechaNacimiento: ['', Validators.required]
  });

  id: number | null = null;
  cargando = false;
  guardando = false;
  error = '';

  tiposDocumento = ['CC', 'CE', 'TI', 'PA', 'NIT'];

  constructor(
    private clienteService: ClienteService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  get editando(): boolean {
    return this.id !== null;
  }

  ngOnInit(): void {
    const paramId = this.route.snapshot.paramMap.get('id');
    if (paramId) {
      this.id = Number(paramId);
      this.cargando = true;
      this.clienteService.obtener(this.id).subscribe({
        next: (cliente) => {
          this.form.patchValue({
            tipoDocumento: cliente.tipoDocumento,
            numeroDocumento: cliente.numeroDocumento,
            nombres: cliente.nombres,
            apellidos: cliente.apellidos,
            email: cliente.email,
            fechaNacimiento: cliente.fechaNacimiento
          });
          this.cargando = false;
        },
        error: (err) => {
          this.error = err.error?.mensaje || 'No se pudo cargar el cliente.';
          this.cargando = false;
        }
      });
    }
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.guardando = true;
    this.error = '';
    const valor = this.form.getRawValue();
    const payload = {
      tipoDocumento: valor.tipoDocumento!,
      numeroDocumento: valor.numeroDocumento!,
      nombres: valor.nombres!,
      apellidos: valor.apellidos!,
      email: valor.email!,
      fechaNacimiento: valor.fechaNacimiento!
    };

    const peticion = this.editando
      ? this.clienteService.actualizar(this.id!, payload)
      : this.clienteService.crear(payload);

    peticion.subscribe({
      next: () => this.router.navigate(['/clientes']),
      error: (err) => {
        this.error = err.error?.mensaje || 'No se pudo guardar el cliente.';
        this.guardando = false;
      }
    });
  }
}
