package com.concesionarioquery.myapp.service.mapper;

import com.concesionarioquery.myapp.domain.Venta;
import com.concesionarioquery.myapp.service.dto.VentaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Venta} and its DTO {@link VentaDTO}.
 */
@Mapper(componentModel = "spring", uses = { CocheMapper.class, ClienteMapper.class, EmpleadoMapper.class })
public interface VentaMapper extends EntityMapper<VentaDTO, Venta> {
    @Mapping(target = "coches", source = "coches", qualifiedByName = "id")
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "id")
    @Mapping(target = "empleado", source = "empleado", qualifiedByName = "id")
    VentaDTO toDto(Venta s);
}
