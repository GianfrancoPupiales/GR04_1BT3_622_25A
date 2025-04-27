package model.service;

import model.dao.ReputationDAO;
import model.entities.Reputation;
import model.entities.User;

public class ReputationService {

    private final ReputationDAO reputationDAO;

    public ReputationService() {
        reputationDAO = new ReputationDAO();
    }

    public void saveRating(User ratedUser, int score) {
        Reputation reputation = reputationDAO.findByUserId(ratedUser.getIdUser());
        if (reputation == null) {
            reputation = new Reputation(ratedUser);
        }

        reputation.addRating(score);
        reputationDAO.save(reputation);
    }


    public Reputation getReputation(User user) {
        return reputationDAO.findByUserId(user.getIdUser());
    }

}
