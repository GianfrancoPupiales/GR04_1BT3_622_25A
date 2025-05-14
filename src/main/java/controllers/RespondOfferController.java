package controllers;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.entities.Offer;
import model.entities.Product;
import model.entities.Reputation;
import model.entities.User;
import model.service.OfferService;
import model.service.ProductService;
import model.service.ReputationService;
import java.util.Map;
import java.util.HashMap;


import java.io.IOException;
import java.util.List;

@WebServlet(name = "RespondOfferController", urlPatterns = {"/RespondOfferController"})
public class RespondOfferController extends HttpServlet {

    private final OfferService offerService = new OfferService();
    private final ProductService productService = new ProductService();
    private final ReputationService reputationService = new ReputationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String route = req.getParameter("route");

        if (route == null || route.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ruta no especificada.");
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
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Ruta no encontrada: " + route);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String route = req.getParameter("route");

        if (route == null || route.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ruta no especificada.");
            return;
        }

        switch (route) {
            case "respond":
                handleRespondOffer(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Ruta no encontrada: " + route);
        }
    }

    private void handleListOffers(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = getLoggedUser(req, resp);
        if (user == null) return;

        offerService.loadPendingOffers(user.getIdUser(), req);
        req.getRequestDispatcher("jsp/OFFERS.jsp").forward(req, resp);
    }

    private void handleViewOffer(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int offerId = Integer.parseInt(req.getParameter("offerId"));
            Offer offer = offerService.findById(offerId);

            if (offer == null) {
                req.setAttribute("error", "La oferta no existe.");
                req.getRequestDispatcher("jsp/error.jsp").forward(req, resp);
                return;
            }

            Product yourProduct = offer.getProductToOffer();
            List<Product> offeredProducts = offer.getOfferedProducts();
            User offeringUser = offer.getOfferedByUser();
<<<<<<< HEAD

            // Reputación del usuario
            Reputation reputation = reputationService.findByUserId(offeringUser.getId());
=======
            Reputation reputation = reputationService.findByUserId(offeringUser.getIdUser());
>>>>>>> ac1dfaae6e6054d48133a6a2ac68b484331bf1f4

            req.setAttribute("yourProduct", yourProduct);
            req.setAttribute("offeredProducts", offeredProducts);
            req.setAttribute("offeringUser", offeringUser);
            req.setAttribute("reputation", reputation);

            req.getRequestDispatcher("jsp/OFFERS.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            req.setAttribute("error", "ID de oferta inválido.");
            req.getRequestDispatcher("jsp/error.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Ocurrió un error al procesar la oferta.");
            req.getRequestDispatcher("jsp/error.jsp").forward(req, resp);
        }
    }

<<<<<<< HEAD
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String route = request.getParameter("route");
        String status = request.getParameter("status");
        String offerIdStr = request.getParameter("offerId");

        // Obtener el usuario actualmente logueado
        User currentUser = getLoggedUser(request, response);

        if (currentUser == null) {
            // Si no hay usuario logueado, redirigir al login
            response.sendRedirect("LoginController?route=enter");
            return;
        }

        // Convierte el offerId de String a int
        int offerId = Integer.parseInt(offerIdStr);

        // Verificar si la oferta existe
        Offer offer = offerService.findById(offerId);

        if (offer == null) {
            // Si no existe la oferta, redirigir con un mensaje de error
            request.setAttribute("message", "La oferta no existe.");
            request.setAttribute("messageType", "danger");
            request.getRequestDispatcher("jsp/OFFERS.jsp").forward(request, response);
            return;
        }


        // Lógica para aceptar o rechazar la oferta
        if ("accepted".equals(status)) {
            offerService.processOfferStatus(offer, "accepted");
        } else if ("rejected".equals(status)) {
            offerService.processOfferStatus(offer, "rejected");
        }

        // Redirigir después de aceptar o rechazar la oferta
        request.setAttribute("message", "Respuesta a la oferta procesada correctamente.");
        request.setAttribute("messageType", "success");
        response.sendRedirect("RespondOfferController?route=list");
    }


    private void handleListOffers(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = getLoggedUser(req, resp);
        if (user == null) return;

        // Cargar las ofertas pendientes para el usuario logueado
        offerService.loadPendingOffers(user.getId(), req);

        // Redirigir a la vista de ofertas
        req.getRequestDispatcher("jsp/OFFERS.jsp").forward(req, resp);
    }


    // Responder una oferta: aceptar o rechazar
=======
>>>>>>> ac1dfaae6e6054d48133a6a2ac68b484331bf1f4
    private void handleRespondOffer(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = getLoggedUser(req, resp);
        if (user == null) return;

        String status = req.getParameter("status");
        String offerIdStr = req.getParameter("offerId");

        Map<String, String> responseMessage = new HashMap<>();

        if (status == null || offerIdStr == null) {
            responseMessage.put("type", "error");
            responseMessage.put("message", "Datos incompletos para procesar la oferta.");
        } else {
            try {
                int offerId = Integer.parseInt(offerIdStr);
                Offer offer = offerService.findById(offerId);

<<<<<<< HEAD
                if (offer == null || offer.getOfferedByUser() == null || offer.getOfferedByUser().getId() != user.getId()) {
=======
                if (offer == null) {
>>>>>>> ac1dfaae6e6054d48133a6a2ac68b484331bf1f4
                    responseMessage.put("type", "error");
                    responseMessage.put("message", "La oferta no existe.");
                } else {
                    OfferService.ResponseMessage serviceMessage = offerService.processOfferStatus(offer, status);
                    responseMessage.put("type", serviceMessage.type());
                    responseMessage.put("message", serviceMessage.message());
                }
            } catch (NumberFormatException e) {
                responseMessage.put("type", "error");
                responseMessage.put("message", "ID de oferta inválido.");
            }
        }

        req.setAttribute("messageType", responseMessage.get("type"));
        req.setAttribute("message", responseMessage.get("message"));
<<<<<<< HEAD

        // Recargar la lista de ofertas pendientes
        offerService.loadPendingOffers(user.getId(), req);
=======
        offerService.loadPendingOffers(user.getIdUser(), req);
>>>>>>> ac1dfaae6e6054d48133a6a2ac68b484331bf1f4
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
