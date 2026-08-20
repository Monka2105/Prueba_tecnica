import { Routes } from '@angular/router';
import { ClienteListComponent } from './clientes/cliente-list/cliente-list.component';
import { ClienteFormComponent } from './clientes/cliente-form/cliente-form.component';
import { CuentaListComponent } from './cuentas/cuenta-list/cuenta-list.component';
import { CuentaFormComponent } from './cuentas/cuenta-form/cuenta-form.component';
import { TransaccionListComponent } from './transacciones/transaccion-list/transaccion-list.component';
import { TransaccionFormComponent } from './transacciones/transaccion-form/transaccion-form.component';

export const routes: Routes = [
  { path: '', redirectTo: 'clientes', pathMatch: 'full' },

  { path: 'clientes', component: ClienteListComponent },
  { path: 'clientes/nuevo', component: ClienteFormComponent },
  { path: 'clientes/:id/editar', component: ClienteFormComponent },

  { path: 'cuentas', component: CuentaListComponent },
  { path: 'cuentas/nueva', component: CuentaFormComponent },

  { path: 'transacciones', component: TransaccionListComponent },
  { path: 'transacciones/nueva', component: TransaccionFormComponent },

  { path: '**', redirectTo: 'clientes' }
];
