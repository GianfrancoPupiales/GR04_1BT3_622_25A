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
import model.service.OfferService;
import model.service.OfferService.ResponseMessage;

import java.io.IOException;
import java.util.List;

@WebServlet("/RespondOfferController")
public class RespondOfferController extends HttpServlet {

    private final OfferDAO offerDAO = new OfferDAO();
    private final OfferService offerService = new OfferService();

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
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect("LoginController?route=enter");
            return;
        }

        try {
            String status = req.getParameter("status");
            int offerId = Integer.parseInt(req.getParameter("offerId"));

            Offer offer = offerDAO.findById(offerId);
            ResponseMessage message = offerService.processOfferStatus(offer, status);

            req.setAttribute("messageType", message.type());
            req.setAttribute("message", message.message());

            // Actualizar la lista de ofertas pendientes
            List<Offer> offers = offerDAO.findPendingOffersByUserId(user.getIdUser());
            req.setAttribute("offers", offers);

            req.getRequestDispatcher("jsp/OFFERS.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            req.setAttribute("messageType", "error");
            req.setAttribute("message", "ID de oferta inválido.");
            req.getRequestDispatcher("jsp/OFFERS.jsp").forward(req, resp);
        }
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
