package br.com.productrestfulapi.utils;

import br.com.productrestfulapi.model.Image;
import br.com.productrestfulapi.model.Product;
import br.com.productrestfulapi.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by juliano on 10/06/17.
 */
public class DataBaseUtils {

    public static List<Product> persistProductsWithRelationships() throws IOException {
        List<Product> productsWithRelationships = new ArrayList<Product>();
        Product productWithImages = new Product("ProductWithImages", "Product with images");
        List<Image> images = new ArrayList<Image>();
        images.add(new Image(productWithImages));
        productWithImages.setImages(images);
        productsWithRelationships.add(productWithImages);
        Product productWithParent = new Product("ProductWithParent", "Product with parent",productWithImages);
        productsWithRelationships.add(productWithParent);
        EntityManager em = JPAUtil.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("DELETE FROM Image").executeUpdate();
        em.createNativeQuery("DELETE FROM Product").executeUpdate();
        em.persist(productWithImages);
        em.persist(productWithParent);
        em.flush();
        em.getTransaction().commit();
        return productsWithRelationships;
    }

    public static void assertProductWasUpdated(String newName) throws IOException {
        List<Product> results = getProducts();
        assertNotNull(results);
        assertEquals(1,results.size());
        Product productDataBase = results.get(0);
        assertEquals(newName,productDataBase.getName());
    }

    public static  void assertProductWasCreated(Product product) throws IOException {
        List<Product> results = getProducts();
        assertNotNull(results);
        assertEquals(1,results.size());
        Product productDataBase = results.get(0);
        assertEquals(product.getName(),productDataBase.getName());
        assertEquals(product.getDescription(),productDataBase.getDescription());
        if (product.getImages() != null && product.getImages().size() > 0 ) {
            assertEquals(product.getImages().get(0), productDataBase.getImages().get(0));
        }
    }

    public static void persistProducts(Product product) throws IOException {
        EntityManager em = JPAUtil.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("DELETE FROM Image").executeUpdate();
        em.createNativeQuery("DELETE FROM Product").executeUpdate();
        em.persist(product);
        em.flush();
        em.getTransaction().commit();
    }

    public static void deleteProducts() throws IOException {
        EntityManager em = JPAUtil.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("DELETE FROM Image").executeUpdate();
        em.createNativeQuery("DELETE FROM Product").executeUpdate();
        em.flush();
        em.getTransaction().commit();
    }

    public static long persistProductsAndGetId(Product product) throws IOException {
        EntityManager em = JPAUtil.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("DELETE FROM Image").executeUpdate();
        em.createNativeQuery("DELETE FROM Product").executeUpdate();
        em.persist(product);
        em.flush();
        em.getTransaction().commit();
        List<Product> results = getProducts();
        return results.get(0).getId();
    }

    private static List<Product> getProducts() throws IOException {
        EntityManager em = JPAUtil.createEntityManager();
        Query query = em.createQuery("SELECT c FROM Product c");
        return (List<Product>) query.getResultList();
    }
}
