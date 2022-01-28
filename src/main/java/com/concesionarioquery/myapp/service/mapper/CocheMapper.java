package com.concesionarioquery.myapp.service.mapper;

import com.concesionarioquery.myapp.domain.Coche;
import com.concesionarioquery.myapp.service.dto.CocheDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Coche} and its DTO {@link CocheDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CocheMapper extends EntityMapper<CocheDTO, Coche> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CocheDTO toDtoId(Coche coche);
}
