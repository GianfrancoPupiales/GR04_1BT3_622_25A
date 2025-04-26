package controllers;

import java.io.IOException;
import java.io.Serial;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.ProductDAO;
import model.entities.Product;
import model.entities.User;

@WebServlet("/ManageProductsController")
public class ManageProductsController extends HttpServlet {

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
                String viewType = req.getParameter("view");
                if (viewType == null || viewType.isEmpty()) {
                    viewType = "user"; // Valor por defecto si no viene el parámetro "view"
                }
                if ("home".equals(viewType)) {
                    this.viewProducts(req, resp);
                } else if ("user".equals(viewType)) {
                    this.viewMyProducts(req, resp);
                } else {
                    throw new IllegalArgumentException("Unknown view type: " + viewType);
                }
                break;
            case "add":
                this.addProduct(req, resp);
                break;
            case "saveNew":
                this.saveNewProduct(req, resp);
                break;
            case "edit":
                this.editProduct(req, resp);
                break;
            case "saveExisting":
                this.saveExistingProduct(req, resp);
                break;
            case "delete":
                this.deleteProduct(req, resp);
                break;
            case "accept":
                this.accept(req, resp);
                break;
            default:
                throw new IllegalArgumentException("Unknown route: " + route);
        }
    }

    private void accept(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        int idProduct = Integer.parseInt(req.getParameter("idProduct"));
        ProductDAO productDAO = new ProductDAO();
        if (productDAO.remove(idProduct)) {
            req.setAttribute("messageType", "info");
            req.setAttribute("message", "Product deleted successfully.");
            req.getRequestDispatcher("ManageProductsController?route=list").forward(req, resp);
        } else {
            req.setAttribute("messageType", "error");
            req.setAttribute("message", "Failed to delete Product.");
            req.getRequestDispatcher("ManageProductsController?route=list").forward(req, resp);
        }
    }

    private void addProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        List<Product> products = new ProductDAO().findProductsByIdUser(user.getIdUser());
        req.setAttribute("products", products);
        req.setAttribute("route", "add");
        req.getRequestDispatcher("jsp/MY_PRODUCT.jsp").forward(req, resp);
    }

    private void deleteProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        int idProduct = Integer.parseInt(req.getParameter("idProduct"));
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        ProductDAO productDAO = new ProductDAO();
        Product product = productDAO.findById(idProduct);
        List<Product> products = productDAO.findProductsByIdUser(user.getIdUser());

        if (product != null) {
            req.setAttribute("product", product);
            req.setAttribute("products", products);
            req.getRequestDispatcher("jsp/MY_PRODUCT.jsp").forward(req, resp);
        } else {
            session.setAttribute("message", "Product could not be found");
            resp.sendRedirect("ManageProductsController?route=list");
        }
    }

    private void editProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        int idProduct = Integer.parseInt(req.getParameter("idProduct"));
        ProductDAO productDAO = new ProductDAO();
        Product product = productDAO.findById(idProduct);

        List<Product> products = productDAO.findProductsByIdUser(user.getIdUser());
        req.setAttribute("products", products);

        if (product != null) {
            req.setAttribute("product", product);
            req.getRequestDispatcher("jsp/MY_PRODUCT.jsp").forward(req, resp);
        } else {
            session.setAttribute("message", "Product not found");
            resp.sendRedirect("ManageProductsController?route=list");
        }
    }

    private void saveExistingProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        ProductDAO productDAO = new ProductDAO();
        Product product = parseProductFromRequest(req);
        // Solo actualizamos lo que viene del formulario
        product.setTitle(req.getParameter("txtTitle"));
        product.setDescription(req.getParameter("txtDescription"));
        product.setState(req.getParameter("txtState"));

        if (productDAO.update(product)) {
            req.setAttribute("messageType", "info");
            req.setAttribute("message", "Product updated successfully.");
        } else {
            req.setAttribute("messageType", "error");
            req.setAttribute("message", "Failed to update product.");
        }

        req.getRequestDispatcher("ManageProductsController?route=list").forward(req, resp);
    }


    private void saveNewProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Product product = parseProductFromRequest(req);
        ProductDAO productDAO = new ProductDAO();
        if (productDAO.create(product)) {
            req.setAttribute("messageType", "info");
            req.setAttribute("message", "Product created successfully.");
            req.getRequestDispatcher("ManageProductsController?route=list").forward(req, resp);
        } else {
            req.setAttribute("messageType", "error");
            req.setAttribute("message", "Failed to create product.");
            req.getRequestDispatcher("ManageProductsController?route=list").forward(req, resp);
        }
    }

    private void viewProducts(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.findAll();
        req.setAttribute("products", products);
        req.getRequestDispatcher("jsp/HOME.jsp").forward(req, resp);
    }


    private void viewMyProducts(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.findProductsByIdUser(user.getIdUser());
        req.setAttribute("products", products);
        req.getRequestDispatcher("jsp/MY_PRODUCT.jsp").forward(req, resp);
    }

    private Product parseProductFromRequest(HttpServletRequest req) {
        int idProduct = 0;
        String txtId = req.getParameter("txtIdProduct");

        if (txtId != null && !txtId.trim().isEmpty()) {
            try {
                idProduct = Integer.parseInt(txtId);
            } catch (NumberFormatException e) {
                System.out.println("Error al convertir el ID: " + e.getMessage());
            }
        }

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String title = req.getParameter("txtTitle");
        String description = req.getParameter("txtDescription");
        String state = req.getParameter("txtState");

        return new Product(idProduct, title, description, state, user);
    }
}