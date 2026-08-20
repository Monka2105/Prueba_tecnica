export interface Cliente {
  id?: number;
  tipoDocumento: string;
  numeroDocumento: string;
  nombres: string;
  apellidos: string;
  email: string;
  fechaNacimiento: string;
  fechaCreacion?: string;
  fechaModificacion?: string;
}
