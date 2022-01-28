package com.concesionarioquery.myapp.service;

import com.concesionarioquery.myapp.domain.Coche;
import com.concesionarioquery.myapp.service.dto.CocheDTO;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.concesionarioquery.myapp.domain.Coche}.
 */
public interface CocheService {
    /**
     * Save a coche.
     *
     * @param cocheDTO the entity to save.
     * @return the persisted entity.
     */
    CocheDTO save(CocheDTO cocheDTO);

    /**
     * Partially updates a coche.
     *
     * @param cocheDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CocheDTO> partialUpdate(CocheDTO cocheDTO);

    /**
     * Get all the coches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CocheDTO> findAllByParam(Pageable pageable, String filtro);

     /**
     * Get all the coches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CocheDTO> findAll(Pageable pageable);

    /**
     * Get the "id" coche.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CocheDTO> findOne(Long id);

    Page<CocheDTO> cochesExpuestos(Boolean expo, Pageable pageable);

    /**
     * Delete the "id" coche.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
