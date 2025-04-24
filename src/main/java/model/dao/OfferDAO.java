package model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.entities.Offer;
import model.entities.Product;

import java.util.List;

public class OfferDAO extends GenericDAO<Offer>{
    public OfferDAO() {
        super(Offer.class);
    }

}
