package br.com.productrestfulapi.model;

import br.com.productrestfulapi.util.JPAUtil;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
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
    public void should_delete_product() throws Exception {
        persistProducts();
        final long productId = product.getId();
        repository.delete(productId);
        em = JPAUtil.createEntityManager();
        assertNull(em.find(Product.class, productId));
    }

    private void persistProducts() throws IOException {
        em = JPAUtil.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("DELETE FROM Image").executeUpdate();
        em.createNativeQuery("DELETE FROM Product").executeUpdate();
        createProduct();
        em.persist(product);
        em.getTransaction().commit();
        em.close();
    }

    private void createProduct() {
        product = new Product("Primeiro Produto", "Primeiro Produto");
        imagesProduct = new ArrayList<Image>();
        imagesProduct.add(new Image(product));
        product.setImages(imagesProduct);
    }

}
