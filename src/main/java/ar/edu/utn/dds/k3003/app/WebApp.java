package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.clients.*;
import ar.edu.utn.dds.k3003.facades.dtos.*;
import ar.edu.utn.dds.k3003.facades.dtos.HeladeraDTO;
import ar.edu.utn.dds.k3003.facades.exceptions.TrasladoNoAsignableException;
import ar.edu.utn.dds.k3003.model.dtos.*;
import ar.edu.utn.dds.k3003.model.dtos.ColaboradorDTO;
import ar.edu.utn.dds.k3003.model.dtos.FormasDeColaborarDTO;
import ar.edu.utn.dds.k3003.model.dtos.ViandaRequest;
import ar.edu.utn.dds.k3003.model.enums.EstadoIncidenteEnum;
import ar.edu.utn.dds.k3003.model.enums.MisFormasDeColaborar;
import ar.edu.utn.dds.k3003.model.enums.TipoIncidenteEnum;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

import io.javalin.micrometer.MicrometerPlugin;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmHeapPressureMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import io.micrometer.prometheusmetrics.PrometheusConfig;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class WebApp extends TelegramLongPollingBot {

    private static ColaboradoresProxy fachadaColaboradores;
    private static LogisticaProxy fachadaLogistica;
    private static IncidentesProxy fachadaIncidentes;
    private static HeladerasProxy fachadaHeladeras;
    private static ViandasProxy fachadaViandas;

    @Override
    public void onUpdateReceived(Update update) {

        // Se verifica si el update tiene un mensaje y si el mensaje tiene texto
        if (update.hasMessage() && update.getMessage().hasText()) {

            // Se obtiene el chat_id y el mensaje recibido
            var chat_id = update.getMessage().getChatId();
            var message_received = update.getMessage().getText();

            // Se divide el mensaje recibido por espacios
            var comando = message_received.split(" ");

            // Se evalua el comando recibido
            switch (comando[0]) {

                // Modulo Colaboradores
                case "/iniciar": {
                    SendMessage msg = new SendMessage();
                    msg.setChatId(chat_id);
                    msg.setText("""
                            *BIENVENIDO AL CHATBOT DEL TP DE DISEÑO - 2024* 
                            Para continuar, utiliza alguno de los siguientes comandos:
                                                    
                            📋 *Comandos Disponibles:*
                                                    
                            🔹 *Colaboradores:* 
                            1️⃣ `/nuevo_colaborador {nombre} {[formas]}` 
                               _Crea un nuevo colaborador con un chatID._
                               
                            1️⃣ `/datos_colaborador {colaboradorId}` 
                               _Muestra los datos de un colaborador._
                                                    
                            2️⃣ `/cambiar_formas_colaborar {colaboradorId} {[formas]}` 
                               _Cambia las formas en que un colaborador puede participar._
                                                    
                            🔹 *Logística:* 
                            3️⃣ `/nueva_ruta {colaboradorId} {heladeraIdOrigen} {heladeraIdDestino}` 
                               _Crea una nueva ruta entre dos heladeras._
                                                    
                            4️⃣ `/asignar_traslado {colaboradorId} {qrVianda} {heladeraIdOrigen} {heladeraIdDestino}` 
                               _Asigna un traslado de una vianda._
                                                    
                            5️⃣ `/modificar_traslado {colaboradorId} {idTraslado} {estadoTraslado}` 
                               _Modifica el estado de un traslado._
                                                    
                            🔹 *Incidentes:* 
                            6️⃣ `/reportar_falla_tecnica {colaboradorId} {heladeraId}` 
                               _Crea un incidente de Falla Tecnica._
                                                    
                            7️⃣ `/resolver_falla_tecnica {colaboradorId} {incidenteId} {heladeraId}` 
                               _Resuelve un incidente reportado de tipo falla tecnica._
                                                    
                            8️⃣ `/listar_incidentes_heladera {colaboradorId} {heladeraId}` 
                               _Devuelve una lista de incidentes por heladera._
                                                    
                            🔹 *Heladeras:* 
                            9️⃣ `/listar_heladeras_zona` 
                               _Devuelve una lista de heladeras._
                                                    
                            🔟 `/listar_disponibilidad_heladera` 
                               _Devuelve la disponibilidad de la heladera._
                                                    
                            1️⃣1️⃣ `/listar_retiros_diarios_heladera` 
                               _Devuelve los retiros diarios de la heladera._
                                                    
                            1️⃣2️⃣ `/subscribirse_heladera` 
                            _Para suscribirse al evento de viandas disponibles de una heladera: viandasDisponibles {idColab} {idHeladera} {nviandasdisponibles}._
                            _Para suscribirse al evento de viandas faltantes de una heladera: viandasFaltantes {idColab} {idHeladera} {nviandasfaltantes}._ 
                            _Para suscribirse al evento de desperfecto de una heladera: heladeraDesperfecto {idColab} {idHeladera}._
                                                                                                                                        
                                                    
                            1️⃣3️⃣ `/desubscribirse_heladera` 
                               _No implementado todavía._
                                                    
                            🔹 *Viandas:* 
                            1️⃣5️⃣ `/nueva_vianda {CodigoQR} {fechaelab} {estado} {Colaborarid} {heladeraID}` 
                               _Crea una nueva vianda._
                                                    
                            1️⃣6️⃣ `/depositar_vianda {colaboradorId} {HeladeraId} {ViandaQR}` 
                               _Depositar una vianda en una heladera._
                                                    
                            1️⃣7️⃣ `/retirar_vianda {colaboradorId} {ViandaQR} {Tarjeta} {HeladeraId}` 
                               _Retirar una vianda de una heladera._
                        """);
                    msg.enableMarkdown(true); // Activa el formato Markdown
                    try {
                        execute(msg);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case "/nuevo_colaborador":{
                    var nombre = String.valueOf(comando[1]);
                    var formasSTR = comando[2];
                    String sinCorchetes = formasSTR.replace("[", "").replace("]", "").trim();
                    //System.out.println(sinCorchetes);
                    // Divide por comas y convierte a una lista
                    String[] formas = sinCorchetes.split(",");
                    List<MisFormasDeColaborar> formasLista = new ArrayList<>();

                    //System.out.println(formas);

                    for (String forma : formas) {
                        formasLista.add(MisFormasDeColaborar.valueOf(forma.toUpperCase()));
                    }

                    //System.out.println(formasLista);

                    ColaboradorConChatDTO colaboradorConChatDTO = new ColaboradorConChatDTO(nombre, formasLista, chat_id);
                    fachadaColaboradores.nuevoColaborador(colaboradorConChatDTO);

                    SendMessage msg2 = new SendMessage();
                    msg2.setChatId(chat_id);
                    msg2.setText("Colaborador creado correctamente");

                    try {
                        execute(msg2);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;


                }
                case "/datos_colaborador": {
                    var id_colaborador = Long.parseLong(comando[1]);
                    ColaboradorDTO colaboradorDTO = fachadaColaboradores.getColab(id_colaborador);

                    SendMessage msg1 = new SendMessage();
                    msg1.setChatId(chat_id);
                    msg1.setText("DATOS DEL COLABORADOR: \n"
                                    + "ID: " + colaboradorDTO.getId()
                                    + "\nNOMBRE: " + colaboradorDTO.getNombre()
                                    + "\nPUNTOS: " + colaboradorDTO.getPuntos()
                                    + "\nDINERO_DONADO: " + colaboradorDTO.getDineroDonado()
                                    + "\nHELADERAS_REPARADAS: " + colaboradorDTO.getHeladerasReparadas()
                            + "\nFORMAS_DE_COLABORAR: " + colaboradorDTO.getFormas().toString()
                    );

                    try {
                        execute(msg1);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case "/cambiar_formas_colaborar": {
                    var id = Long.parseLong(comando[1]);
                    var formasSTR = comando[2];
                    String sinCorchetes = formasSTR.replace("[", "").replace("]", "").trim();
                    System.out.println(sinCorchetes);
                    // Divide por comas y convierte a una lista
                    String[] formas = sinCorchetes.split(",");
                    List<MisFormasDeColaborar> formasLista = new ArrayList<>();

                    System.out.println(formas);

                    for (String forma : formas) {
                        formasLista.add(MisFormasDeColaborar.valueOf(forma.toUpperCase()));
                    }

                    System.out.println(formasLista);

                    FormasDeColaborarDTO formasDeColaborarDTO = new FormasDeColaborarDTO();

                    formasDeColaborarDTO.setFormas(formasLista);

                    fachadaColaboradores.cambiarFormas(id, formasDeColaborarDTO);

                    SendMessage msg2 = new SendMessage();
                    msg2.setChatId(chat_id);
                    msg2.setText("Formas cambiadas correctamente");

                    try {
                        execute(msg2);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }

                // Modulo Logistica
                case "/nueva_ruta": { // {colaboradorId} {heladeraIdOrigen} {heladeraIdDestino}

                    if(comando.length != 4) {
                        SendMessage msg = new SendMessage();
                        msg.setChatId(chat_id);
                        msg.setText("Comando incorrecto. Por favor, utilice /iniciar para ver los comandos disponibles.");
                        try {
                            execute(msg);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    var colaboradorId = Long.parseLong(comando[1]);
                    var heladeraIdOrigen = Integer.parseInt(comando[2]);
                    var heladeraIdDestino = Integer.parseInt(comando[3]);

                    ColaboradorDTO colaboradorDTO = fachadaColaboradores.getColab(colaboradorId);

                    if (!colaboradorDTO.getFormas().contains(MisFormasDeColaborar.TRANSPORTADOR)) {
                        SendMessage msg = new SendMessage();
                        msg.setChatId(chat_id);
                        msg.setText("El usuario no tiene permisos para crear una ruta");
                        try {
                            execute(msg);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    fachadaLogistica.nueva_ruta(colaboradorId, heladeraIdOrigen, heladeraIdDestino);

                    SendMessage msg = new SendMessage();
                    msg.setChatId(chat_id);
                    msg.setText("Ruta creada correctamente" +
                            "\nColaborador: " + colaboradorId +
                            "\nHeladera Origen: " + heladeraIdOrigen +
                            "\nHeladera Destino: " + heladeraIdDestino);
                    try {
                        execute(msg);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case "/asignar_traslado": { // {colaboradorId} {qrVianda} {heladeraIdOrigen} {heladeraIdDestino}

                    if(comando.length != 5) {
                        SendMessage msg = new SendMessage();
                        msg.setChatId(chat_id);
                        msg.setText("Comando incorrecto. Por favor, utilice /iniciar para ver los comandos disponibles.");
                        try {
                            execute(msg);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    var colaboradorId = Long.parseLong(comando[1]);
                    var qrVianda = String.valueOf(comando[2]);
                    var heladeraIdOrigen = Integer.parseInt(comando[3]);
                    var heladeraIdDestino = Integer.parseInt(comando[4]);

                    ColaboradorDTO colaboradorDTO = fachadaColaboradores.getColab(colaboradorId);

                    if (!colaboradorDTO.getFormas().contains(MisFormasDeColaborar.TRANSPORTADOR)) {
                        SendMessage msg = new SendMessage();
                        msg.setChatId(chat_id);
                        msg.setText("El usuario no tiene permisos para asignar un traslado");
                        try {
                            execute(msg);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    TrasladoDTO traslado = fachadaLogistica.asignar_traslado(qrVianda, heladeraIdOrigen, heladeraIdDestino);

                    SendMessage msg = new SendMessage();
                    msg.setChatId(chat_id);
                    msg.setText("Traslado asignado correctamente" +
                            "\nID Traslado: " + traslado.getId() +
                            "\nQR Vianda: " + qrVianda +
                            "\nHeladera Origen: " + heladeraIdOrigen +
                            "\nHeladera Destino: " + heladeraIdDestino);
                    try {
                        execute(msg);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case "/modificar_traslado": { // {colaboradorId} {idTraslado} {estadoTraslado}

                    if(comando.length != 4) {
                        SendMessage msg = new SendMessage();
                        msg.setChatId(chat_id);
                        msg.setText("Comando incorrecto. Por favor, utilice /iniciar para ver los comandos disponibles.");
                        try {
                            execute(msg);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    var colaboradorId = Long.parseLong(comando[1]);
                    var idTraslado = Long.parseLong(comando[2]);
                    var estadpTraslado = EstadoTrasladoEnum.valueOf(comando[3]);

                    ColaboradorDTO colaboradorDTO = fachadaColaboradores.getColab(colaboradorId);

                    if (!colaboradorDTO.getFormas().contains(MisFormasDeColaborar.TRANSPORTADOR)) {
                        SendMessage msg = new SendMessage();
                        msg.setChatId(chat_id);
                        msg.setText("El usuario no tiene permisos para modificar el estado del traslado");
                        try {
                            execute(msg);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    fachadaLogistica.modificar_traslado(idTraslado, estadpTraslado);

                    SendMessage msg = new SendMessage();
                    msg.setChatId(chat_id);
                    msg.setText("Estado del traslado modificado correctamente" +
                            "\nID Traslado: " + idTraslado +
                            "\nNuevo estado Traslado: " + estadpTraslado);
                    try {
                        execute(msg);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }

                // Modulo Incidentes
                case "/reportar_falla_tecnica": { // {colaboradorId} {heladeraId}

                    if (comando.length != 3) {
                        SendMessage errorMsg = new SendMessage();
                        errorMsg.setChatId(chat_id);
                        errorMsg.setText("Uso incorrecto. Formato: /reportar_falla_tecnica {heladeraId}");
                        try {
                            execute(errorMsg);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    var heladeraId = Long.parseLong(comando[2]);
                    var colaboradorId = Long.parseLong(comando[1]);

                    ColaboradorDTO colaboradorDTO = fachadaColaboradores.getColab(colaboradorId);

                    if (!colaboradorDTO.getFormas().contains(MisFormasDeColaborar.TECNICO)) {
                        SendMessage msg = new SendMessage();
                        msg.setChatId(chat_id);
                        msg.setText("El usuario no tiene permisos para reportar la falla tecnica");
                        try {
                            execute(msg);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    fachadaIncidentes.crearIncidente(heladeraId);

                    SendMessage msg = new SendMessage();
                    msg.setChatId(chat_id);
                    msg.setText("Incidente de Falla Tecnica creado correctamente");
                    try {
                        execute(msg);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case "/resolver_falla_tecnica": { // {colaboradorId} {incidenteId} {heladeraId}

                    if(comando.length != 4) {
                        SendMessage msg = new SendMessage();
                        msg.setChatId(chat_id);
                        msg.setText("Comando incorrecto. Por favor, utilice /iniciar para ver los comandos disponibles.");
                        try {
                            execute(msg);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    var heladeraId = Long.parseLong(comando[3]);
                    var incidenteId = Long.parseLong(comando[2]);
                    var colaboradorId = Long.parseLong(comando[1]);

                    ColaboradorDTO colaboradorDTO = fachadaColaboradores.getColab(colaboradorId);

                    if (!colaboradorDTO.getFormas().contains(MisFormasDeColaborar.TECNICO)) {
                        SendMessage msg = new SendMessage();
                        msg.setChatId(chat_id);
                        msg.setText("El usuario no tiene permisos para resolver la falla tecnica");
                        try {
                            execute(msg);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    IncidenteDTO incidente = new IncidenteDTO(TipoIncidenteEnum.FALLA_TECNICA,heladeraId, EstadoIncidenteEnum.RESUELTO, false, 0, 0);

                    incidente.setId(incidenteId);

                    fachadaColaboradores.arreglarHeladera(colaboradorId,incidente);

                    SendMessage msg = new SendMessage();
                    msg.setChatId(chat_id);
                    msg.setText("El incidente ID "+ incidenteId +" ha sido resuelto correctamente");
                    try {
                        execute(msg);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case "/listar_incidentes_heladera": { // {colaboradorId} {heladeraId}

                    if (comando.length != 3) {
                        SendMessage errorMsg = new SendMessage();
                        errorMsg.setChatId(chat_id);
                        errorMsg.setText("Comando incorrecto. Por favor, utilice /iniciar para ver los comandos disponibles.");
                        try {
                            execute(errorMsg);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    var colaboradorId = Long.parseLong(comando[1]);
                    var heladeraId = Long.parseLong(comando[2]);

                    ColaboradorDTO colaboradorDTO = fachadaColaboradores.getColab(colaboradorId);

                    if (!colaboradorDTO.getFormas().contains(MisFormasDeColaborar.TECNICO)) {
                        SendMessage msg = new SendMessage();
                        msg.setChatId(chat_id);
                        msg.setText("El usuario no tiene permisos para listar los incidentes");
                        try {
                            execute(msg);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    try {
                        List<IncidenteDTO> incidentes = fachadaIncidentes.listarIncidentesPorHeladera(heladeraId);

                        if (incidentes.isEmpty()) {
                            SendMessage msg = new SendMessage();
                            msg.setChatId(chat_id);
                            msg.setText("No se encontraron incidentes para la heladera con ID " + heladeraId);
                            execute(msg);
                        } else {
                            StringBuilder incidentesTexto = new StringBuilder("Incidentes para la heladera con ID " + heladeraId + ":\n\n");
                            for (IncidenteDTO incidente : incidentes) {
                                incidentesTexto.append("ID: ").append(incidente.getId())
                                        .append("\nTipo: ").append(incidente.getTipoIncidente())
                                        .append("\nEstado: ").append(incidente.getEstadoIncidente())
                                        .append("\nExcede Temperatura: ").append(incidente.isExcedeTemperatura() ? "Sí" : "No")
                                        .append("\n\n");
                            }

                            SendMessage msg = new SendMessage();
                            msg.setChatId(chat_id);
                            msg.setText(incidentesTexto.toString());
                            execute(msg);
                        }
                    } catch (Exception e) {
                        SendMessage errorMsg = new SendMessage();
                        errorMsg.setChatId(chat_id);
                        errorMsg.setText("Error al obtener los incidentes: " + e.getMessage());
                        try {
                            execute(errorMsg);
                        } catch (TelegramApiException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    break;
                }

                // Modulo Heladeras
                case "/listar_heladeras_zona": {
                    SendMessage msg = new SendMessage();
                    msg.setChatId(chat_id);
                    List <HeladeraDTO> lista = fachadaHeladeras.getHeladeras();
                    msg.setText(lista.toString());
                    try {
                        execute(msg);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case "/listar_disponibilidad_heladera": {
                    var idHeladera = Integer.parseInt((comando[1]));
                    SendMessage msg = new SendMessage();
                    Integer cantViandas= fachadaHeladeras.getViandasDeHeladera(idHeladera);
                    msg.setChatId(chat_id);
                    msg.setText("La cantidad de viandas de la heladera: "+ idHeladera + " es : " + cantViandas);
                   try{
                        execute(msg);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case "/listar_retiros_diarios_heladera": {
                    SendMessage msg = new SendMessage();
                    var idHeladera = Integer.parseInt((comando[1]));
                    List <RetiroDTO> retirosDelDia = fachadaHeladeras.getRetirosDelDia(idHeladera);
                    msg.setChatId(chat_id);
                    msg.setText(retirosDelDia.toString());
                    try {
                        execute(msg);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case "/subscribirse_heladera": {
                    SendMessage msg = new SendMessage();
                    msg.setChatId(chat_id);
                    var tipoAlerta = (comando[1]);
                    if(Objects.equals(tipoAlerta, "viandasDisponibles"))
                    {
                        var idColaborador= Long.parseLong(comando[2]);
                        var idHeladera= Integer.parseInt(comando[3]);
                        var nviandas= Integer.parseInt(comando[4]);
                        fachadaHeladeras.suscribirViandasDisponibles(new SubscriptorDto(idColaborador,idHeladera,nviandas));
                        msg.setText("Suscrito correctamente");
                    }
                    else if(Objects.equals(tipoAlerta, "viandasFaltantes")){
                        var idColaborador= Long.parseLong(comando[2]);
                        var idHeladera= Integer.parseInt(comando[3]);
                        var nviandas= Integer.parseInt(comando[4]);
                        fachadaHeladeras.suscribirViandasFaltantes(new SubscriptorDto(idColaborador,idHeladera,nviandas));
                        msg.setText("Suscrito correctamente");
                    }

                    else if(Objects.equals(tipoAlerta, "heladeraDesperfecto")) {
                        var idColaborador = Long.parseLong(comando[2]);
                        var idHeladera = Integer.parseInt(comando[3]);
                        fachadaHeladeras.suscribirDesperfecto(new SubscriptorDesperfectoDTO(idColaborador, idHeladera));
                        msg.setText("Suscrito correctamente");
                    }
                    try {
                        execute(msg);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case "/desubscribirse_heladera": {
                    SendMessage msg = new SendMessage();
                    msg.setChatId(chat_id);
                    msg.setText("Comando no implementado");
                    try {
                        execute(msg);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case "/subscribirse_evento_heladera": {
                    SendMessage msg = new SendMessage();
                    msg.setChatId(chat_id);
                    msg.setText("Comando no implementado");
                    try {
                        execute(msg);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }

                // Modulo Viandas
                case "/nueva_vianda": { //{CodigoQR} {fechaelab} {estado} {Colaborarid} {heladeraID}

                    if(comando.length != 6) {
                        SendMessage msg = new SendMessage();
                        msg.setChatId(chat_id);
                        msg.setText("Comando incorrecto. Por favor, utilice /iniciar para ver los comandos disponibles.");
                        try {
                           execute(msg);
                        } catch (TelegramApiException e) {
                           throw new RuntimeException(e);
                        }
                        break;
                    }

                    var codigoQR = String.valueOf(comando[1]);
                    var fechaElaboracion = LocalDateTime.parse(comando[2]);
                    var estado = EstadoViandaEnum.valueOf(comando[3]);
                    var colaboradorid = Long.parseLong(comando[4]);
                    var heladeraid = Integer.parseInt(comando[5]);

                    ColaboradorDTO colaboradorDTO = fachadaColaboradores.getColab(colaboradorid);

                    if (!colaboradorDTO.getFormas().contains(MisFormasDeColaborar.DONADOR)) {
                        SendMessage msg = new SendMessage();
                        msg.setChatId(chat_id);
                        msg.setText("El usuario no tiene permisos para crear una vianda");
                        try {
                            execute(msg);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    fachadaViandas.agregar(codigoQR, fechaElaboracion, estado, colaboradorid, heladeraid);

                    SendMessage msg = new SendMessage();
                    msg.setChatId(chat_id);
                    msg.setText("Vianda creada correctamente" +
                            "\nCodigo QR: " + codigoQR +
                            "\nFecha Elaboracion: " + fechaElaboracion +
                            "\nEstado: " + estado +
                            "\ncolaboradorId: " + colaboradorid +
                            "\nHeladeraId: " + heladeraid);
                    try {
                        execute(msg);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case "/depositar_vianda": { // {colaboradorId} {heladeraId, ViandaQR}

                    if(comando.length != 4) {
                        SendMessage msg = new SendMessage();
                        msg.setChatId(chat_id);
                        msg.setText("Comando incorrecto. Por favor, utilice /iniciar para ver los comandos disponibles.");
                        try {
                            execute(msg);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    var colaboradorId = Long.parseLong(comando[1]);
                    var heladeraId = Integer.valueOf(comando[2]);
                    var qrVianda = String.valueOf(comando[3]);

                    ColaboradorDTO colaboradorDTO = fachadaColaboradores.getColab(colaboradorId);

                    if (!colaboradorDTO.getFormas().contains(MisFormasDeColaborar.TRANSPORTADOR)) {
                        SendMessage msg = new SendMessage();
                        msg.setChatId(chat_id);
                        msg.setText("El usuario no tiene permisos para depositar una vianda");
                        try {
                            execute(msg);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    System.out.println(heladeraId);
                    System.out.println(qrVianda);

                    ViandaRequest viandaRequest = new ViandaRequest(heladeraId, qrVianda);
                    fachadaHeladeras.depositar2(viandaRequest);

                    SendMessage msg = new SendMessage();
                    msg.setChatId(chat_id);
                    msg.setText("Vianda depositada correctamente.");
                    try {
                        execute(msg);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case "/retirar_vianda": { // {colaboradorId} {ViandaQR} {Tarjeta} {HeladeraId}

                    if(comando.length != 5) {
                        SendMessage msg = new SendMessage();
                        msg.setChatId(chat_id);
                        msg.setText("Comando incorrecto. Por favor, utilice /iniciar para ver los comandos disponibles.");
                        try {
                            execute(msg);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    var colaboradorId = Long.parseLong(comando[1]);
                    var qrVianda = String.valueOf(comando[2]);
                    var tarjeta = String.valueOf(comando[3]);
                    var heladeraId = Integer.parseInt(comando[4]);

                    ColaboradorDTO colaboradorDTO = fachadaColaboradores.getColab(colaboradorId);

                    if (!colaboradorDTO.getFormas().contains(MisFormasDeColaborar.TRANSPORTADOR)) {
                        SendMessage msg = new SendMessage();
                        msg.setChatId(chat_id);
                        msg.setText("El usuario no tiene permisos para retirar una vianda");
                        try {
                            execute(msg);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    RetiroDTO retiroDTO = new RetiroDTO(qrVianda, tarjeta, heladeraId);
                    fachadaHeladeras.retirar(retiroDTO);

                    SendMessage msg = new SendMessage();
                    msg.setChatId(chat_id);
                    msg.setText("Vianda retirada correctamente.");
                    try {
                        execute(msg);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }

                // Comando no reconocido
                default: {
                    SendMessage msg = new SendMessage();
                    msg.setChatId(chat_id);
                    msg.setText("Comando no reconocido. Por favor, utilice /iniciar para ver los comandos disponibles.");
                    try {
                        execute(msg);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return System.getenv("NOMBRE_BOT");
    }

    @Override
    public String getBotToken() {
        return System.getenv("TOKEN_BOT");
    }

    public static void main(String[] args) throws TelegramApiException {

        var objectMapper = createObjectMapper();

        fachadaColaboradores = new ColaboradoresProxy(objectMapper);
        fachadaIncidentes = new IncidentesProxy(objectMapper);
        fachadaLogistica = new LogisticaProxy(objectMapper);
        fachadaHeladeras = new HeladerasProxy(objectMapper);
        fachadaViandas = new ViandasProxy(objectMapper);

        final var registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        registry.config().commonTags("app", "metrics-sample");

        var env = System.getenv();

        var port = Integer.parseInt(env.getOrDefault("PORT", "8080"));

        try (var jvmGcMetrics = new JvmGcMetrics();
             var jvmHeapPressureMetrics = new JvmHeapPressureMetrics()) {
            jvmGcMetrics.bindTo(registry);
            jvmHeapPressureMetrics.bindTo(registry);
        }
        new JvmMemoryMetrics().bindTo(registry);
        new ProcessorMetrics().bindTo(registry);
        new FileDescriptorMetrics().bindTo(registry);

        final var micrometerPlugin = new MicrometerPlugin(config -> config.registry = registry);

        var app = Javalin.create(config -> { config.registerPlugin(micrometerPlugin); }).start(port);

        // Endpoint para obtener las métricas
        app.get("/metrics", ctx -> {
            var auth = ctx.header("Authorization");
            if (auth != null && auth.equals("Bearer " + env.get("TOKEN_METRICAS"))) {
                ctx.contentType("text/plain; version=0.0.4")
                        .result(registry.scrape());
            } else {
                ctx.status(401).json("{\"error\": \"Unauthorized access\"}");
            }
        });

        // Manejador global para todas las excepciones
        app.exception(Exception.class, (e, ctx) -> {

            // Header del content type
            ctx.contentType("application/json");

            // Codigo de estado HTTP
            if (e instanceof TrasladoNoAsignableException) {
                ctx.status(400); // Bad Request
            } else if (e instanceof NoSuchElementException) {
                ctx.status(404); // Not Found
            } else {
                ctx.status(500); // Internal Server Error
            }

            // Cuerpo de la respuesta
            ctx.result("{\"error\": \"" + e.getMessage() + "\"}");
        });

        // Se crea un nuevo Bot API
        final TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            // Se registra el bot
            telegramBotsApi.registerBot(new WebApp());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public static ObjectMapper createObjectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        var sdf = new SimpleDateFormat(Constants.DEFAULT_SERIALIZATION_FORMAT, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        objectMapper.setDateFormat(sdf);
        return objectMapper;
    }
}