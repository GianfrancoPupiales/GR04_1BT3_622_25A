package model.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
public class Profile implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idProfile")
    private int idProfile;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "photo")
    private String photo;

    @Column(name = "description")
    private String description;

    @Column(name = "phoneNumber", length = 10)
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "idUser")
    private User user;

    public Profile() {

    }

    public Profile(int idProfile, String firstName, String lastName, String photo, String description,String phoneNumber,  User user) {
        this.idProfile = idProfile;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photo = photo;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.user = user;
    }

    public Profile(int idProfile, String firstName, String lastName, String photo, String description, String phoneNumber) {
        this.idProfile = idProfile;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photo = photo;
        this.description = description;
        this.phoneNumber = phoneNumber;
    }

    public int getIdProfile() {
        return idProfile;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoto() {
        return photo;
    }

    public String getDescription() {
        return description;
    }

    public User getUser() {
        return user;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIdProfile(int id) {
        this.idProfile = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
