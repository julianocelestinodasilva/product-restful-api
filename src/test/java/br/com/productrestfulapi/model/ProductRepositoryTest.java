package br.com.productrestfulapi.model;

import br.com.productrestfulapi.util.JPAUtil;
import br.com.productrestfulapi.utils.DataBaseUtils;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertNull;

/**
 * Created by juliano on 09/06/17.
 */
public class ProductRepositoryTest {

    private EntityManager em;

    private Product product;
    // TODO private List<Image> imagesProduct;
    private ProductRepository repository;

    @Before
    public void setUp() throws Exception {
        repository = new ProductRepository(JPAUtil.createEntityManager());
    }

    @Test
    public void should_update_product() throws Exception {
        DataBaseUtils.deleteProducts();
        product = new Product("Primeiro Produto", "Primeiro Produto");
        final long id = DataBaseUtils.persistProductsAndGetId(product);
        final String productName = "Namde Update";
        product.setName(productName);
        product.setId(id);
        repository.update(product);
        DataBaseUtils.assertProductWasUpdated(productName);
    }

    @Test
    public void should_create_product() throws Exception {
        DataBaseUtils.deleteProducts();
        product = new Product("Primeiro Produto", "Primeiro Produto");
        repository.create(product);
        DataBaseUtils.assertProductWasCreated(product);
    }

    @Test
    public void should_delete_product() throws Exception {
        product = new Product("Primeiro Produto", "Primeiro Produto");
        DataBaseUtils.persistProducts(product);
        final long productId = product.getId();
        repository.delete(productId);
        em = JPAUtil.createEntityManager();
        assertNull(em.find(Product.class, productId));
    }

}
