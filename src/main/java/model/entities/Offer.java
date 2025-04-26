package model.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Offer implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idOffer")
    private int idOffer;

    @Column(name = "offeredProducts")
    private String offeredProducts;

    @Column(name = "productToOffer")
    private String productToOffer;

    @Column(name = "status", nullable = false)
    private String status = "pending"; // default status


    // Constructor sin argumentos
    public Offer() {
    }

    // Constructor con 4 argumentos
    public Offer(int idOffer, String offeredProducts, String productToOffer, String status) {
        this.idOffer = idOffer;
        this.offeredProducts = offeredProducts;
        this.productToOffer = productToOffer;
        this.status = status;
    }

    // Constructor con 3 argumentos
    public Offer(int idOffer, String offeredProducts, String productToOffer) {
        this.idOffer = idOffer;
        this.offeredProducts = offeredProducts;
        this.productToOffer = productToOffer;
        this.status = "pending"; // Valor predeterminado
    }


    public int getIdOffer() {
        return idOffer;
    }

    public void setIdOffer(int idOffer) {
        this.idOffer = idOffer;
    }

    public String getOfferedProducts() {
        return offeredProducts;
    }

    public void setOfferedProducts(String offeredProducts) {
        this.offeredProducts = offeredProducts;
    }

    public String getProductToOffer() {
        return productToOffer;
    }

    public void setProductToOffer(String productToOffer) {
        this.productToOffer = productToOffer;
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
}
