package com.concesionarioquery.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.concesionarioquery.myapp.IntegrationTest;
import com.concesionarioquery.myapp.domain.Empleado;
import com.concesionarioquery.myapp.repository.EmpleadoRepository;
import com.concesionarioquery.myapp.service.dto.EmpleadoDTO;
import com.concesionarioquery.myapp.service.mapper.EmpleadoMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EmpleadoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmpleadoResourceIT {

    private static final String DEFAULT_DNI = "AAAAAAAAAA";
    private static final String UPDATED_DNI = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMEROVENTAS = 1;
    private static final Integer UPDATED_NUMEROVENTAS = 2;

    private static final Integer DEFAULT_TIER = 1;
    private static final Integer UPDATED_TIER = 2;

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String ENTITY_API_URL = "/api/empleados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private EmpleadoMapper empleadoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmpleadoMockMvc;

    private Empleado empleado;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empleado createEntity(EntityManager em) {
        Empleado empleado = new Empleado()
            .dni(DEFAULT_DNI)
            .nombre(DEFAULT_NOMBRE)
            .numeroventas(DEFAULT_NUMEROVENTAS)
            .tier(DEFAULT_TIER)
            .activo(DEFAULT_ACTIVO);
        return empleado;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empleado createUpdatedEntity(EntityManager em) {
        Empleado empleado = new Empleado()
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .numeroventas(UPDATED_NUMEROVENTAS)
            .tier(UPDATED_TIER)
            .activo(UPDATED_ACTIVO);
        return empleado;
    }

    @BeforeEach
    public void initTest() {
        empleado = createEntity(em);
    }

    @Test
    @Transactional
    void createEmpleado() throws Exception {
        int databaseSizeBeforeCreate = empleadoRepository.findAll().size();
        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);
        restEmpleadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(empleadoDTO)))
            .andExpect(status().isCreated());

        // Validate the Empleado in the database
        List<Empleado> empleadoList = empleadoRepository.findAll();
        assertThat(empleadoList).hasSize(databaseSizeBeforeCreate + 1);
        Empleado testEmpleado = empleadoList.get(empleadoList.size() - 1);
        assertThat(testEmpleado.getDni()).isEqualTo(DEFAULT_DNI);
        assertThat(testEmpleado.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEmpleado.getNumeroventas()).isEqualTo(DEFAULT_NUMEROVENTAS);
        assertThat(testEmpleado.getTier()).isEqualTo(DEFAULT_TIER);
        assertThat(testEmpleado.getActivo()).isEqualTo(DEFAULT_ACTIVO);
    }

    @Test
    @Transactional
    void createEmpleadoWithExistingId() throws Exception {
        // Create the Empleado with an existing ID
        empleado.setId(1L);
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        int databaseSizeBeforeCreate = empleadoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmpleadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(empleadoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        List<Empleado> empleadoList = empleadoRepository.findAll();
        assertThat(empleadoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmpleados() throws Exception {
        // Initialize the database
        empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList
        restEmpleadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empleado.getId().intValue())))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].numeroventas").value(hasItem(DEFAULT_NUMEROVENTAS)))
            .andExpect(jsonPath("$.[*].tier").value(hasItem(DEFAULT_TIER)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO.booleanValue())));
    }

    @Test
    @Transactional
    void getEmpleado() throws Exception {
        // Initialize the database
        empleadoRepository.saveAndFlush(empleado);

        // Get the empleado
        restEmpleadoMockMvc
            .perform(get(ENTITY_API_URL_ID, empleado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(empleado.getId().intValue()))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.numeroventas").value(DEFAULT_NUMEROVENTAS))
            .andExpect(jsonPath("$.tier").value(DEFAULT_TIER))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingEmpleado() throws Exception {
        // Get the empleado
        restEmpleadoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmpleado() throws Exception {
        // Initialize the database
        empleadoRepository.saveAndFlush(empleado);

        int databaseSizeBeforeUpdate = empleadoRepository.findAll().size();

        // Update the empleado
        Empleado updatedEmpleado = empleadoRepository.findById(empleado.getId()).get();
        // Disconnect from session so that the updates on updatedEmpleado are not directly saved in db
        em.detach(updatedEmpleado);
        updatedEmpleado
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .numeroventas(UPDATED_NUMEROVENTAS)
            .tier(UPDATED_TIER)
            .activo(UPDATED_ACTIVO);
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(updatedEmpleado);

        restEmpleadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, empleadoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(empleadoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Empleado in the database
        List<Empleado> empleadoList = empleadoRepository.findAll();
        assertThat(empleadoList).hasSize(databaseSizeBeforeUpdate);
        Empleado testEmpleado = empleadoList.get(empleadoList.size() - 1);
        assertThat(testEmpleado.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testEmpleado.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEmpleado.getNumeroventas()).isEqualTo(UPDATED_NUMEROVENTAS);
        assertThat(testEmpleado.getTier()).isEqualTo(UPDATED_TIER);
        assertThat(testEmpleado.getActivo()).isEqualTo(UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void putNonExistingEmpleado() throws Exception {
        int databaseSizeBeforeUpdate = empleadoRepository.findAll().size();
        empleado.setId(count.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, empleadoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(empleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        List<Empleado> empleadoList = empleadoRepository.findAll();
        assertThat(empleadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmpleado() throws Exception {
        int databaseSizeBeforeUpdate = empleadoRepository.findAll().size();
        empleado.setId(count.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(empleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        List<Empleado> empleadoList = empleadoRepository.findAll();
        assertThat(empleadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmpleado() throws Exception {
        int databaseSizeBeforeUpdate = empleadoRepository.findAll().size();
        empleado.setId(count.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(empleadoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Empleado in the database
        List<Empleado> empleadoList = empleadoRepository.findAll();
        assertThat(empleadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmpleadoWithPatch() throws Exception {
        // Initialize the database
        empleadoRepository.saveAndFlush(empleado);

        int databaseSizeBeforeUpdate = empleadoRepository.findAll().size();

        // Update the empleado using partial update
        Empleado partialUpdatedEmpleado = new Empleado();
        partialUpdatedEmpleado.setId(empleado.getId());

        partialUpdatedEmpleado.dni(UPDATED_DNI).nombre(UPDATED_NOMBRE).tier(UPDATED_TIER);

        restEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmpleado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmpleado))
            )
            .andExpect(status().isOk());

        // Validate the Empleado in the database
        List<Empleado> empleadoList = empleadoRepository.findAll();
        assertThat(empleadoList).hasSize(databaseSizeBeforeUpdate);
        Empleado testEmpleado = empleadoList.get(empleadoList.size() - 1);
        assertThat(testEmpleado.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testEmpleado.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEmpleado.getNumeroventas()).isEqualTo(DEFAULT_NUMEROVENTAS);
        assertThat(testEmpleado.getTier()).isEqualTo(UPDATED_TIER);
        assertThat(testEmpleado.getActivo()).isEqualTo(DEFAULT_ACTIVO);
    }

    @Test
    @Transactional
    void fullUpdateEmpleadoWithPatch() throws Exception {
        // Initialize the database
        empleadoRepository.saveAndFlush(empleado);

        int databaseSizeBeforeUpdate = empleadoRepository.findAll().size();

        // Update the empleado using partial update
        Empleado partialUpdatedEmpleado = new Empleado();
        partialUpdatedEmpleado.setId(empleado.getId());

        partialUpdatedEmpleado
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .numeroventas(UPDATED_NUMEROVENTAS)
            .tier(UPDATED_TIER)
            .activo(UPDATED_ACTIVO);

        restEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmpleado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmpleado))
            )
            .andExpect(status().isOk());

        // Validate the Empleado in the database
        List<Empleado> empleadoList = empleadoRepository.findAll();
        assertThat(empleadoList).hasSize(databaseSizeBeforeUpdate);
        Empleado testEmpleado = empleadoList.get(empleadoList.size() - 1);
        assertThat(testEmpleado.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testEmpleado.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEmpleado.getNumeroventas()).isEqualTo(UPDATED_NUMEROVENTAS);
        assertThat(testEmpleado.getTier()).isEqualTo(UPDATED_TIER);
        assertThat(testEmpleado.getActivo()).isEqualTo(UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void patchNonExistingEmpleado() throws Exception {
        int databaseSizeBeforeUpdate = empleadoRepository.findAll().size();
        empleado.setId(count.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, empleadoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(empleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        List<Empleado> empleadoList = empleadoRepository.findAll();
        assertThat(empleadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmpleado() throws Exception {
        int databaseSizeBeforeUpdate = empleadoRepository.findAll().size();
        empleado.setId(count.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(empleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        List<Empleado> empleadoList = empleadoRepository.findAll();
        assertThat(empleadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmpleado() throws Exception {
        int databaseSizeBeforeUpdate = empleadoRepository.findAll().size();
        empleado.setId(count.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(empleadoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Empleado in the database
        List<Empleado> empleadoList = empleadoRepository.findAll();
        assertThat(empleadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmpleado() throws Exception {
        // Initialize the database
        empleadoRepository.saveAndFlush(empleado);

        int databaseSizeBeforeDelete = empleadoRepository.findAll().size();

        // Delete the empleado
        restEmpleadoMockMvc
            .perform(delete(ENTITY_API_URL_ID, empleado.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Empleado> empleadoList = empleadoRepository.findAll();
        assertThat(empleadoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
