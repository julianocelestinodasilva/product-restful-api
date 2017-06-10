package br.com.productrestfulapi.utils;

import br.com.productrestfulapi.model.Product;
import br.com.productrestfulapi.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by juliano on 10/06/17.
 */
public class DataBaseUtils {

    public static void assertProductWasUpdated(String newName) throws IOException {
        List<Product> results = getProductsDataBase();
        assertNotNull(results);
        assertEquals(1,results.size());
        Product productDataBase = results.get(0);
        assertEquals(newName,productDataBase.getName());
    }

    public static  void assertProductWasCreated(Product product) throws IOException {
        List<Product> results = getProductsDataBase();
        assertNotNull(results);
        assertEquals(1,results.size());
        Product productDataBase = results.get(0);
        assertEquals(product.getName(),productDataBase.getName());
        assertEquals(product.getDescription(),productDataBase.getDescription());
        assertEquals(product.getImages().get(0),productDataBase.getImages().get(0));
    }

    public static void persistProductsDataBase(Object product) throws IOException {
        EntityManager em = JPAUtil.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("DELETE FROM Image").executeUpdate();
        em.createNativeQuery("DELETE FROM Product").executeUpdate();
        em.persist(product);
        em.flush();
        em.getTransaction().commit();
    }

    public static void deleteProductsDataBase() throws IOException {
        EntityManager em = JPAUtil.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("DELETE FROM Image").executeUpdate();
        em.createNativeQuery("DELETE FROM Product").executeUpdate();
        em.flush();
        em.getTransaction().commit();
    }

    public static long persistProductsDataBaseAndGetId(Product product) throws IOException {
        EntityManager em = JPAUtil.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("DELETE FROM Image").executeUpdate();
        em.createNativeQuery("DELETE FROM Product").executeUpdate();
        em.persist(product);
        em.flush();
        em.getTransaction().commit();
        List<Product> results = getProductsDataBase();
        return results.get(0).getId();
    }

    private static List<Product> getProductsDataBase() throws IOException {
        EntityManager em = JPAUtil.createEntityManager();
        Query query = em.createQuery("SELECT c FROM Product c");
        return (List<Product>) query.getResultList();
    }
}
