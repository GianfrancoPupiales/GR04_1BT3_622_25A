package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.OfferDAO;
import model.entities.Offer;
import model.entities.User;

import java.io.IOException;
import java.util.List;

@WebServlet("/RespondOfferController")
public class RespondOfferController extends HttpServlet {

    private final OfferDAO offerDAO = new OfferDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String route = req.getParameter("route");
        if ("list".equals(route)) {
            this.listOffers(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid route");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String status = req.getParameter("status");
        int offerId = Integer.parseInt(req.getParameter("offerId"));
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        Offer offer = offerDAO.findById(offerId);
        if (offer == null) {
            req.setAttribute("messageType", "error");
            req.setAttribute("message", "Offer not found.");
        } else {
            // Actualizar el estado de la oferta
            offer.setStatus(status);
            offerDAO.update(offer);

            // Configurar el mensaje basado en el estado
            if ("accepted".equals(status)) {
                req.setAttribute("messageType", "success");
                req.setAttribute("message", "¡Felicidades por tu intercambio!");
            } else if ("rejected".equals(status)) {
                req.setAttribute("messageType", "warning");
                req.setAttribute("message", "Lo siento, tu oferta ha sido rechazada.");
            } else {
                req.setAttribute("messageType", "error");
                req.setAttribute("message", "Invalid status.");
            }
        }

        // Actualizar la lista de ofertas pendientes
        List<Offer> offers = offerDAO.findPendingOffersByUserId(user.getIdUser());
        req.setAttribute("offers", offers);

        // Regresar a la misma página con las ofertas actualizadas
        req.getRequestDispatcher("jsp/OFFERS.jsp").forward(req, resp);
    }



    private void listOffers(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user != null) {
            List<Offer> offers = offerDAO.findPendingOffersByUserId(user.getIdUser());
            req.setAttribute("offers", offers);
            req.getRequestDispatcher("jsp/OFFERS.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("LoginController?route=enter");
        }
    }
}