package br.com.productrestfulapi.model;

import org.codehaus.jettison.json.JSONObject;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="image")
public class Image implements Serializable {

    public static final String ID_WAS_NOT_INFORMED = "id was not informed !";
    public static final String IMAGE_WAS_NOT_INFORMED = "Image was not informed !";
    public static final String PRODUCT_WAS_NOT_INFORMED = "Product was not informed !";

    @Id
    @GeneratedValue
    @Column(name="id_image")
    private long id;

    @JoinColumn(name = "id_product", referencedColumnName = "id_product")
    @ManyToOne(optional = false)
    private Product product;

    public Image(Product product) {
        this.product = product;
    }

    public Image() {
    }

    public static Image getFromJSON(JSONObject json) {
        verifyFieldsNotNull(json);
        try {
            Product product = new Product();
            product.setId(json.getLong("idProduct"));
            Image image = new Image(product);
            return image;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static void verifyFieldsNotNull(JSONObject json) {
        if (json == null) {
            throw new IllegalArgumentException(IMAGE_WAS_NOT_INFORMED);
        }
        if (!json.has("idProduct")) {
            throw new IllegalArgumentException(PRODUCT_WAS_NOT_INFORMED);
        }
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Image image = (Image) o;

        return id == image.id;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                '}';
    }
}
