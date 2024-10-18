package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoTrasladoEnum;
import ar.edu.utn.dds.k3003.model.Incidente;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

public class IncidenteRepository {
    private final EntityManager entityManager;

    public IncidenteRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public Incidente save(Incidente traslado) {

        if (Objects.isNull(traslado.getId())) {
            entityManager.getTransaction().begin();
            entityManager.persist(traslado);
            entityManager.getTransaction().commit();
        }

        return traslado;
    }

    public Incidente findById(Long id) throws NoSuchElementException {

        Incidente incidente = entityManager.find(Incidente.class, id);

        if (Objects.isNull(incidente)) {
            throw new NoSuchElementException("No hay un incidente de id: " + id);
        }

        return incidente;
    }

    /*
    public void modificarEstado(Long idTraslado, EstadoTrasladoEnum estado) {

        entityManager.getTransaction().begin();
        Traslado traslado = this.findById(idTraslado);
        traslado.setEstado(estado);
        entityManager.merge(traslado);
        entityManager.getTransaction().commit();
    }
    */

    public void borrarTodo() {
        entityManager.getTransaction().begin();
        entityManager.createQuery("DELETE FROM Incidente").executeUpdate();
        entityManager.getTransaction().commit();
    }
}
