package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaColaboradores;
import ar.edu.utn.dds.k3003.facades.FachadaHeladeras;
import ar.edu.utn.dds.k3003.facades.FachadaLogistica;
import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.*;
import ar.edu.utn.dds.k3003.facades.exceptions.TrasladoNoAsignableException;
import ar.edu.utn.dds.k3003.model.Incidente;
import ar.edu.utn.dds.k3003.model.Ruta;
import ar.edu.utn.dds.k3003.model.Traslado;
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

public class Fachada implements FachadaLogistica {

    public final RutaRepository rutaRepository;
    private final RutaMapper rutaMapper;
    public final TrasladoRepository trasladoRepository;
    private final TrasladoMapper trasladoMapper;

    public IncidenteRepository incidenteRepository;
    public IncidenteMapper incidenteMapper;

    private FachadaViandas fachadaViandas;
    private FachadaHeladeras fachadaHeladeras;

    private Counter trasladosAsignadosCounter;
    private Counter rutasCreadasCounter;
    private Counter trasladosRetiradosCounter;
    private Counter trasladosDepositadosCounter;

    public Fachada() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("postgres");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        this.rutaRepository = new RutaRepository(entityManager);
        this.trasladoRepository = new TrasladoRepository(entityManager);
        this.rutaMapper = new RutaMapper();
        this.trasladoMapper = new TrasladoMapper();

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
        // this.fachadaHeladeras.modificarEstadoHeladera(incidenteDTO.getHeladeraId(), EstadoHeladeraEnum.INACTIVA);

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






    // Metodos deprecados ------------------------------------

    /*
    * "Un colaborador de transporte establece que puede llevar
    * una vianda entre 2 heladeras"
    * */

