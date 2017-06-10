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

    /*http://www.objectdb.com/java/jpa/persistence/retrieve
    A similar method, getReference, can be considered the lazy version of find:
    Employee employee = em.getReference(Employee.class, 1);*/

    public void update(Product product) throws IllegalArgumentException {
        verifyFieldsNotNull(product);
        em.getTransaction().begin();
        em.merge(product);
        em.getTransaction().commit();
    }

    public void create(Product product) throws IllegalArgumentException {
        verifyFieldsNotNull(product);
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

    private void verifyFieldsNotNull(Product product) throws IllegalArgumentException {
        if (product == null) {
            throw new IllegalArgumentException(Product.PRODUCT_WAS_NOT_INFORMED);
        }
        if (product.getName() == null || product.getName().isEmpty()
                || product.getDescription() == null || product.getDescription().isEmpty()) {
            throw new IllegalArgumentException(Product.NAME_OR_DESCRIPTION_WAS_NOT_INFORMED);
        }
    }
}