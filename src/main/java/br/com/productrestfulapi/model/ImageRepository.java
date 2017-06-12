package br.com.productrestfulapi.model;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by juliano on 09/06/17.
 */
public class ImageRepository {

    private EntityManager em;

    public ImageRepository(EntityManager em) {
        this.em = em;
    }

    public Product get(long id) {
        return em.find(Product.class,id);
    }

    /*public List<Product> get() {
        CriteriaQuery<Product> criteria = em.getCriteriaBuilder().createQuery(Product.class);
        Root<Product> root = criteria.from(Product.class);
        criteria.select(root);
        return em.createQuery(criteria).getResultList();
    }*/

    /*public void update(Product product) {
        verifyFieldsNotNull(product);
        em.getTransaction().begin();
        em.merge(product);
        em.getTransaction().commit();
    }*/

    public void create(Image image) {
        verifyFieldsNotNull(image);
        em.getTransaction().begin();
        em.persist(image);
        em.getTransaction().commit();
    }

    /*public boolean delete(long id) {
        Product product = em.find(Product.class, id);
        if (product == null) {
            return false;
        }
        em.getTransaction().begin();
        em.remove(product);
        em.getTransaction().commit();
        return true;
    }*/

    private void verifyFieldsNotNull(Image image) {
        if (image == null) {
            throw new IllegalArgumentException(Image.IMAGE_WAS_NOT_INFORMED);
        }
        if (image.getProduct() == null || image.getProduct().getId() < 1) {
            throw new IllegalArgumentException(Image.PRODUCT_WAS_NOT_INFORMED);
        }
    }
}