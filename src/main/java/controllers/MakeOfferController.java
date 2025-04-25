package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.OfferDAO;
import model.dao.ProductDAO;
import model.entities.Offer;
import model.entities.Product;
import model.entities.User;

import java.io.IOException;
import java.io.Serial;
import java.util.List;

@WebServlet("/MakeOfferController")
public class MakeOfferController extends HttpServlet{
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.router(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.router(req, resp);
    }

    private void router(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Control logic
        String route = (req.getParameter("route") == null) ? "list" : req.getParameter("route");

        switch (route) {
            case "list":
                this.viewMyProducts(req, resp);
                break;
            case "select":
                this.selectProduct(req, resp);
                break;
            case "offer":
                this.makeOffer(req, resp);
                break;
            case "confirm":
                this.confirmOffer(req, resp);
                break;
            default:
                throw new IllegalArgumentException("Unknown route: " + route);
        }
    }

    private void selectProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String viewType = req.getParameter("view"); // "home" o "user"
        String id = req.getParameter("id");
        ProductDAO dao = new ProductDAO();
        List<Product> availableProducts;
        // Mostrar productos del usuario (PRODUCT_OFF.jsp)
        HttpSession session = req.getSession();
        Product product = (Product) session.getAttribute("product");
        availableProducts = dao.findProductById(Integer.parseInt(id));
        req.setAttribute("availableProducts", availableProducts);
        req.getRequestDispatcher("jsp/PROD_OFFER.jsp").forward(req, resp);
    }

    private void makeOffer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        List<Product> products = new ProductDAO().findProductsByIdUser(user.getIdUser());
        req.setAttribute("products", products);
        req.setAttribute("route", "list");
        req.getRequestDispatcher("jsp/OFFER.jsp").forward(req, resp);
    }

    private void confirmOffer(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Offer offer = parseOfferFromRequest(req);
        OfferDAO offerDAO = new OfferDAO();
        if (offerDAO.create(offer)) {
            req.setAttribute("messageType", "info");
            req.setAttribute("message", "Offer created successfully.");
            req.getRequestDispatcher("MakeOfferController?route=list").forward(req, resp);
        } else {
            req.setAttribute("messageType", "error");
            req.setAttribute("message", "Failed to create offer.");
            req.getRequestDispatcher("MakeOfferController?route=list").forward(req, resp);
        }
    }

    private void viewMyProducts(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String viewType = req.getParameter("view"); // "home" o "user"
        ProductDAO dao = new ProductDAO();
        List<Product> products;
        // Mostrar productos del usuario (PRODUCT.jsp)
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        products = dao.findAvailableProductsByIdUser(user.getIdUser());
        req.setAttribute("products", products);
        req.getRequestDispatcher("jsp/OFFER.jsp").forward(req, resp);
    }

    private Offer parseOfferFromRequest(HttpServletRequest req) {
        int idOffer = 0;
        String txtId = req.getParameter("txtIdOffer");

        if (txtId != null && !txtId.trim().isEmpty()) {
            try {
                idOffer = Integer.parseInt(txtId);
            } catch (NumberFormatException e) {
                System.out.println("Error al convertir el ID: " + e.getMessage());
            }
        }

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String offeredProducts = req.getParameter("listOfferedProducts");
        String productToOffer = req.getParameter("txtProductToOffer");

        return new Offer(idOffer, offeredProducts, productToOffer);
    }
}
