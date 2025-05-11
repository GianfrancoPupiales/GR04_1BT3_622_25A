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

        if (route != null && route.equals("list")) {
            handleListOffers(req, resp);
            return;
        }

        try {
            // Obtener el ID de la oferta desde el parámetro
            int offerId = Integer.parseInt(req.getParameter("offerId"));

            // Buscar la oferta
            Offer offer = offerService.findById(offerId);

            if (offer == null) {
                req.setAttribute("error", "La oferta no existe.");
                req.getRequestDispatcher("jsp/error.jsp").forward(req, resp);
                return;
            }

            // Tu producto (el que fue ofrecido originalmente en el sistema)
            Product yourProduct = offer.getProductToOffer();

            // Productos ofrecidos a cambio
            List<Product> offeredProducts = offer.getOfferedProducts();

            // Usuario que hace la oferta
            User offeringUser = offer.getOfferedByUser();

            // Reputación del usuario
            Reputation reputation = reputationService.findByUserId(offeringUser.getIdUser());

            // Enviar los datos a la vista
            req.setAttribute("yourProduct", yourProduct);
            req.setAttribute("offeredProducts", offeredProducts);
            req.setAttribute("offeringUser", offeringUser);
            req.setAttribute("reputation", reputation);

            req.getRequestDispatcher("jsp/OFFERS.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Ocurrió un error al procesar la oferta.");
            req.getRequestDispatcher("jsp/error.jsp").forward(req, resp);
        }
    }

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
        offerService.loadPendingOffers(user.getIdUser(), req);

        // Redirigir a la vista de ofertas
        req.getRequestDispatcher("jsp/OFFERS.jsp").forward(req, resp);
    }


    // Responder una oferta: aceptar o rechazar
    private void handleRespondOffer(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = getLoggedUser(req, resp);
        if (user == null) return;

        String status = req.getParameter("status");
        String offerIdStr = req.getParameter("offerId");

        // Usar un Map para almacenar el tipo y el mensaje de la respuesta
        Map<String, String> responseMessage = new HashMap<>();

        if (status == null || offerIdStr == null) {
            responseMessage.put("type", "error");
            responseMessage.put("message", "Datos incompletos para procesar la oferta.");
        } else {
            try {
                int offerId = Integer.parseInt(offerIdStr);
                Offer offer = offerService.findById(offerId);

                if (offer == null || offer.getOfferedByUser() == null || offer.getOfferedByUser().getIdUser() != user.getIdUser()) {
                    responseMessage.put("type", "error");
                    responseMessage.put("message", "Oferta no encontrada o no autorizada.");
                } else {
                    // Llamar al servicio para procesar la oferta
                    responseMessage.put("type", "success");
                    responseMessage.put("message", offerService.processOfferStatus(offer, status).message());
                }

            } catch (NumberFormatException e) {
                responseMessage.put("type", "error");
                responseMessage.put("message", "ID de oferta inválido.");
            }
        }

        // Mostrar el mensaje de respuesta
        req.setAttribute("messageType", responseMessage.get("type"));
        req.setAttribute("message", responseMessage.get("message"));

        // Recargar la lista de ofertas pendientes
        offerService.loadPendingOffers(user.getIdUser(), req);
        req.getRequestDispatcher("jsp/OFFERS.jsp").forward(req, resp);
    }


    // Obtener usuario en sesión
    private User getLoggedUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false); // evita crear una nueva sesión si no existe
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect("LoginController?route=enter");
            return null;
        }
        return user;
    }
}
