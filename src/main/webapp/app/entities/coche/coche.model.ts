export interface ICoche {
  id?: number;
  marca?: string | null;
  modelo?: string | null;
  color?: string | null;
  numeroSerie?: string | null;
  precio?: number | null;
  expuesto?: boolean | null;
}

export class Coche implements ICoche {
  constructor(
    public id?: number,
    public marca?: string | null,
    public modelo?: string | null,
    public color?: string | null,
    public numeroSerie?: string | null,
    public precio?: number | null,
    public expuesto?: boolean | null
  ) {
    this.expuesto = this.expuesto ?? false;
  }
}

export function getCocheIdentifier(coche: ICoche): number | undefined {
  return coche.id;
}
