package br.com.productrestfulapi.model;

import br.com.productrestfulapi.util.JPAUtil;
import br.com.productrestfulapi.utils.DataBaseUtils;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by juliano on 09/06/17.
 */
public class ProductRepositoryTest {

    private EntityManager em;

    private Product product;
    private ProductRepository repository;

    @Before
    public void setUp() throws Exception {
        repository = new ProductRepository(JPAUtil.createEntityManager());
    }

    @Test
    public void get_product_by_identity_including_specified_relationships() throws Exception {
        List<Product> productsWithRelationships = DataBaseUtils.persistProductsWithRelationships();
        Product productWithImages = productsWithRelationships.get(0);
        Product product = repository.get(productWithImages.getId());
        assertNotNull(product);
        assertProductWithImages(productWithImages, product);
    }

    @Test
    public void get_all_products_including_specified_relationships() throws Exception {
        List<Product> productsWithRelationships = DataBaseUtils.persistProductsWithRelationships();
        Product productWithImages = productsWithRelationships.get(0);
        Product productWithParent = productsWithRelationships.get(1);
        List<Product> products = repository.get();
        assertNotNull(products);
        assertEquals(2,products.size());
        assertProductWithImages(productWithImages, products);
        assertProductWithParent(productWithParent, products);
    }

    @Test
    public void should_update_product() throws Exception {
        DataBaseUtils.deleteEntities();
        product = new Product("Primeiro Produto", "Primeiro Produto");
        final long id = DataBaseUtils.deleteEntitiesAndPersistProductAndGetId(product);
        final String productName = "Namde Update";
        product.setName(productName);
        product.setId(id);
        repository.update(product);
        DataBaseUtils.assertProductWasUpdated(productName);
    }

    @Test
    public void should_create_product() throws Exception {
        DataBaseUtils.deleteEntities();
        product = new Product("Primeiro Produto", "Primeiro Produto");
        repository.create(product);
        DataBaseUtils.assertWasCreated(product);
    }

    @Test
    public void should_delete_product() throws Exception {
        product = new Product("Primeiro Produto", "Primeiro Produto");
        DataBaseUtils.persist(product);
        final long productId = product.getId();
        repository.delete(productId);
        em = JPAUtil.createEntityManager();
        assertNull(em.find(Product.class, productId));
    }

    private void assertProductWithParent(Product productWithParent, List<Product> products) {
        Product product2 = products.get(1);
        assertEquals(productWithParent.getName(),product2.getName());
        assertEquals(productWithParent.getDescription(),product2.getDescription());
        assertNotNull(product2.getParent());
        assertTrue(product2.getParent().getId() > 0);
    }

    private void assertProductWithImages(Product productWithImages, List<Product> products) {
        Product product1 = products.get(0);
        assertEquals(productWithImages.getName(),product1.getName());
        assertEquals(productWithImages.getDescription(),product1.getDescription());
        assertNotNull(product1.getImages());
        assertEquals(1,product1.getImages().size());
        assertTrue(product1.getImages().get(0).getId() > 0);
    }

    private void assertProductWithImages(Product productWithImages, Product productFound) {
        assertEquals(productWithImages.getName(),productFound.getName());
        assertEquals(productWithImages.getDescription(),productFound.getDescription());
        assertNotNull(productFound.getImages());
        assertEquals(1,productFound.getImages().size());
        assertTrue(productFound.getImages().get(0).getId() > 0);
    }
}
