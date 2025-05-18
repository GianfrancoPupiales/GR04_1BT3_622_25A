package model.dao;

import model.entities.Profile;
import model.entities.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryProfileDAO extends ProfileDAO {

    private final Map<Integer, Profile> store = new HashMap<>();
    private final Map<Integer, List<Product>> productsStore = new HashMap<>();

    @Override
    public Profile findProfileByUserId(int userId) {
        return store.get(userId);
    }

    public void save(Profile profile) {
        store.put(profile.getId(), profile);
    }
/*
    @Override
    public boolean update(Profile profile) {
        if (!store.containsKey(profile.getId())) return false;
        store.put(profile.getId(), profile);
        return true;
    }


    @Override
    public List<Product> findProductsByUserId(int userId) {
        return productsStore.getOrDefault(userId, List.of());
    }

    @Override
    public void saveProductForUser(int i, Product product1) {

    }

    public void saveProductsForUser(int userId, List<Product> products) {
        productsStore.put(userId, products);
    }
    */
}
