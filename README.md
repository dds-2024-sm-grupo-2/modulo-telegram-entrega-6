# Entrega 5

## Módulo Incidentes

### Metricas
```
https://moduloincidentes.grafana.net/public-dashboards/d8e6299427524a8dbd9a433c7883c861
```

### API REST
```
https://modulo-incidentes-entrega-5-busv.onrender.com
```
Se implementan los siguientes endpoints:

- POST /incidentes 
    - Request body: JSON con los datos del incidente a crear.
      -  ALERTA_FRAUDE
        ```
        {
          "heladeraId": 1,
          "tipoIncidente": "ALERTA_FRAUDE"
        }
        ```
      -  ALERTA_TEMPERATURA
        ```
        {
          "heladeraId": 1,
          "tipoIncidente": "ALERTA_TEMPERATURA",
          "excedeTemperatura": true,
          "excesoTemperatura": 5
        }
        ```
      - ALERTA_FALLA_CONEXION
      ```
      {
        "heladeraId": 1,
        "tipoIncidente": "ALERTA_FALLA_CONEXION",
        "tiempoSinRespuesta": 1
      }
      ```
      - FALLA_TECNICA
      ```
      {
          "heladeraId": 1,
          "tipoIncidente": "FALLA_TECNICA"
      }
      ```
  - Response body: JSON con los datos del incidente creado.
    ```
    {
     "id": 1,
     "estado": "ACTIVO", // o "RESUELTO", cuando se resuelve el incidente
     "heladeraId": 1,
     "tipo": "ALERTA_TEMPERATURA",
     "excede": true,
     "unidades": 5,
     "tiempoSinRespuesta": null,
     "fecha": "2024-10-11T12:00:00"
    }
    ```
- GET /incidentes/{id}
    - Response body: JSON con los datos del incidente solicitado.
  ```
  {
    "id": 1,
    "estado": "ACTIVO",
    "heladeraId": 1,
    "tipo": "ALERTA_TEMPERATURA",
    "excede": true,
    "unidades": 5,
    "tiempoSinRespuesta": null,
    "fecha": "2024-10-11T12:00:00"
  }
  ```
- DELETE /incidentes
    - Response body: JSON con los datos de los incidentes eliminados.
    ```
    {
      message": "Se eliminaron todos los incidentes"
    }
    ```
