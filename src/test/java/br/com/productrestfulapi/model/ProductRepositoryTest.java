package br.com.productrestfulapi.model;

import br.com.productrestfulapi.util.JPAUtil;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by juliano on 09/06/17.
 */
public class ProductRepositoryTest {

    private EntityManager em;

    private Product product;
    private List<Image> imagesProduct;
    private ProductRepository repository;

    @Before
    public void setUp() throws Exception {
        repository = new ProductRepository(JPAUtil.createEntityManager());
    }

    @Test
    public void should_create_product() throws Exception {
        clearProductsDataBase();
        createProductWithImages();
        repository.create(product);
        assertProductWasCreated();
    }

    @Test
    public void should_delete_product() throws Exception {
        persistProductsDataBase();
        final long productId = product.getId();
        repository.delete(productId);
        em = JPAUtil.createEntityManager();
        assertNull(em.find(Product.class, productId));
    }

    private void assertProductWasCreated() throws IOException {
        em = JPAUtil.createEntityManager();
        Query query = em.createQuery("SELECT c FROM Product c");
        List<Product> results = query.getResultList();
        assertNotNull(results);
        assertEquals(1,results.size());
        Product productDataBase = results.get(0);
        assertEquals(product.getName(),productDataBase.getName());
        assertEquals(product.getDescription(),productDataBase.getDescription());
        assertEquals(product.getImages().get(0),productDataBase.getImages().get(0));
    }

    private void persistProductsDataBase() throws IOException {
        em = JPAUtil.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("DELETE FROM Image").executeUpdate();
        em.createNativeQuery("DELETE FROM Product").executeUpdate();
        createProductWithImages();
        em.persist(product);
        em.getTransaction().commit();
        //em.close();
    }

    private void clearProductsDataBase() throws IOException {
        em = JPAUtil.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("DELETE FROM Image").executeUpdate();
        em.createNativeQuery("DELETE FROM Product").executeUpdate();
        em.flush();
        em.getTransaction().commit();
    }

    private void createProductWithImages() {
        product = new Product("Primeiro Produto", "Primeiro Produto");
        imagesProduct = new ArrayList<Image>();
        imagesProduct.add(new Image(product));
        product.setImages(imagesProduct);
    }

}
