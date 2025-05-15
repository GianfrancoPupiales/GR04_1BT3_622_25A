package model.service;

import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.http.HttpServletRequest;
import model.dao.OfferDAO;
import model.entities.Offer;
import model.entities.Product;
import model.entities.User;

import javax.sound.midi.SysexMessage;
import java.util.ArrayList;
import java.util.List;

public class OfferService {

    private final OfferDAO offerDAO;
    private static EntityManagerFactory entityManagerFactory;
    private ProductService productService;

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

            ProductService productService = new ProductService();
            productService.updateProductAvailability(offer.getOfferedProducts(), false);
            productService.updateProductAvailability(List.of(offer.getProductToOffer()), false);

            return offerDAO.update(offer);
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

    public ResponseMessage processOfferStatus(Offer offer, String status) {
        if (offer == null) {
            return new ResponseMessage("error", "La oferta no existe.");
        }

        // Evitar que se procese más de una vez
        if (!"pending".equalsIgnoreCase(offer.getStatus())) {
            return new ResponseMessage("warning", "La oferta ya fue procesada anteriormente.");
        }

        offer.setStatus(status);
        boolean updated = offerDAO.update(offer);

        if (!updated) {
            return new ResponseMessage("error", "No se pudo actualizar el estado de la oferta.");
        }

        if ("accepted".equalsIgnoreCase(status)) {
            ProductService productService = new ProductService();
            System.out.println("Productos que me ofrecieron :");
            for (Product product : offer.getOfferedProducts()) {
                System.out.println(product);
            }

            System.out.println("My producto:" + offer.getProductToOffer());

            // Desactivar tu producto (el que fue ofrecido a cambio)
            productService.updateProductAvailability(List.of(offer.getProductToOffer()), false);
            System.out.println("Desactivando productToOffer: " + offer.getProductToOffer().getIdProduct());

            // Desactivar los productos ofrecidos por el ofertante
            if (offer.getOfferedProducts() != null && !offer.getOfferedProducts().isEmpty()) {
                System.out.println("Desactivando offeredProducts: " + offer.getOfferedProducts().size());
                for (Product p : offer.getOfferedProducts()) {
                    System.out.println(" - OfferedProduct: " + p.getIdProduct());
                }
                productService.updateProductAvailability(offer.getOfferedProducts(), false);
            } else {
                System.out.println("OfferedProducts está vacío o es null");
            }
        }


        return switch (status.toLowerCase()) {
            case "accepted" -> new ResponseMessage("success", "¡Felicidades por tu intercambio!");
            case "rejected" -> new ResponseMessage("warning", "Lo siento, tu oferta ha sido rechazada.");
            default -> new ResponseMessage("error", "Estado de oferta inválido.");
        };
    }

    public void confirmDelivery(Offer offer) {
        offer.markAsDelivered(offer.getOfferedByUser( ));
    }

    public boolean updateOffer(Offer offer) {
        return offerDAO.update(offer);
    }


    public record ResponseMessage(String type, String message) {
    }


}
