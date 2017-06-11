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

    public Image(Product product) {
        this.product = product;
    }

    public Image() {
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
