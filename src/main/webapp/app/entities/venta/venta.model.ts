import dayjs from 'dayjs/esm';
import { ICoche } from 'app/entities/coche/coche.model';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { IEmpleado } from 'app/entities/empleado/empleado.model';

export interface IVenta {
  id?: number;
  fecha?: dayjs.Dayjs | null;
  tipopago?: string | null;
  total?: number | null;
  coches?: ICoche | null;
  cliente?: ICliente | null;
  empleado?: IEmpleado | null;
}

export class Venta implements IVenta {
  constructor(
    public id?: number,
    public fecha?: dayjs.Dayjs | null,
    public tipopago?: string | null,
    public total?: number | null,
    public coches?: ICoche | null,
    public cliente?: ICliente | null,
    public empleado?: IEmpleado | null
  ) {}
}

export function getVentaIdentifier(venta: IVenta): number | undefined {
  return venta.id;
}
