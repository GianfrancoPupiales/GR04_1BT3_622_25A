package model.service;

import model.dao.ProfileDAO;
import model.entities.Product;
import model.entities.Profile;

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
                && profile.getLastName() != null && !profile.getLastName().isBlank();
    }

    /*
    public Profile getFullProfileWithProducts(int userId) {
        Profile profile = profileDAO.findByUserId(userId);
        if (profile != null) {
            List<Product> userProducts = profileDAO.findProductsByUserId(userId);
            profile.setProducts(userProducts);
        }
        return profile;
    }
    */

    public boolean createProfile(Profile profile) {
        return profileDAO.create(profile);
    }

    public Profile getFullProfileWithProducts(int i) {
        return null;
    }
}
