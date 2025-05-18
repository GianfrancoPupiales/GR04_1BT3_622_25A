package model.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
public class Profile implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "photo")
    private String photo;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "idUser")
    private User user;

    public Profile() {

    }

    public Profile(int id, String firstName, String lastName, String photo, String description, User user) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photo = photo;
        this.description = description;
        this.user = user;
    }

    public int getId() {return id;}

    public String getFirstName() {return firstName;}

    public String getLastName() {return lastName;}

    public String getPhoto() {return photo;}

    public String getDescription() {return description;}

    public User getUser() {return user;}

    public String setFirstName(String carla) {
        return "void";
    }

    public void setProducts(List<Product> userProducts) {
    }

    public Collection<Object> getProducts() {
        return null;
    }
}
