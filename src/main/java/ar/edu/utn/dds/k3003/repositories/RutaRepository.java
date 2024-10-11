package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.Ruta;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Setter
@Getter
public class RutaRepository {
    private final EntityManager entityManager;

    public RutaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Ruta save(Ruta ruta) throws NoSuchElementException {

        if (Objects.isNull(ruta.getId())) {
            entityManager.getTransaction().begin();
            entityManager.persist(ruta);
            entityManager.getTransaction().commit();
        }

        return ruta;
    }

    public Ruta findById(Long id) throws NoSuchElementException {

        Ruta ruta = entityManager.find(Ruta.class, id);

        if (Objects.isNull(ruta)){
            throw new NoSuchElementException("No hay una ruta de id: " + id);
        }

        return ruta;
    }

    public List<Ruta> findByHeladeras(Integer heladeraOrigen, Integer heladeraDestino) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ruta> criteriaQuery = criteriaBuilder.createQuery(Ruta.class);
        Root<Ruta> root = criteriaQuery.from(Ruta.class);

        criteriaQuery.select(root).where(
                criteriaBuilder.equal(root.get("heladeraIdOrigen"), heladeraOrigen),
                criteriaBuilder.equal(root.get("heladeraIdDestino"), heladeraDestino)
        );

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    public void borrarTodo() {
        entityManager.getTransaction().begin();
        try {
            int deletedCount = entityManager.createQuery("DELETE FROM Ruta").executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }
}