package model.service;

import model.entities.Offer;
import model.entities.User;
import model.dao.UserDAO;

public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    public User authenticate(String dni, String password) {
        User user = userDAO.findUserByDni(dni);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean createUser(User user) {
        return userDAO.create(user);
    }

    public User findById(int idUser) {
        return userDAO.findById(idUser);
    }
}