    @Override
    public RutaDTO agregar(RutaDTO rutaDTO) throws NoSuchElementException {
        try {
            Ruta ruta_sin_id = new Ruta(rutaDTO.getColaboradorId(), rutaDTO.getHeladeraIdOrigen(), rutaDTO.getHeladeraIdDestino());
            Ruta ruta_con_id = this.rutaRepository.save(ruta_sin_id);

            // Verificar si rutasCreadasCounter no es null antes de incrementar
            if (rutasCreadasCounter != null) {
                rutasCreadasCounter.increment();
            } else {
                System.out.println("Advertencia: rutasCreadasCounter es null. Incremento no realizado.");
            }

            return rutaMapper.map(ruta_con_id);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getLocalizedMessage());
        }
    }

    public Ruta buscarRutaXOrigenYDestino(Integer Origen, Integer Destino) throws NoSuchElementException {
        List<Ruta> rutas = this.rutaRepository.findByHeladeras(Origen, Destino);

        if (rutas.isEmpty()) {
            throw new NoSuchElementException("No hay ninguna ruta cargada para la heladeraOrigen " + Origen + " hacia la heladeraDestino " + Destino);
        }

        if (rutas.size() > 1) {
            // Selecciono una ruta al azar de las disponibles
            Random rand = new Random();
            return rutas.get(rand.nextInt(rutas.size()));
        }

        // Si solo hay una ruta, la devuelvo
        return rutas.get(0);
    }

    /*
    * Dado un id de traslado, se busca el mismo en el repositorio de traslados
    * y se devuelve un DTO con la información del traslado, o se lanza una excepción
    * si no se encuentra el traslado del tipo NoSuchElementException
    * */

    @Override
    public TrasladoDTO buscarXId(Long idTraslado) throws NoSuchElementException {
        Traslado traslado = this.trasladoRepository.findById(idTraslado);
        TrasladoDTO trasladoDTO= new TrasladoDTO(traslado.getQrVianda(), traslado.getEstado(), traslado.getFechaTraslado(), traslado.getRuta().getHeladeraIdOrigen(), traslado.getRuta().getHeladeraIdDestino());
        trasladoDTO.setColaboradorId(traslado.getRuta().getColaboradorId());
        trasladoDTO.setId(traslado.getId());
        return trasladoDTO;
    }

    /*
    * "Cuando se determina que hay que llevar una vianda de una
    * heladera a otra, lo siguiente que hay que hacer es asignar
    * un transportista que se haya comprometido a cumplir esa ruta"
    * */

    @Override
    public TrasladoDTO asignarTraslado(TrasladoDTO trasladoDTO) throws TrasladoNoAsignableException, NoSuchElementException {
        Ruta ruta;

        // Si no se encuentra la vianda, se lanza una excepción NoSuchElementException
        ViandaDTO vianda = this.fachadaViandas.buscarXQR(trasladoDTO.getQrVianda());

        // Si no se encuentra una ruta para ese origen y destino, se lanza una excepción TrasladoNoAsignableException
        try {
            ruta = buscarRutaXOrigenYDestino(trasladoDTO.getHeladeraOrigen(), trasladoDTO.getHeladeraDestino());
        } catch (NoSuchElementException e) {
            throw new TrasladoNoAsignableException(e.getLocalizedMessage());
        }

        // Si ya existe un traslado para esa vianda y esa ruta, se lanza una excepción TrasladoNoAsignableException. Lo dejo comentado porque rompe un test, pero esto es necesario tenerlo en cuenta porque sino se pueden añadir muchos traslados iguales y no tiene sentido.

        if (!this.trasladoRepository.findByRuta(ruta).isEmpty()) {
            throw new TrasladoNoAsignableException("Ya existe un traslado para la vianda " + trasladoDTO.getQrVianda() + " con el id de ruta " + ruta.getId() + " desde la heladera origen " + ruta.getHeladeraIdOrigen() + " hacia la heladera destino " + ruta.getHeladeraIdDestino() +" asignado al colaborador " + ruta.getColaboradorId() + ".");
        }

        // Si tanto la ruta como la vianda existen, procedo a crear y guardar el traslado
        Traslado traslado = this.trasladoRepository.save(
                new Traslado(
                        trasladoDTO.getQrVianda(),
                        ruta,
                        EstadoTrasladoEnum.ASIGNADO,
                        trasladoDTO.getFechaTraslado()
                )
        );

        // Modifico el estado de la vianda a EN_TRASLADO
        fachadaViandas.modificarEstado(vianda.getCodigoQR(), EstadoViandaEnum.EN_TRASLADO);

        // Verificar si trasladosAsignadosCounter no es null antes de incrementar
        if (trasladosAsignadosCounter != null) {
            trasladosAsignadosCounter.increment();
        } else {
            System.out.println("Advertencia: trasladosAsignadosCounter es null. Incremento no realizado.");
        }

        // Creo un DTO con la información del traslado
        TrasladoDTO traslado_dto = new TrasladoDTO(
                traslado.getQrVianda(),
                traslado.getEstado(),
                traslado.getFechaTraslado(),
                traslado.getRuta().getHeladeraIdOrigen(),
                traslado.getRuta().getHeladeraIdDestino()
        );

        // Asigno el id del colaborador que está en la ruta al trasladoDTO
        traslado_dto.setColaboradorId(ruta.getColaboradorId());

        // Asigno el id del traslado generado al DTO
        traslado_dto.setId(traslado.getId());

        // Retorna un DTO con la información del traslado completa
        return traslado_dto;
    }

    /*
     * "Un colaborador de transporte puede ver los traslados que
     * ha realizado en un mes y año determinado"
     * */

    @Override
    public List<TrasladoDTO> trasladosDeColaborador(Long idColaborador, Integer mes, Integer anio) {
        List<Traslado> traslados = this.trasladoRepository.findByCollaboratorId(idColaborador, mes, anio);
        return traslados.stream()
                .map(this.trasladoMapper::map)
                .collect(Collectors.toList());
    }

    /*
    * "Una vez que un trasportista tiene una vianda asignada,
    * comienza el transporte de la vianda una vez que el mismo
    * la retira de la heladera"
    *
    * "Vianda: Depositada -> EnTraslado
    *  Transporte: Asignado -> En Progreso"
    * */

    @Override
    public void trasladoRetirado(Long idTraslado) {
        Traslado traslado = this.trasladoRepository.findById(idTraslado);
        ViandaDTO vianda = this.fachadaViandas.buscarXQR(traslado.getQrVianda());

        RetiroDTO retiro = new RetiroDTO(
                vianda.getCodigoQR(),
                "123456789",
                LocalDateTime.now(),
                traslado.getRuta().getHeladeraIdOrigen()
        );

        fachadaHeladeras.retirar(retiro);

        // Modifico los estados de la vianda y el traslado. Desprecio los retornos
        fachadaViandas.modificarEstado(vianda.getCodigoQR(), EstadoViandaEnum.RETIRADA);
        this.trasladoRepository.modificarEstado(traslado.getId(), EstadoTrasladoEnum.EN_VIAJE);

        // Verificar si trasladosRetiradosCounter no es null antes de incrementar
        if (trasladosRetiradosCounter != null) {
            trasladosRetiradosCounter.increment();
        } else {
            System.out.println("Advertencia: trasladosRetiradosCounter es null. Incremento no realizado.");
        }
    }

    /*
    * "Cuando el transportista deja la vianda en la heladera
    * de destino, se establece que el traslado esta completo"
    * "Vianda: EnTraslado -> Depositado
    *  Transporte: En Progreso -> Terminado"
    * */

    @Override
    public void trasladoDepositado(Long idTraslado) {
        Traslado traslado = this.trasladoRepository.findById(idTraslado);
        ViandaDTO vianda = this.fachadaViandas.buscarXQR(traslado.getQrVianda());

        fachadaViandas.modificarHeladera(vianda.getCodigoQR(), traslado.getRuta().getHeladeraIdDestino());

        // Modifico los estados de la vianda y el traslado. Desprecio los retornos
        fachadaViandas.modificarEstado(vianda.getCodigoQR(), EstadoViandaEnum.DEPOSITADA);
        this.trasladoRepository.modificarEstado(traslado.getId(), EstadoTrasladoEnum.ENTREGADO);

        // Verificar si trasladosDepositadosCounter no es null antes de incrementar
        if (trasladosDepositadosCounter != null) {
            trasladosDepositadosCounter.increment();
        } else {
            System.out.println("Advertencia: trasladosDepositadosCounter es null. Incremento no realizado.");
        }
    }

    @Override
    public void setHeladerasProxy(FachadaHeladeras fachadaHeladeras) {
        this.fachadaHeladeras = fachadaHeladeras;
    }

    @Override
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
