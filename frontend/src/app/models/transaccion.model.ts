export type TipoTransaccion = 'CONSIGNACION' | 'RETIRO' | 'TRANSFERENCIA';
export type TipoMovimiento = 'CREDITO' | 'DEBITO';

export interface Transaccion {
  id?: number;
  cuentaId: number;
  cuentaRelacionadaId?: number | null;
  tipoTransaccion: TipoTransaccion;
  tipoMovimiento?: TipoMovimiento;
  monto: number;
  saldoResultante?: number;
  fechaTransaccion?: string;
  descripcion?: string;
}
