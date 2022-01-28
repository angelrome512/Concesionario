package com.concesionarioquery.myapp.repository;

import java.util.List;

import com.concesionarioquery.myapp.domain.Venta;
import com.concesionarioquery.myapp.service.dto.CocheDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Venta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    // @Query ("select v from Venta v where v.expuesto != null")
    // Page<Venta> cochesExpuestos(Pageable pageable);
}
