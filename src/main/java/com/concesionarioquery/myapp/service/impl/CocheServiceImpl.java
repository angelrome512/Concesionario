package com.concesionarioquery.myapp.service.impl;

import com.concesionarioquery.myapp.domain.Coche;
import com.concesionarioquery.myapp.repository.CocheRepository;
import com.concesionarioquery.myapp.repository.specification.CocheSpecification;
import com.concesionarioquery.myapp.service.CocheService;
import com.concesionarioquery.myapp.service.dto.CocheDTO;
import com.concesionarioquery.myapp.service.mapper.CocheMapper;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Coche}.
 */
@Service
@Transactional
public class CocheServiceImpl implements CocheService {

    private final Logger log = LoggerFactory.getLogger(CocheServiceImpl.class);

    private final CocheRepository cocheRepository;

    private final CocheMapper cocheMapper;

    public CocheServiceImpl(CocheRepository cocheRepository, CocheMapper cocheMapper) {
        this.cocheRepository = cocheRepository;
        this.cocheMapper = cocheMapper;
    }

    @Override
    public CocheDTO save(CocheDTO cocheDTO) {
        log.debug("Request to save Coche : {}", cocheDTO);
        Coche coche = cocheMapper.toEntity(cocheDTO);
        coche = cocheRepository.save(coche);
        return cocheMapper.toDto(coche);
    }

    @Override
    public Optional<CocheDTO> partialUpdate(CocheDTO cocheDTO) {
        log.debug("Request to partially update Coche : {}", cocheDTO);

        return cocheRepository
            .findById(cocheDTO.getId())
            .map(existingCoche -> {
                cocheMapper.partialUpdate(existingCoche, cocheDTO);

                return existingCoche;
            })
            .map(cocheRepository::save)
            .map(cocheMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CocheDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Coches");
        return cocheRepository.findAll(pageable).map(cocheMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CocheDTO> findOne(Long id) {
        log.debug("Request to get Coche : {}", id);
        return cocheRepository.findById(id).map(cocheMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CocheDTO> findAllByParam(Pageable pageable, String filtro) {
        log.debug("Request to get Coche : {}");
        return cocheRepository.findAll(CocheSpecification.searchingParam(filtro),pageable).map(cocheMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CocheDTO> cochesExpuestos(Boolean expo, Pageable pageable) {
        log.debug("Request to get Coche : {}");
        return cocheRepository.cochesExpuestos(expo, pageable).map(cocheMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Coche : {}", id);
        cocheRepository.deleteById(id);
    }
}
