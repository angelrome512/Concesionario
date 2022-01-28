import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IVenta, Venta } from '../venta.model';
import { VentaService } from '../service/venta.service';
import { ICoche } from 'app/entities/coche/coche.model';
import { CocheService } from 'app/entities/coche/service/coche.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EmpleadoService } from 'app/entities/empleado/service/empleado.service';

@Component({
  selector: 'jhi-venta-update',
  templateUrl: './venta-update.component.html',
})
export class VentaUpdateComponent implements OnInit {
  isSaving = false;

  cochesCollection: ICoche[] = [];
  clientesSharedCollection: ICliente[] = [];
  empleadosSharedCollection: IEmpleado[] = [];

  editForm = this.fb.group({
    id: [],
    fecha: [],
    tipopago: [],
    total: [],
    coches: [],
    cliente: [],
    empleado: [],
  });

  constructor(
    protected ventaService: VentaService,
    protected cocheService: CocheService,
    protected clienteService: ClienteService,
    protected empleadoService: EmpleadoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ venta }) => {
      this.updateForm(venta);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const venta = this.createFromForm();
    if (venta.id !== undefined) {
      this.subscribeToSaveResponse(this.ventaService.update(venta));
    } else {
      this.subscribeToSaveResponse(this.ventaService.create(venta));
    }
  }

  trackCocheById(index: number, item: ICoche): number {
    return item.id!;
  }

  trackClienteById(index: number, item: ICliente): number {
    return item.id!;
  }

  trackEmpleadoById(index: number, item: IEmpleado): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVenta>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(venta: IVenta): void {
    this.editForm.patchValue({
      id: venta.id,
      fecha: venta.fecha,
      tipopago: venta.tipopago,
      total: venta.total,
      coches: venta.coches,
      cliente: venta.cliente,
      empleado: venta.empleado,
    });

    this.cochesCollection = this.cocheService.addCocheToCollectionIfMissing(this.cochesCollection, venta.coches);
    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing(this.clientesSharedCollection, venta.cliente);
    this.empleadosSharedCollection = this.empleadoService.addEmpleadoToCollectionIfMissing(this.empleadosSharedCollection, venta.empleado);
  }

  protected loadRelationshipsOptions(): void {
    this.cocheService
      .cochesExpuestos(true)
      .pipe(map((res: HttpResponse<ICoche[]>) => res.body ?? []))
      .pipe(map((coches: ICoche[]) => this.cocheService.addCocheToCollectionIfMissing(coches, this.editForm.get('coches')!.value)))
      .subscribe((coches: ICoche[]) => (this.cochesCollection = coches));

    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(
        map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing(clientes, this.editForm.get('cliente')!.value))
      )
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));

    this.empleadoService
      .query()
      .pipe(map((res: HttpResponse<IEmpleado[]>) => res.body ?? []))
      .pipe(
        map((empleados: IEmpleado[]) =>
          this.empleadoService.addEmpleadoToCollectionIfMissing(empleados, this.editForm.get('empleado')!.value)
        )
      )
      .subscribe((empleados: IEmpleado[]) => (this.empleadosSharedCollection = empleados));
  }

  protected createFromForm(): IVenta {
    return {
      ...new Venta(),
      id: this.editForm.get(['id'])!.value,
      fecha: this.editForm.get(['fecha'])!.value,
      tipopago: this.editForm.get(['tipopago'])!.value,
      total: this.editForm.get(['total'])!.value,
      coches: this.editForm.get(['coches'])!.value,
      cliente: this.editForm.get(['cliente'])!.value,
      empleado: this.editForm.get(['empleado'])!.value,
    };
  }
}
