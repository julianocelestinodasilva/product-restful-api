package br.com.productrestfulapi.model;

import javax.persistence.EntityManager;

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
    }
}