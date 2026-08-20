export type TipoCuenta = 'CUENTA_AHORROS' | 'CUENTA_CORRIENTE';
export type EstadoCuenta = 'ACTIVA' | 'INACTIVA' | 'CANCELADA';

export interface Cuenta {
  id?: number;
  tipoCuenta: TipoCuenta;
  numeroCuenta?: string;
  estado?: EstadoCuenta;
  saldo: number;
  saldoDisponible?: number;
  exentaGmf?: boolean;
  fechaCreacion?: string;
  fechaModificacion?: string;
  clienteId: number;
  clienteNombreCompleto?: string;
}
