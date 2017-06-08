package br.com.productrestfulapi.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name="product")
public class Product implements Serializable {

    @Id
    @GeneratedValue
    @Column(name="id_product")
    private long id;

    @JoinColumn(name = "id_product_parent", referencedColumnName = "id_product")
    @ManyToOne
    private Product parent;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Collection<Image> images;

    @Column(name="name",length=30,nullable=false)
    @NotNull(message="The model.Product name can not be null!")
    private String name;

    @Column(name="description",length=80,nullable=false)
    @NotNull(message="The model.Product description can not be null!")
    private String description;

    public Product(long id, Product parent, Collection<Image> images, String name, String description) {
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

    public Collection<Image> getImages() {
        return images;
    }

    public void setImages(Collection<Image> images) {
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
