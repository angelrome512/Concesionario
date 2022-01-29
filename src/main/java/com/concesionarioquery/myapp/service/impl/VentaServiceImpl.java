package com.concesionarioquery.myapp.service.impl;

import com.concesionarioquery.myapp.domain.Coche;
import com.concesionarioquery.myapp.domain.Empleado;
import com.concesionarioquery.myapp.domain.Venta;
import com.concesionarioquery.myapp.repository.CocheRepository;
import com.concesionarioquery.myapp.repository.EmpleadoRepository;
import com.concesionarioquery.myapp.repository.VentaRepository;
import com.concesionarioquery.myapp.service.VentaService;
import com.concesionarioquery.myapp.service.dto.CocheDTO;
import com.concesionarioquery.myapp.service.dto.VentaDTO;
import com.concesionarioquery.myapp.service.mapper.CocheMapper;
import com.concesionarioquery.myapp.service.mapper.VentaMapper;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Venta}.
 */
@Service
@Transactional
public class VentaServiceImpl implements VentaService {

    private final Logger log = LoggerFactory.getLogger(VentaServiceImpl.class);

    private final VentaRepository ventaRepository;

    private final EmpleadoRepository empleadoRepository;

    private final CocheRepository cocheRepository;

    private final VentaMapper ventaMapper;

    public VentaServiceImpl(VentaRepository ventaRepository, VentaMapper ventaMapper, EmpleadoRepository empleadoRespository,  CocheRepository cocheRepository) {
        this.ventaRepository = ventaRepository;
        this.ventaMapper = ventaMapper;
        this.empleadoRepository = empleadoRespository;
        this.cocheRepository = cocheRepository;
    }

    @Override
    public VentaDTO save(VentaDTO ventaDTO) {
        log.debug("Request to save Venta : {}", ventaDTO);
        Venta venta = ventaMapper.toEntity(ventaDTO);

        Empleado empleado = venta.getEmpleado();
        Coche coche = venta.getCoches();
        coche.setExpuesto(false);

        if(null == empleado.getNumeroventas()){
            empleado.setNumeroventas(0);
        }
        empleado.setNumeroventas(empleado.getNumeroventas()+1);

        coche = cocheRepository.save(coche);
        empleado = empleadoRepository.save(empleado);
        venta = ventaRepository.save(venta);
        return ventaMapper.toDto(venta);
    }

    @Override
    public Optional<VentaDTO> partialUpdate(VentaDTO ventaDTO) {
        log.debug("Request to partially update Venta : {}", ventaDTO);

        return ventaRepository
            .findById(ventaDTO.getId())
            .map(existingVenta -> {
                ventaMapper.partialUpdate(existingVenta, ventaDTO);

                return existingVenta;
            })
            .map(ventaRepository::save)
            .map(ventaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VentaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ventas");
        return ventaRepository.findAll(pageable).map(ventaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VentaDTO> findOne(Long id) {
        log.debug("Request to get Venta : {}", id);
        return ventaRepository.findById(id).map(ventaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Venta : {}", id);
        ventaRepository.deleteById(id);
    }
}
