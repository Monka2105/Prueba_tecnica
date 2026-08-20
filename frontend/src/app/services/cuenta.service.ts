import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cuenta } from '../models/cuenta.model';

@Injectable({ providedIn: 'root' })
export class CuentaService {
  private readonly baseUrl = '/api/cuentas';

  constructor(private http: HttpClient) {}

  listar(): Observable<Cuenta[]> {
    return this.http.get<Cuenta[]>(this.baseUrl);
  }

  obtener(id: number): Observable<Cuenta> {
    return this.http.get<Cuenta>(`${this.baseUrl}/${id}`);
  }

  obtenerPorCliente(clienteId: number): Observable<Cuenta[]> {
    return this.http.get<Cuenta[]>(`${this.baseUrl}/cliente/${clienteId}`);
  }

  crear(cuenta: Cuenta): Observable<Cuenta> {
    return this.http.post<Cuenta>(this.baseUrl, cuenta);
  }

  activar(id: number): Observable<Cuenta> {
    return this.http.patch<Cuenta>(`${this.baseUrl}/${id}/activar`, {});
  }

  inactivar(id: number): Observable<Cuenta> {
    return this.http.patch<Cuenta>(`${this.baseUrl}/${id}/inactivar`, {});
  }

  cancelar(id: number): Observable<Cuenta> {
    return this.http.patch<Cuenta>(`${this.baseUrl}/${id}/cancelar`, {});
  }
}
