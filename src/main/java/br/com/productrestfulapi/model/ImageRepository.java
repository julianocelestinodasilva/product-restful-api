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

    public void create(Image image) {
        verifyFieldsNotNull(image);
        em.getTransaction().begin();
        em.persist(image);
        em.getTransaction().commit();
    }

    public boolean delete(long id) {
        Image image = em.find(Image.class, id);
        if (image == null) {
            return false;
        }
        em.getTransaction().begin();
        em.remove(image);
        em.getTransaction().commit();
        return true;
    }

    private void verifyFieldsNotNull(Image image) {
        if (image == null) {
            throw new IllegalArgumentException(Image.IMAGE_WAS_NOT_INFORMED);
        }
        if (image.getProduct() == null || image.getProduct().getId() < 1) {
            throw new IllegalArgumentException(Image.PRODUCT_WAS_NOT_INFORMED);
        }
    }
}