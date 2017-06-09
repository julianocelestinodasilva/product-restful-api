package br.com.productrestfulapi.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Created by juliano on 09/06/17.
 */
public class ProductRepository {

    private EntityManager em;

    public ProductRepository(EntityManager em) {
        this.em = em;
    }

    public void delete(long id) {
        Product product = em.find(Product.class, id);
        em.getTransaction().begin();
        em.remove(product);
        em.getTransaction().commit();
        shutdown(); // TODO Nao usar shutdown
    }

    private void shutdown() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.createNativeQuery("SHUTDOWN").executeUpdate();
        em.close();
    }
}
