package br.com.productrestfulapi.model;

import br.com.productrestfulapi.util.JPAUtil;
import io.restassured.response.Response;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by juliano on 09/06/17.
 */
public class ProductRepositoryTest {

    private EntityManager em;

    private Product productOne;
    private List<Image> imagesProductOne;

    @Test
    public void should_delete_product() throws Exception {
        persistProducts();
        final long productId = productOne.getId();
        ProductRepository repository = new ProductRepository(JPAUtil.createEntityManager());
        repository.delete(productId);
        em = JPAUtil.createEntityManager();
        assertNull(em.find(Product.class, productId));

    }

    private void persistProducts() throws IOException {
        em = JPAUtil.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("DELETE FROM Image").executeUpdate();
        em.createNativeQuery("DELETE FROM Product").executeUpdate();
        persistProductOne();
        em.getTransaction().commit();
        em.close();
        // shutdown();
    }

    private void persistProductOne() {
        productOne = new Product("Primeiro Produto", "Primeiro Produto");
        imagesProductOne = new ArrayList<Image>();
        imagesProductOne.add(new Image(productOne));
        imagesProductOne.add(new Image(productOne));
        productOne.setImages(imagesProductOne);
        em.persist(productOne);
    }
}
