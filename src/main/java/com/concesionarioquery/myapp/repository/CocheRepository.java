package com.concesionarioquery.myapp.repository;

import java.util.List;

import com.concesionarioquery.myapp.domain.Coche;
import com.concesionarioquery.myapp.service.dto.CocheDTO;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Coche entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CocheRepository extends JpaRepository<Coche, Long>, JpaSpecificationExecutor<Coche> {

    @Query ("select c from Coche c where c.expuesto = :expo")
    Page<Coche> cochesExpuestos(@Param("expo")Boolean expo, Pageable pageable);

    @Query ("select c from Coche c where c.modelo like %:modelo%")
    Page<Coche> cochesPaginadosPorModelo(@Param("modelo") String modelo, Pageable pageable);
}
