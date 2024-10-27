package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.clients.ColaboradoresProxy;
import ar.edu.utn.dds.k3003.facades.FachadaColaboradores;
import ar.edu.utn.dds.k3003.facades.FachadaHeladeras;
import ar.edu.utn.dds.k3003.facades.FachadaLogistica;
import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.*;
import ar.edu.utn.dds.k3003.model.Incidente;
import ar.edu.utn.dds.k3003.model.dtos.IncidenteDTO;
import ar.edu.utn.dds.k3003.model.enums.EstadoIncidenteEnum;
import ar.edu.utn.dds.k3003.repositories.*;
import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import ar.edu.utn.dds.k3003.clients.ViandasRetrofitClient;
import lombok.Setter;
import org.mockito.internal.matchers.Null;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import io.javalin.micrometer.MicrometerPlugin;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmHeapPressureMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.prometheusmetrics.PrometheusConfig;

public class Fachada {

    public IncidenteRepository incidenteRepository;
    public IncidenteMapper incidenteMapper;

    private FachadaViandas fachadaViandas;
    private FachadaHeladeras fachadaHeladeras;

    private ColaboradoresProxy fachadaColaboradores;

    private Counter trasladosAsignadosCounter;
    private Counter rutasCreadasCounter;
    private Counter trasladosRetiradosCounter;
    private Counter trasladosDepositadosCounter;

    public Fachada() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("postgres");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        this.incidenteRepository = new IncidenteRepository(entityManager);
        this.incidenteMapper = new IncidenteMapper();
    }

    public IncidenteDTO crearIncidente(IncidenteDTO incidenteDTO) throws IllegalArgumentException {

        if(incidenteDTO.getHeladeraId() == null){
            throw new IllegalArgumentException("El id de la heladera no puede ser nulo");
        }

        if(incidenteDTO.getTipoIncidente() == null){
            throw new IllegalArgumentException("El tipo de incidente no puede ser nulo");
        }

        // Creo un nuevo incidente con los datos recibidos
        Incidente incidente = new Incidente(
                EstadoIncidenteEnum.ACTIVO,
                incidenteDTO.getTipoIncidente(),
                incidenteDTO.getHeladeraId(),
                incidenteDTO.isExcedeTemperatura(),
                incidenteDTO.getExcesoTemperatura(),
                incidenteDTO.getTiempoSinRespuesta()
        );

        // TODO: Descomentar cuando se implemente la funcionalidad en Heladeras que modifica el estado de una heladera
        // HeladeraDTO heladeraDTO = this.fachadaHeladeras.modificarEstadoHeladera(incidenteDTO.getHeladeraId(), EstadoHeladeraEnum.INACTIVA);

        // TODO: Descomentar cuando se implementa la funcionalidad en Colaboradores que reporta una falla
         this.fachadaColaboradores.reportarFalla(incidenteDTO);

        // Guardo el incidente en la base de datos
        Incidente incidenteGuardado = this.incidenteRepository.save(incidente);

        // Devuelvo un DTO con la información del incidente guardado
        return incidenteMapper.map(incidenteGuardado);
    }

    public IncidenteDTO buscarIncidente(Long idIncidente) throws NoSuchElementException {
        Incidente incidente = this.incidenteRepository.findById(idIncidente);
        IncidenteDTO incidenteDTO= new IncidenteDTO(incidente.getTipoIncidente(), incidente.getHeladeraId(), incidente.getEstadoIncidente(), incidente.isExcedeTemperatura(), incidente.getExcesoTemperatura(), incidente.getTiempoSinRespuesta());
        incidenteDTO.setId(incidente.getId());
        return incidenteDTO;
    }

    public void setHeladerasProxy(FachadaHeladeras fachadaHeladeras) {
        this.fachadaHeladeras = fachadaHeladeras;
    }

    public void setViandasProxy(FachadaViandas fachadaViandas) {
        this.fachadaViandas = fachadaViandas;
    }

    public void setRegistry(PrometheusMeterRegistry registry) {

        this.trasladosAsignadosCounter = Counter.builder("app.traslados.asignados")
                .description("Numero de traslados asignados")
                .register(registry);
        this.rutasCreadasCounter = Counter.builder("app.rutas.creadas")
                .description("Numero de rutas creadas")
                .register(registry);
        this.trasladosRetiradosCounter = Counter.builder("app.traslados.retirados")
                .description("Numero de traslados retirados")
                .register(registry);
        this.trasladosDepositadosCounter = Counter.builder("app.traslados.depositados")
                .description("Numero de traslados depositados")
                .register(registry);
    }
}
