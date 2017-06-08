package br.com.productrestfulapi.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="image")
public class Image implements Serializable {

    @Id
    @GeneratedValue
    @Column(name="id_image")
    private long id;

    @JoinColumn(name = "id_product", referencedColumnName = "id_product")
    @ManyToOne(optional = false)
    private Product product;
    // We have a Product Entity with One to Many relationship with Image entity

    public Image(Product product) {
        this.id = id;
        this.product = product;
    }

    public Image() {
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                '}';
    }
}
