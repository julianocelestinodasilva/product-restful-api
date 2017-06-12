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
import static org.junit.Assert.assertTrue;

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

    public static  void assertWasCreated(Product product) throws IOException {
        List<Product> results = getProducts();
        assertNotNull(results);
        assertEquals(1,results.size());
        Product productDataBase = results.get(0);
        assertEquals(product.getName(),productDataBase.getName());
        assertEquals(product.getDescription(),productDataBase.getDescription());
        Product parent = product.getParent();
        if (parent != null) {
            assertTrue(parent.getId() == productDataBase.getParent().getId());
        }
    }

    public static  void assertWasCreated(Image image) throws IOException {
        List<Image> results = getImages();
        assertNotNull(results);
        assertEquals(1,results.size());
        Image imageDataBase = results.get(0);
        assertEquals(image.getProduct().getId(),imageDataBase.getProduct().getId());
    }

    public static void persist(Product product) throws IOException {
        EntityManager em = JPAUtil.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("DELETE FROM Image").executeUpdate();
        em.createNativeQuery("DELETE FROM Product").executeUpdate();
        em.persist(product);
        em.flush();
        em.getTransaction().commit();
    }

    public static Image persistImageAndProduct(Product product) throws IOException {
        EntityManager em = JPAUtil.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("DELETE FROM Image").executeUpdate();
        em.createNativeQuery("DELETE FROM Product").executeUpdate();
        final long id = persistProductAndGetId(product,em);
        product.setId(id);
        final Image image = new Image(product);
        em.persist(image);
        em.flush();
        em.getTransaction().commit();
        return image;
    }

    public static void deleteEntities() throws IOException {
        EntityManager em = JPAUtil.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("DELETE FROM Image").executeUpdate();
        em.createNativeQuery("DELETE FROM Product").executeUpdate();
        em.flush();
        em.getTransaction().commit();
    }

    public static long deleteEntitiesAndPersistProductAndGetId(Product product) throws IOException {
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

    private static long persistProductAndGetId(Product product,EntityManager em) throws IOException {
        em.persist(product);
        em.flush();
        List<Product> results = getProducts(em);
        return results.get(0).getId();
    }

    private static List<Product> getProducts() throws IOException {
        EntityManager em = JPAUtil.createEntityManager();
        Query query = em.createQuery("SELECT c FROM Product c");
        return (List<Product>) query.getResultList();
    }

    private static List<Product> getProducts(EntityManager em) throws IOException {
        Query query = em.createQuery("SELECT c FROM Product c");
        return (List<Product>) query.getResultList();
    }

    private static List<Image> getImages() throws IOException {
        EntityManager em = JPAUtil.createEntityManager();
        Query query = em.createQuery("SELECT c FROM Image c");
        return (List<Image>) query.getResultList();
    }
}
