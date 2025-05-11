package model.service;

import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.http.HttpServletRequest;
import model.dao.OfferDAO;
import model.entities.Offer;
import model.entities.User;

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

        return acceptedOffers.stream()
                .filter(offer -> !offer.isDelivered())
                .toList();
    }

    public boolean rateOfferAndMarkDelivered(Offer offer, int score) {
        User offeredByUser = offer.getOfferedByUser();

        if (offeredByUser != null) {
            ReputationService reputationService = new ReputationService();
            reputationService.saveRating(offeredByUser, score);
            offer.markAsDelivered(offeredByUser);
            return offerDAO.updateOffer(offer);  // Asegurarse de que el DAO actualice la oferta correctamente
        }
        return false;
    }

    // Cargar ofertas pendientes por usuario
    public void loadPendingOffers(int userId, HttpServletRequest req) {
        List<Offer> pendingOffers = offerDAO.findPendingOffersByUserId(userId);
        req.setAttribute("offers", pendingOffers);
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


    public ResponseMessage processOfferStatus(Offer offer, String status) {
        if (offer == null) {
            return new ResponseMessage("error", "Offer not found.");
        }

        offer.setStatus(status);
        offerDAO.update(offer);

        return switch (status) {
            case "accepted" -> new ResponseMessage("success", "¡Felicidades por tu intercambio!");
            case "rejected" -> new ResponseMessage("warning", "Lo siento, tu oferta ha sido rechazada.");
            default -> new ResponseMessage("error", "Invalid status.");
        };
    }

    public record ResponseMessage(String type, String message) {
    }


}
