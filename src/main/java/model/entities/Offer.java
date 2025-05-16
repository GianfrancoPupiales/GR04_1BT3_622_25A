package model.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class Offer implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idOffer")
    private int idOffer;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "offer_products",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> offeredProducts; // producto o productos de quien ofrece

    @ManyToOne
    @JoinColumn(name = "productToOffer_id")
    private Product productToOffer; //producto principal

    @Column(name = "status", nullable = false)
    private String status = "pending"; // default status

    @Column(name = "isDelivered", nullable = false)
    private boolean isDelivered = false;

    @ManyToOne
    @JoinColumn(name = "offeredByUser_id")
    private User offeredByUser; //id del usuario del producto principal

    // Constructor sin argumentos
    public Offer() {
    }

    // Constructor con 4 argumentos
    public Offer(int idOffer, List<Product> offeredProducts, Product productToOffer, String status) {
        this.idOffer = idOffer;
        this.offeredProducts = offeredProducts;
        this.productToOffer = productToOffer;
        this.status = status;
    }

    // Constructor con 3 argumentos
    public Offer(int idOffer, List<Product> offeredProducts, Product productToOffer) {
        this.idOffer = idOffer;
        this.offeredProducts = offeredProducts;
        this.productToOffer = productToOffer;
        this.status = "pending"; // Valor predeterminado
    }

    public User getOfferedByUser() {
        return offeredByUser;
    }

    public void setOfferedByUser(User offeredByUser) {
        this.offeredByUser = offeredByUser;
    }

    public int getIdOffer() {
        return idOffer;
    }

    public void setIdOffer(int idOffer) {
        this.idOffer = idOffer;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }

    public List<Product> getOfferedProducts() {
        return offeredProducts;
    }

    public void setOfferedProducts(List<Product> offeredProducts) {
        this.offeredProducts = offeredProducts;
    }

    public Product getProductToOffer() {
        return productToOffer;
    }

    public void setProductToOffer(Product productToOffer) {
        this.productToOffer = productToOffer;
    }

    public void markAsDelivered(User offeredByUser) {
        this.isDelivered = true;
    }
}
