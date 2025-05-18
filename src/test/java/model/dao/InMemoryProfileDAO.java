package model.dao;

import model.entities.Profile;
import model.entities.Product;
import model.entities.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryProfileDAO extends ProfileDAO {

    public final Map<Integer, Profile> store = new HashMap<>();
    private final Map<Integer, List<Product>> productsStore = new HashMap<>();
    private final Map<Integer, User> usersStore = new HashMap<>();

    @Override
    public Profile findProfileByUserId(int userId) {
        return store.get(userId);
    }

    public void save(Profile profile) {
        store.put(profile.getIdProfile(), profile);
    }

    @Override
    public boolean update(Profile profile) {
        if (!store.containsKey(profile.getIdProfile())) return false;
        store.put(profile.getIdProfile(), profile);
        return true;
    }

    public void saveProductsForUser(int id, List<Product> products) {
        productsStore.put(id, products);
    }

    public void saveUser(User user) {
        if (user != null) {
            usersStore.put(user.getUserId(), user);
        }
    }

    public User findUserById(int userId) {
        return usersStore.get(userId);
    }

    @Override
    public List<Product> findProductsByUserId(int userId) {
        return productsStore.getOrDefault(userId, List.of());
    }
}
