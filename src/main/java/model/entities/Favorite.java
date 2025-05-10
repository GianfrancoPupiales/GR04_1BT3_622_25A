package model.entities;

public class Favorite {
    private String id;              // ID único del favorito (opcional pero útil)
    private String userId;          // ID del usuario que marcó como favorito
    private String productId;       // ID del producto marcado como favorito

    // Constructor
    public Favorite(String userId, String productId) {
        this.userId = userId;
        this.productId = productId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
