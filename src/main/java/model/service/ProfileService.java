package model.service;

import model.dao.ProfileDAO;
import model.entities.Product;
import model.entities.Profile;
import model.entities.User;

import java.util.List;

public class ProfileService {

    private final ProfileDAO profileDAO;

    public ProfileService(ProfileDAO profileDAO) {
        this.profileDAO = profileDAO;
    }

    public ProfileService() {
        profileDAO = new ProfileDAO();
    }

    public Profile getProfileByUserId(int userId) {
        return profileDAO.findProfileByUserId(userId);
    }

    public boolean updateProfile(Profile profile) {
        if (!validateProfile(profile)) {
            return false;
        }
        Profile existing = profileDAO.findProfileByUserId(profile.getUser().getUserId());
        if (existing != null && profile.getPhoto() == null) {
            profile.setPhoto(existing.getPhoto());
        }

        return profileDAO.update(profile);
    }

    public boolean validateProfile(Profile profile) {
        return profile != null
                && profile.getFirstName() != null && !profile.getFirstName().isBlank()
                && profile.getLastName() != null && !profile.getLastName().isBlank()
                && profile.getPhoneNumber() != null && profile.getPhoneNumber().matches("\\d{10}");
    }

    public boolean createProfile(Profile profile) {
        return profileDAO.create(profile);
    }

    public Profile getFullProfileWithProducts(int userId) {
        Profile profile = getProfileByUserId(userId);
        if (profile == null) {
            return null;
        }

        User user = getUserWithProducts(userId);
        profile.setUser(user);

        return profile;
    }

    private User getUserWithProducts(int userId) {
        User user = profileDAO.findUserById(userId);
        if (user == null) return null;

        List<Product> products = profileDAO.findProductsByUserId(userId);
        if (products != null && !products.isEmpty()) {
            for (Product product : products) {
                product.setUser(user);
            }
            user.setProducts(products);
        }

        return user;
    }

}
