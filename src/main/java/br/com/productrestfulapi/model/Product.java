package br.com.productrestfulapi.model;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="product")
public class Product implements Serializable {

    public static final String ID_WAS_NOT_INFORMED = "id was not informed !";
    public static final String NAME_OR_DESCRIPTION_WAS_NOT_INFORMED = "name or description was not informed !";
    public static final String PRODUCT_WAS_NOT_INFORMED = "Product was not informed !";

    @Id
    @GeneratedValue
    @Column(name="id_product")
    private long id;

    @JoinColumn(name = "id_product_parent", referencedColumnName = "id_product")
    @ManyToOne
    private Product parent;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Image> images;

    @Column(name="name",length=30,nullable=false)
    @NotNull(message="The model.Product name can not be null!")
    private String name;

    @Column(name="description",length=80,nullable=false)
    @NotNull(message="The model.Product description can not be null!")
    private String description;

    public Product(long id, Product parent, List<Image> images, String name, String description) {
        this.id = id;
        this.parent = parent;
        this.images = images;
        this.name = name;
        this.description = description;
    }

    public Product(String name, String description, Product parent) {
        this.name = name;
        this.description = description;
        this.parent = parent;
    }

    public Product(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Product(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", parent=" + parent +
                ", images=" + images +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (parent != null ? !parent.equals(product.parent) : product.parent != null) return false;
        if (images != null ? !images.equals(product.images) : product.images != null) return false;
        if (name != null ? !name.equals(product.name) : product.name != null) return false;
        return description != null ? description.equals(product.description) : product.description == null;
    }

    @Override
    public int hashCode() {
        int result = parent != null ? parent.hashCode() : 0;
        result = 31 * result + (images != null ? images.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    public static Product getFromJSONToUpdate(JSONObject json) throws IllegalArgumentException {
        verifyFieldsNotNull(json);
        try {
            Product product = getFromJSON(json);
            if (!json.has("id") || json.getLong("id") < 1) {
                throw new IllegalArgumentException(ID_WAS_NOT_INFORMED);
            }
            product.setId(json.getLong("id"));
            return product;
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Product getFromJSONToCreate(JSONObject json) throws IllegalArgumentException {
        verifyFieldsNotNull(json);
        try {
            return getFromJSON(json);
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static Product getFromJSON(JSONObject json) throws JSONException {
        String name = json.getString("name");
        String description = json.getString("description");
        Product product = new Product(name, description);
        if (json.has("parent")) {
            Product parent = (Product) json.get("parent");
            product.setParent(parent);
        }
        if (json.has("images")) {
            List<Image> images = (List<Image>) json.get("images");
            product.setImages(images);
        }
        return product;
    }

    private static void verifyFieldsNotNull(JSONObject json) {
        if (json == null) {
            throw new IllegalArgumentException(PRODUCT_WAS_NOT_INFORMED);
        }
        if (!json.has("name") || !json.has("description")) {
            throw new IllegalArgumentException(NAME_OR_DESCRIPTION_WAS_NOT_INFORMED);
        }
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(Product parent) {
        this.parent = parent;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Product getParent() {
        return parent;
    }

    public Product() {}

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
