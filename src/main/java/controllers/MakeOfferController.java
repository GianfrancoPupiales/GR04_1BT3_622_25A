package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.entities.Offer;
import model.entities.Product;
import model.entities.User;
import model.service.OfferService;
import model.service.ProductService;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/MakeOfferController")
public class MakeOfferController extends HttpServlet {
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
        int idProductToOffer = Integer.parseInt(req.getParameter("id"));
        req.getSession().setAttribute("idProductToOffer", String.valueOf(idProductToOffer));
        ProductService productService = new ProductService();
        Product productToOffer = productService.findById(idProductToOffer);
        req.setAttribute("product", productToOffer);
        req.getRequestDispatcher("jsp/PROD_OFFER.jsp").forward(req, resp);
    }


    private void makeOffer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getUser(req);
        List<Product> products = new ProductService().findAvailableProductsByUserId(user.getIdUser());
        req.setAttribute("products", products);
        req.getRequestDispatcher("jsp/OFFER.jsp").forward(req, resp);
    }

    private static User getUser(HttpServletRequest req) {
        HttpSession session = req.getSession();
        return (User) session.getAttribute("user");
    }

    private void confirmOffer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        String productToOfferId = (String) session.getAttribute("idProductToOffer");

        if (productToOfferId == null) {
            session.setAttribute("message", "No product selected to offer.");
            session.setAttribute("messageType", "error");
            resp.sendRedirect(req.getContextPath() + "/ManageProductsController?route=list&view=home");
            return;
        }

        Offer offer = parseOfferFromRequest(req);

        ProductService productService = new ProductService();
        Product productToOffer = productService.findById(Integer.parseInt(productToOfferId));
        offer.setProductToOffer(productToOffer);

        User user = getUser(req);
        offer.setOfferedByUser(user);

        String listOfferedProductsParam = req.getParameter("listOfferedProducts");
        if (listOfferedProductsParam != null && !listOfferedProductsParam.trim().isEmpty()) {
            String[] idStrings = listOfferedProductsParam.split(",");
            List<Product> offeredProducts = new ArrayList<>();

            for (String idStr : idStrings) {
                try {
                    int id = Integer.parseInt(idStr.trim());
                    Product offeredProduct = productService.findById(id);
                    if (offeredProduct != null) {
                        offeredProducts.add(offeredProduct);
                    }
                } catch (NumberFormatException e) {
                    // Log error
                }
            }
            offer.setOfferedProducts(offeredProducts);
        }

        OfferService offerService = new OfferService();
        boolean created = offerService.createOffer(offer);

        session.removeAttribute("idProductToOffer"); // Limpiar sesion

        if (created) {
            session.setAttribute("message", "Offer created successfully.");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", "Failed to create offer.");
            session.setAttribute("messageType", "error");
        }
        // Redirigir a home para evitar repost
        resp.sendRedirect(req.getContextPath() + "/ManageProductsController?route=list&view=home");
    }

    private void viewMyProducts(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = new ProductService();
        List<Product> products;
        User user = getUser(req);
        products = productService.findAvailableProductsByUserId(user.getUserId());
        req.setAttribute("products", products);
        req.getRequestDispatcher("jsp/OFFER.jsp").forward(req, resp);
    }

    private Offer parseOfferFromRequest(HttpServletRequest req) {
        int idOffer = parseOfferId(req.getParameter("txtIdOffer"));
        Product productToOffer = parseProduct(req.getParameter("productToOffer"));
        List<Product> offeredProducts = parseOfferedProducts(req.getParameterValues("selectedProducts"));

        return new Offer(idOffer, offeredProducts, productToOffer, "pending");
    }

    private int parseOfferId(String txtId) {
        if (txtId == null || txtId.trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(txtId.trim());
        } catch (NumberFormatException e) {
            System.out.println("Error converting ID: " + e.getMessage());
            return 0;
        }
    }

    private Product parseProduct(String productIdStr) {
        if (productIdStr == null || productIdStr.trim().isEmpty()) {
            return null;
        }
        try {
            int id = Integer.parseInt(productIdStr.trim());
            return new ProductService().findById(id);
        } catch (NumberFormatException e) {
            System.out.println("Error converting product ID to offer: " + e.getMessage());
            return null;
        }
    }

    private List<Product> parseOfferedProducts(String[] offeredProductIds) {
        List<Product> offeredProducts = new ArrayList<>();
        if (offeredProductIds == null || offeredProductIds.length == 0) {
            System.out.println("Empty list of products");
            return offeredProducts;
        }

        ProductService productService = new ProductService();

        for (String idStr : offeredProductIds) {
            if (idStr == null || idStr.trim().isEmpty()) {
                continue;
            }
            try {
                int id = Integer.parseInt(idStr.trim());
                Product product = productService.findById(id);
                if (product != null) {
                    offeredProducts.add(product);
                    System.out.println(product);
                }
            } catch (NumberFormatException e) {
                System.out.println("Error converting product ID: " + e.getMessage());
            }
        }
        return offeredProducts;
    }
}
