package model.dao;

import model.entities.Favorite;
import model.entities.Product;
import model.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryFavoriteDAO extends FavoriteDAO {
    private List<Favorite> favorites = new ArrayList<>();

    @Override
    public boolean create(Favorite favorite) {
        favorites.add(favorite);
        return true;
    }

    @Override
    public Favorite findByUserAndProduct(User user, Product product) {
        return favorites.stream()
                .filter(f -> f.getUser().getIdUser() == user.getIdUser() &&
                        f.getProduct().getIdProduct() == product.getIdProduct())
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Favorite> findByUser(User user) {
        return favorites.stream()
                .filter(f -> f.getUser().getIdUser() == user.getIdUser())
                .collect(Collectors.toList());
    }


    @Override
    public boolean deleteByUserAndProduct(User user, Product product) {
        Favorite toRemove = findByUserAndProduct(user, product);
        if (toRemove != null) {
            favorites.remove(toRemove);
            return true;
        }
        return false;
    }
}
