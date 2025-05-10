package model.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
//  Esto evita que un mismo usuario agregue el mismo producto como favorito más de una vez.
// @Table(name = "favorites", uniqueConstraints = @UniqueConstraint(columnNames = {"idUser", "idProduct"}))
@Table(name = "favorites")
public class Favorite implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idFavorite")
    private int idFavorite;

    @ManyToOne
    @JoinColumn(name = "idUser", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "idProduct", nullable = false)
    private Product product;

    // Constructor
    public Favorite() {
    }

    public Favorite(User user, Product product) {
        this.user = user;
        this.product = product;
    }

    public int getIdFavorite() {
        return idFavorite;
    }

    public void setIdFavorite(int idFavorite) {
        this.idFavorite = idFavorite;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
