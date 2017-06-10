package br.com.productrestfulapi.model;

import com.sun.javaws.exceptions.InvalidArgumentException;

import javax.persistence.EntityManager;

/**
 * Created by juliano on 09/06/17.
 */
public class ProductRepository {

    private EntityManager em;

    public ProductRepository(EntityManager em) {
        this.em = em;
    }

    /*http://www.objectdb.com/java/jpa/persistence/retrieve
    A similar method, getReference, can be considered the lazy version of find:
    Employee employee = em.getReference(Employee.class, 1);*/

    public void create(Product product) throws IllegalArgumentException {
        if (product == null) {
            throw new IllegalArgumentException("Product was not informed !");
        }
        if (product.getName() == null || product.getName().isEmpty()
                || product.getDescription() == null || product.getDescription().isEmpty()) {
            throw new IllegalArgumentException("name or description was not informed !");
        }
        em.getTransaction().begin();
        em.persist(product);
        em.getTransaction().commit();
    }

    public boolean delete(long id) {
        Product product = em.find(Product.class, id);
        if (product == null) {
            return false;
        }
        em.getTransaction().begin();
        em.remove(product);
        em.getTransaction().commit();
        return true;
    }
}