package ar.edu.utn.dds.k3003.model.app;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.FachadaHeladeras;
import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.*;
import ar.edu.utn.dds.k3003.facades.exceptions.TrasladoNoAsignableException;
import ar.edu.utn.dds.k3003.model.Ruta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
public class FachadaLogisticaTest {
    Fachada fachada;
    @Mock
    FachadaViandas fachadaViandas;
    @Mock
    FachadaHeladeras fachadaHeladeras;

    @BeforeEach
    void setUp() {
        this.fachada = new Fachada();
        this.fachada.setHeladerasProxy(this.fachadaHeladeras);
        this.fachada.setViandasProxy(this.fachadaViandas);
    }

    @Test
    @DisplayName("Método: agregar")
    void agregar(){

        RutaDTO ruta = new RutaDTO(1L, 1, 2);

        RutaDTO result = fachada.agregar(ruta);

        Assertions.assertNotEquals(ruta, result,"La ruta antes de agregarla y despues de agregarla son diferentes porque una tiene id y la otra no.");
    }

    @Test
    @DisplayName("Método: buscarRutaXOrigenYDestino")
    void buscarRutaXOrigenYDestino(){

        Integer origen = 1;
        Integer destino = 2;

        fachada.agregar(new RutaDTO(1L, 1, 2));
        Ruta result = fachada.buscarRutaXOrigenYDestino(origen, destino);

        Assertions.assertNotNull(result, "La ruta no es nula porque agregue una ruta para ese origen y destino.");
    }

    @Test
    @DisplayName("Método: buscarXId")
    void buscarXId() throws NoSuchElementException {

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            fachada.buscarXId(2L);
        }, "Si el traslado no fue añadido a la fachada, debería fallar la busqueda por id");
    }

    @Test
    @DisplayName("Método: asignarTraslado")
    void asignarTraslado(){

        TrasladoDTO trasladoDTO = new TrasladoDTO("qr", EstadoTrasladoEnum.EN_VIAJE, LocalDateTime.now(), 1, 2);

        Assertions.assertThrows(TrasladoNoAsignableException.class, () -> {
            fachada.asignarTraslado(trasladoDTO);
        }, "Si el traslado no fue añadido a la fachada, debería fallar la busqueda por id");

    }

    @Test
    @DisplayName("Método: trasladosDeColaborador")
    void trasladosDeColaborador(){

        List<TrasladoDTO> lista = new ArrayList<>();

        Assertions.assertEquals(lista, fachada.trasladosDeColaborador(1L,1,1), "La lista de traslados debería ser vacía porque no se añadieron traslados a la fachada");
    }

    @Test
    @DisplayName("Método: trasladoRetirado")
    void trasladoRetirado() throws NoSuchElementException {

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            fachada.trasladoRetirado(1L);
        }, "Si el traslado no fue añadido a la fachada, debería fallar cuando lo quiero retirar");
    }

    @Test
    @DisplayName("Método: trasladoDepositado")
    void trasladoDepositado() throws NoSuchElementException{

            Assertions.assertThrows(NoSuchElementException.class, () -> {
                fachada.trasladoDepositado(1L);
            }, "Si el traslado no fue añadido a la fachada, debería fallar cuando lo quiero depositar");
    }
}