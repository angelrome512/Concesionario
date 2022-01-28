export interface IEmpleado {
  id?: number;
  dni?: string | null;
  nombre?: string | null;
  numeroventas?: number | null;
  tier?: number | null;
  activo?: boolean | null;
}

export class Empleado implements IEmpleado {
  constructor(
    public id?: number,
    public dni?: string | null,
    public nombre?: string | null,
    public numeroventas?: number | null,
    public tier?: number | null,
    public activo?: boolean | null
  ) {
    this.activo = this.activo ?? false;
  }
}

export function getEmpleadoIdentifier(empleado: IEmpleado): number | undefined {
  return empleado.id;
}
