package model.service;

import jakarta.servlet.http.HttpServletRequest;
import model.dao.OfferDAO;
import model.entities.Offer;
import model.entities.Product;
import model.entities.User;

import java.util.List;

public class OfferService {

    private final OfferDAO offerDAO;
    private final ProductService productService;

    public OfferService() {
        this.offerDAO = new OfferDAO();
        this.productService = new ProductService();
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

    public ResponseMessage processOfferStatus(int offerId, String status) {
        Offer offer = offerDAO.findById(offerId);
        if (offer == null) {
            return new ResponseMessage("error", "The offer doesn't exist.");
        }

        // Evitar que se procese más de una vez
        if (!"pending".equalsIgnoreCase(offer.getStatus())) {
            return new ResponseMessage("warning", "The offer has already been processed.");
        }

        offer.setStatus(status);
        boolean updated = offerDAO.update(offer);

        if (!updated) {
            return new ResponseMessage("error", "The status of the offer could not be updated.");
        }

        if ("accepted".equalsIgnoreCase(status)) {
            System.out.println("Productos que me ofrecieron :");
            for (Product product : offer.getOfferedProducts()) {
                System.out.println("Productos: " + product.getIdProduct() + "Nombre:" + product.getTitle());
            }

            System.out.println("My productos:" + offer.getProductToOffer());

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
                System.out.println("OfferedProducts is empty or null");
            }
        }


        return switch (status.toLowerCase()) {
            case "accepted" -> new ResponseMessage("success", "Congratulations on your exchange!");
            case "rejected" -> new ResponseMessage("success", "The offer has been successfully rejected");
            default -> new ResponseMessage("error", "Invalid offer status");
        };
    }

    public void confirmDelivery(Offer offer) {
        offer.markAsDelivered(offer.getOfferedByUser());
    }

    public boolean updateOffer(Offer offer) {
        return offerDAO.update(offer);
    }


    public record ResponseMessage(String type, String message) {
    }

    public boolean createOffer(Offer offer) {
        return offerDAO.create(offer);
    }
}
