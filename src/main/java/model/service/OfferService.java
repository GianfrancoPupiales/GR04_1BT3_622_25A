package model.service;

import jakarta.persistence.EntityManagerFactory;
import model.dao.OfferDAO;
import model.entities.Offer;

import java.util.ArrayList;
import java.util.List;

public class OfferService {

    private final OfferDAO offerDAO;
    private static EntityManagerFactory entityManagerFactory;
    public OfferService() {
        this.offerDAO = new OfferDAO();
    }

    public List<Offer> findAcceptedOffersPendingDeliveryByUserId(int idUser) {
        List<Offer> acceptedOffers = offerDAO.findAcceptedOffersByUserId(idUser);

        List<Offer> pendingDeliveries = new ArrayList<>();
        for (Offer offer : acceptedOffers) {
            if (!offer.isDelivered()) {
                pendingDeliveries.add(offer);
            }
        }

        return pendingDeliveries;
    }

    public Offer findById(int offerId) {
        return offerDAO.findById(offerId);
    }
    public boolean changeOfferStatusToPending(Offer offer) {
        return offerDAO.changeOfferStatusToPending(offer);
    }

    public boolean confirmDeliveryAndUpdateOffer(Offer offer) {
        return  offerDAO.confirmDeliveryAndUpdateOffer(offer);
    }
}
