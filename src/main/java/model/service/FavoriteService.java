package model.service;

import jakarta.persistence.EntityManager;
import model.dao.FavoriteDAO;
import model.entities.Favorite;
import model.entities.Product;
import model.entities.User;

import java.util.List;

public class FavoriteService {
    private FavoriteDAO favoriteDAO = new FavoriteDAO();
    public void addFavorite(User user, Product product) {
/*
        Favorite existing = favoriteDAO.findByUserAndProduct(user,product);

        if (existing != null) {
            throw new RuntimeException("Este producto ya está en favoritos");
        }
*/
        Favorite favorite = new Favorite(user, product);
        favoriteDAO.create(favorite);
    }



    public List<Favorite> getFavoritesByUser(User user) {
        List<Favorite> list = favoriteDAO.findByUser(user);
        System.out.println("Favoritos encontrados: " + list.size());
        return list;
    }

}
