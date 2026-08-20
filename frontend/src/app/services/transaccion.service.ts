import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Transaccion } from '../models/transaccion.model';

@Injectable({ providedIn: 'root' })
export class TransaccionService {
  private readonly baseUrl = '/api/transacciones';

  constructor(private http: HttpClient) {}

  listar(): Observable<Transaccion[]> {
    return this.http.get<Transaccion[]>(this.baseUrl);
  }

  obtener(id: number): Observable<Transaccion> {
    return this.http.get<Transaccion>(`${this.baseUrl}/${id}`);
  }

  obtenerPorCuenta(cuentaId: number): Observable<Transaccion[]> {
    return this.http.get<Transaccion[]>(`${this.baseUrl}/cuenta/${cuentaId}`);
  }

  registrar(transaccion: Transaccion): Observable<Transaccion> {
    return this.http.post<Transaccion>(this.baseUrl, transaccion);
  }
}
