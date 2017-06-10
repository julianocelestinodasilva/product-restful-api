package br.com.productrestfulapi.model;

import br.com.productrestfulapi.util.JPAUtil;
import br.com.productrestfulapi.utils.DataBaseUtils;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

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
    public void should_update_product() throws Exception {
        DataBaseUtils.deleteProductsDataBase();
        instanciateProductWithImages();
        final long id = DataBaseUtils.persistProductsDataBaseAndGetId(product);
        final String productName = "Namde Update";
        product.setName(productName);
        product.setId(id);
        repository.update(product);
        DataBaseUtils.assertProductWasUpdated(productName);
    }

    @Test
    public void should_create_product() throws Exception {
        DataBaseUtils.deleteProductsDataBase();
        instanciateProductWithImages();
        repository.create(product);
        DataBaseUtils.assertProductWasCreated(product);
    }

    @Test
    public void should_delete_product() throws Exception {
        instanciateProductWithImages();
        DataBaseUtils.persistProductsDataBase(product);
        final long productId = product.getId();
        repository.delete(productId);
        em = JPAUtil.createEntityManager();
        assertNull(em.find(Product.class, productId));
    }

    private void instanciateProductWithImages() {
        product = new Product("Primeiro Produto", "Primeiro Produto");
        imagesProduct = new ArrayList<Image>();
        imagesProduct.add(new Image(product));
        product.setImages(imagesProduct);
    }

}