- GET /metrics
    - Response body: JSON con las métricas de los incidentes.
    ```
    # HELP app_rutas_creadas_total Numero de rutas creadas
    # TYPE app_rutas_creadas_total counter
    app_rutas_creadas_total{app="metrics-sample"} 0.0
    # HELP app_traslados_asignados_total Numero de traslados asignados
    # TYPE app_traslados_asignados_total counter
    app_traslados_asignados_total{app="metrics-sample"} 0.0
    # HELP app_traslados_depositados_total Numero de traslados depositados
    # TYPE app_traslados_depositados_total counter
    app_traslados_depositados_total{app="metrics-sample"} 0.0
    # HELP app_traslados_retirados_total Numero de traslados retirados
    # TYPE app_traslados_retirados_total counter
    app_traslados_retirados_total{app="metrics-sample"} 0.0
    # HELP jetty_connections_bytes_in_bytes Bytes received by tracked connections
    # TYPE jetty_connections_bytes_in_bytes summary
    jetty_connections_bytes_in_bytes_count{app="metrics-sample",connector_name="unnamed"} 8
    jetty_connections_bytes_in_bytes_sum{app="metrics-sample",connector_name="unnamed"} 196.0
    # HELP jetty_connections_bytes_in_bytes_max Bytes received by tracked connections
    # TYPE jetty_connections_bytes_in_bytes_max gauge
    jetty_connections_bytes_in_bytes_max{app="metrics-sample",connector_name="unnamed"} 98.0
    # HELP jetty_connections_bytes_out_bytes Bytes sent by tracked connections
    # TYPE jetty_connections_bytes_out_bytes summary
    jetty_connections_bytes_out_bytes_count{app="metrics-sample",connector_name="unnamed"} 8
    jetty_connections_bytes_out_bytes_sum{app="metrics-sample",connector_name="unnamed"} 210.0
    # HELP jetty_connections_bytes_out_bytes_max Bytes sent by tracked connections
    # TYPE jetty_connections_bytes_out_bytes_max gauge
    jetty_connections_bytes_out_bytes_max{app="metrics-sample",connector_name="unnamed"} 109.0
    # HELP jetty_connections_current_connections The current number of open Jetty connections
    # TYPE jetty_connections_current_connections gauge
    jetty_connections_current_connections{app="metrics-sample",connector_name="unnamed"} 2.0
    # HELP jetty_connections_max_connections The maximum number of observed connections over a rolling 2-minute interval
    # TYPE jetty_connections_max_connections gauge
    jetty_connections_max_connections{app="metrics-sample",connector_name="unnamed"} 4.0
    # HELP jetty_connections_messages_in_messages_total Messages received by tracked connections
    # TYPE jetty_connections_messages_in_messages_total counter
    jetty_connections_messages_in_messages_total{app="metrics-sample",connector_name="unnamed"} 2.0
    # HELP jetty_connections_messages_out_messages_total Messages sent by tracked connections
    # TYPE jetty_connections_messages_out_messages_total counter
    jetty_connections_messages_out_messages_total{app="metrics-sample",connector_name="unnamed"} 2.0
    # HELP jetty_connections_request_seconds Jetty client or server requests
    # TYPE jetty_connections_request_seconds summary
    jetty_connections_request_seconds_count{app="metrics-sample",connector_name="unnamed",type="server"} 8
    jetty_connections_request_seconds_sum{app="metrics-sample",connector_name="unnamed",type="server"} 1.60266693
    # HELP jetty_connections_request_seconds_max Jetty client or server requests
    # TYPE jetty_connections_request_seconds_max gauge
    jetty_connections_request_seconds_max{app="metrics-sample",connector_name="unnamed",type="server"} 0.799663768
    # HELP jetty_server_async_dispatches_total Asynchronous dispatches
    # TYPE jetty_server_async_dispatches_total counter
    jetty_server_async_dispatches_total{app="metrics-sample"} 0.0
    # HELP jetty_server_async_expires_total Asynchronous operations that timed out before completing
    # TYPE jetty_server_async_expires_total counter
    jetty_server_async_expires_total{app="metrics-sample"} 0.0
    # HELP jetty_server_async_waits_operations Pending asynchronous wait operations
    # TYPE jetty_server_async_waits_operations gauge
    jetty_server_async_waits_operations{app="metrics-sample"} 0.0
    # HELP jetty_server_dispatches_open_seconds Jetty dispatches that are currently in progress
    # TYPE jetty_server_dispatches_open_seconds summary
    jetty_server_dispatches_open_seconds_count{app="metrics-sample"} 1
    jetty_server_dispatches_open_seconds_sum{app="metrics-sample"} 0.005397458
    # HELP jetty_server_dispatches_open_seconds_max Jetty dispatches that are currently in progress
    # TYPE jetty_server_dispatches_open_seconds_max gauge
    jetty_server_dispatches_open_seconds_max{app="metrics-sample"} 0.005410659
    # HELP jetty_server_requests_seconds HTTP requests to the Jetty server
    # TYPE jetty_server_requests_seconds summary
    jetty_server_requests_seconds_count{app="metrics-sample",exception="Unknown",method="GET",outcome="SUCCESS",status="200",uri="/metrics"} 1
    jetty_server_requests_seconds_sum{app="metrics-sample",exception="Unknown",method="GET",outcome="SUCCESS",status="200",uri="/metrics"} 0.514771081
    jetty_server_requests_seconds_count{app="metrics-sample",exception="Unknown",method="GET",outcome="SUCCESS",status="200",uri="root"} 1
    jetty_server_requests_seconds_sum{app="metrics-sample",exception="Unknown",method="GET",outcome="SUCCESS",status="200",uri="root"} 0.001714104
    jetty_server_requests_seconds_count{app="metrics-sample",exception="Unknown",method="HEAD",outcome="CLIENT_ERROR",status="404",uri="NOT_FOUND"} 1
    jetty_server_requests_seconds_sum{app="metrics-sample",exception="Unknown",method="HEAD",outcome="CLIENT_ERROR",status="404",uri="NOT_FOUND"} 0.402292009
    jetty_server_requests_seconds_count{app="metrics-sample",exception="Unknown",method="HEAD",outcome="SUCCESS",status="200",uri="NOT_FOUND"} 2
    jetty_server_requests_seconds_sum{app="metrics-sample",exception="Unknown",method="HEAD",outcome="SUCCESS",status="200",uri="NOT_FOUND"} 0.397403461
    # HELP jetty_server_requests_seconds_max HTTP requests to the Jetty server
    # TYPE jetty_server_requests_seconds_max gauge
    jetty_server_requests_seconds_max{app="metrics-sample",exception="Unknown",method="GET",outcome="SUCCESS",status="200",uri="/metrics"} 0.514771081
    jetty_server_requests_seconds_max{app="metrics-sample",exception="Unknown",method="GET",outcome="SUCCESS",status="200",uri="root"} 0.001714104
    jetty_server_requests_seconds_max{app="metrics-sample",exception="Unknown",method="HEAD",outcome="CLIENT_ERROR",status="404",uri="NOT_FOUND"} 0.402292009
    jetty_server_requests_seconds_max{app="metrics-sample",exception="Unknown",method="HEAD",outcome="SUCCESS",status="200",uri="NOT_FOUND"} 0.395600021
    # HELP jetty_threads_busy The number of busy threads in the pool
    # TYPE jetty_threads_busy gauge
    jetty_threads_busy{app="metrics-sample"} 3.0
    # HELP jetty_threads_config_max The maximum number of threads in the pool
    # TYPE jetty_threads_config_max gauge
    jetty_threads_config_max{app="metrics-sample"} 250.0
    # HELP jetty_threads_config_min The minimum number of threads in the pool
    # TYPE jetty_threads_config_min gauge
    jetty_threads_config_min{app="metrics-sample"} 8.0
    # HELP jetty_threads_current The total number of threads in the pool
    # TYPE jetty_threads_current gauge
    jetty_threads_current{app="metrics-sample"} 8.0
    # HELP jetty_threads_idle The number of idle threads in the pool
    # TYPE jetty_threads_idle gauge
    jetty_threads_idle{app="metrics-sample"} 4.0
    # HELP jetty_threads_jobs Number of jobs queued waiting for a thread
    # TYPE jetty_threads_jobs gauge
    jetty_threads_jobs{app="metrics-sample"} 0.0
    # HELP jvm_buffer_count_buffers An estimate of the number of buffers in the pool
    # TYPE jvm_buffer_count_buffers gauge
    jvm_buffer_count_buffers{app="metrics-sample",id="direct"} 8.0
    jvm_buffer_count_buffers{app="metrics-sample",id="mapped"} 0.0
    jvm_buffer_count_buffers{app="metrics-sample",id="mapped - 'non-volatile memory'"} 0.0
    # HELP jvm_buffer_memory_used_bytes An estimate of the memory that the Java virtual machine is using for this buffer pool
    # TYPE jvm_buffer_memory_used_bytes gauge
    jvm_buffer_memory_used_bytes{app="metrics-sample",id="direct"} 90112.0
    jvm_buffer_memory_used_bytes{app="metrics-sample",id="mapped"} 0.0
    jvm_buffer_memory_used_bytes{app="metrics-sample",id="mapped - 'non-volatile memory'"} 0.0
    # HELP jvm_buffer_total_capacity_bytes An estimate of the total capacity of the buffers in this pool
    # TYPE jvm_buffer_total_capacity_bytes gauge
    jvm_buffer_total_capacity_bytes{app="metrics-sample",id="direct"} 90112.0
    jvm_buffer_total_capacity_bytes{app="metrics-sample",id="mapped"} 0.0
    jvm_buffer_total_capacity_bytes{app="metrics-sample",id="mapped - 'non-volatile memory'"} 0.0
    # HELP jvm_gc_live_data_size_bytes Size of long-lived heap memory pool after reclamation
    # TYPE jvm_gc_live_data_size_bytes gauge
    jvm_gc_live_data_size_bytes{app="metrics-sample"} NaN
    # HELP jvm_gc_max_data_size_bytes Max size of long-lived heap memory pool
    # TYPE jvm_gc_max_data_size_bytes gauge
    jvm_gc_max_data_size_bytes{app="metrics-sample"} NaN
    # HELP jvm_gc_memory_allocated_bytes_total Incremented for an increase in the size of the (young) heap memory pool after one GC to before the next
    # TYPE jvm_gc_memory_allocated_bytes_total counter
    jvm_gc_memory_allocated_bytes_total{app="metrics-sample"} 0.0
    # HELP jvm_gc_memory_promoted_bytes_total Count of positive increases in the size of the old generation memory pool before GC to after GC
    # TYPE jvm_gc_memory_promoted_bytes_total counter
    jvm_gc_memory_promoted_bytes_total{app="metrics-sample"} 0.0
    # HELP jvm_gc_overhead_percent An approximation of the percent of CPU time used by GC activities over the last lookback period or since monitoring began, whichever is shorter, in the range [0..1]
    # TYPE jvm_gc_overhead_percent gauge
    jvm_gc_overhead_percent{app="metrics-sample"} 0.0
    # HELP jvm_memory_committed_bytes The amount of memory in bytes that is committed for the Java virtual machine to use
    # TYPE jvm_memory_committed_bytes gauge
    jvm_memory_committed_bytes{app="metrics-sample",area="heap",id="Eden Space"} 6356992.0
    jvm_memory_committed_bytes{app="metrics-sample",area="heap",id="Survivor Space"} 786432.0
    jvm_memory_committed_bytes{app="metrics-sample",area="heap",id="Tenured Gen"} 1.5777792E7
    jvm_memory_committed_bytes{app="metrics-sample",area="nonheap",id="CodeHeap 'non-nmethods'"} 2555904.0
    jvm_memory_committed_bytes{app="metrics-sample",area="nonheap",id="CodeHeap 'non-profiled nmethods'"} 2555904.0
    jvm_memory_committed_bytes{app="metrics-sample",area="nonheap",id="CodeHeap 'profiled nmethods'"} 6553600.0
    jvm_memory_committed_bytes{app="metrics-sample",area="nonheap",id="Compressed Class Space"} 5898240.0
    jvm_memory_committed_bytes{app="metrics-sample",area="nonheap",id="Metaspace"} 4.4695552E7
    # HELP jvm_memory_max_bytes The maximum amount of memory in bytes that can be used for memory management
    # TYPE jvm_memory_max_bytes gauge
    jvm_memory_max_bytes{app="metrics-sample",area="heap",id="Eden Space"} 3.5782656E7
    jvm_memory_max_bytes{app="metrics-sample",area="heap",id="Survivor Space"} 4456448.0
    jvm_memory_max_bytes{app="metrics-sample",area="heap",id="Tenured Gen"} 8.9522176E7
    jvm_memory_max_bytes{app="metrics-sample",area="nonheap",id="CodeHeap 'non-nmethods'"} 5828608.0
    jvm_memory_max_bytes{app="metrics-sample",area="nonheap",id="CodeHeap 'non-profiled nmethods'"} 1.22916864E8
    jvm_memory_max_bytes{app="metrics-sample",area="nonheap",id="CodeHeap 'profiled nmethods'"} 1.22912768E8
    jvm_memory_max_bytes{app="metrics-sample",area="nonheap",id="Compressed Class Space"} 1.073741824E9
    jvm_memory_max_bytes{app="metrics-sample",area="nonheap",id="Metaspace"} -1.0
    # HELP jvm_memory_usage_after_gc_percent The percentage of long-lived heap pool used after the last GC event, in the range [0..1]
    # TYPE jvm_memory_usage_after_gc_percent gauge
    jvm_memory_usage_after_gc_percent{app="metrics-sample",area="heap",pool="long-lived"} 0.0
    # HELP jvm_memory_used_bytes The amount of used memory
    # TYPE jvm_memory_used_bytes gauge
    jvm_memory_used_bytes{app="metrics-sample",area="heap",id="Eden Space"} 4919768.0
    jvm_memory_used_bytes{app="metrics-sample",area="heap",id="Survivor Space"} 658144.0
    jvm_memory_used_bytes{app="metrics-sample",area="heap",id="Tenured Gen"} 1.3045824E7
    jvm_memory_used_bytes{app="metrics-sample",area="nonheap",id="CodeHeap 'non-nmethods'"} 1371264.0
    jvm_memory_used_bytes{app="metrics-sample",area="nonheap",id="CodeHeap 'non-profiled nmethods'"} 1912320.0
    jvm_memory_used_bytes{app="metrics-sample",area="nonheap",id="CodeHeap 'profiled nmethods'"} 6496896.0
    jvm_memory_used_bytes{app="metrics-sample",area="nonheap",id="Compressed Class Space"} 5739152.0
    jvm_memory_used_bytes{app="metrics-sample",area="nonheap",id="Metaspace"} 4.4390112E7
    # HELP process_cpu_usage The "recent cpu usage" for the Java Virtual Machine process
    # TYPE process_cpu_usage gauge
    process_cpu_usage{app="metrics-sample"} 0.5238095238095238
    # HELP process_files_max_files The maximum file descriptor count
    # TYPE process_files_max_files gauge
    process_files_max_files{app="metrics-sample"} 1048576.0
    # HELP process_files_open_files The open file descriptor count
    # TYPE process_files_open_files gauge
    process_files_open_files{app="metrics-sample"} 14.0
    # HELP system_cpu_count The number of processors available to the Java virtual machine
    # TYPE system_cpu_count gauge
    system_cpu_count{app="metrics-sample"} 1.0
    # HELP system_cpu_usage The "recent cpu usage" of the system the application is running in
    # TYPE system_cpu_usage gauge
    system_cpu_usage{app="metrics-sample"} 0.510547619047619
    # HELP system_load_average_1m The sum of the number of runnable entities queued to available processors and the number of runnable entities running on the available processors averaged over a period of time
    # TYPE system_load_average_1m gauge
    system_load_average_1m{app="metrics-sample"} 6.21
    ```
<hr>
<div id="footer" align="center">
  <a href="https://www.frba.utn.edu.ar/">
  <img src="https://github.com/ddsutn-k3003/2024-tp-entrega-2-kenzogrosvald/assets/94919997/f35d82b8-fd1a-435a-b1d9-3aad2785b732" style="width:70%; height:auto;">
  </a>
</div>
