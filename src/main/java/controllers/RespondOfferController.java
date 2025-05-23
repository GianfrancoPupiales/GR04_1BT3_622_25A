package controllers;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.entities.Offer;
import model.entities.Product;
import model.entities.Reputation;
import model.entities.User;
import model.service.OfferService;
import model.service.ReputationService;

import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "RespondOfferController", urlPatterns = {"/RespondOfferController"})
public class RespondOfferController extends HttpServlet {


    private final OfferService offerService = new OfferService();
    private final ReputationService reputationService = new ReputationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String route = req.getParameter("route");

        if (route == null || route.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Route not specified.");
            return;
        }

        switch (route) {
            case "list":
                handleListOffers(req, resp);
                break;
            case "view":
                handleViewOffer(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Route not found: " + route);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String route = req.getParameter("route");

        if (route == null || route.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Route not specified.");
            return;
        }

        switch (route) {
            case "respond":
                handleRespondOffer(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Route not found: " + route);
        }
    }

    private void handleListOffers(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = getLoggedUser(req, resp);
        if (user == null) return;


        offerService.loadPendingOffers(user.getUserId(), req);
        req.getRequestDispatcher("jsp/OFFERS.jsp").forward(req, resp);
    }

    private void handleViewOffer(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int offerId = Integer.parseInt(req.getParameter("offerId"));
            Offer offer = offerService.findById(offerId);
            if (offer == null) {
                req.setAttribute("error", "The offer doesn't exist.");
                req.getRequestDispatcher("jsp/error.jsp").forward(req, resp);
                return;
            }

            Product yourProduct = offer.getProductToOffer();
            List<Product> offeredProducts = offer.getOfferedProducts();
            User offeringUser = offer.getOfferedByUser();

            Reputation reputation = reputationService.findByUserId(offeringUser.getUserId());
            req.setAttribute("yourProduct", yourProduct);
            req.setAttribute("offeredProducts", offeredProducts);
            req.setAttribute("offeringUser", offeringUser);
            req.setAttribute("reputation", reputation);

            req.getRequestDispatcher("jsp/OFFERS.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            req.setAttribute("error", "Invalid offer ID.");
            req.getRequestDispatcher("jsp/error.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "An error occurred while processing the offer.");
            req.getRequestDispatcher("jsp/error.jsp").forward(req, resp);
        }
    }

    // Responder una oferta: aceptar o rechazar

    private void handleRespondOffer(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = getLoggedUser(req, resp);
        if (user == null) return;

        String status = req.getParameter("status");
        String offerIdStr = req.getParameter("offerId");

        Map<String, String> responseMessage = new HashMap<>();

        if (status == null || offerIdStr == null) {
            responseMessage.put("type", "error");
            responseMessage.put("message", "Incomplete data for processing the offer.");
        } else {
            try {
                int offerId = Integer.parseInt(offerIdStr);
                Offer offer = offerService.findById(offerId);
                System.out.println(">>> ID offer: " + offer.getIdOffer());
                System.out.println(">>> Productos ofrecidos:");
                for (Product p : offer.getOfferedProducts()) {
                    System.out.println(" - " + p.getTitle());
                }
                req.setAttribute("offer", offer);
                if (offer == null) {
                    responseMessage.put("type", "error");
                    responseMessage.put("message", "La oferta no existe.");
                } else {
                    OfferService.ResponseMessage serviceMessage = offerService.processOfferStatus(offerId, status);
                    responseMessage.put("type", serviceMessage.type());
                    responseMessage.put("message", serviceMessage.message());
                }
            } catch (NumberFormatException e) {
                responseMessage.put("type", "error");
                responseMessage.put("message", "Invalid offer ID.");
            }
        }

        req.setAttribute("messageType", responseMessage.get("type"));
        req.setAttribute("message", responseMessage.get("message"));

        offerService.loadPendingOffers(user.getUserId(), req);
        req.getRequestDispatcher("jsp/OFFERS.jsp").forward(req, resp);
    }

    private User getLoggedUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect("LoginController?route=enter");
            return null;
        }
        return user;
    }
}
