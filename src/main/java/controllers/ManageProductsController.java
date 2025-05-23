package controllers;

import java.io.IOException;
import java.io.Serial;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.entities.Product;
import model.entities.User;
import model.enums.ProductCategory;
import model.enums.ProductState;
import model.service.FileStorageService;
import model.service.ProductService;

@MultipartConfig
@WebServlet("/ManageProductsController")
public class ManageProductsController extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ProductService productService = new ProductService();

    private ProductService getProductService() {
        return productService;
    }

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
                String view = Optional.ofNullable(req.getParameter("view")).filter(v -> !v.isEmpty()).orElse("user");
                if ("home".equals(view)) {
                    viewProducts(req, resp);
                } else {
                    viewMyProducts(req, resp);
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
                this.confirmRemove(req, resp);
                break;
            case "select":
                this.selectProduct(req, resp);
                break;
            default:
                throw new IllegalArgumentException("Unknown route: " + route);
        }
    }

    private void selectProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("idProduct");
        if (idParam == null || !idParam.matches("\\d+")) {
            resp.sendRedirect("ManageProductsController?route=list");
            return;
        }

        int idProduct = Integer.parseInt(idParam);
        Product product = getProductService().findProductById(idProduct);

        if (product != null) {
            req.setAttribute("product", product);
            req.getRequestDispatcher("jsp/PRODUCT.jsp").forward(req, resp);
        } else {
            forwardWithMessage(req, resp, "error", "Product not found.");
        }
    }

    private void addProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Product> products = getProductsByUserId(req);
        req.setAttribute("products", products);
        req.setAttribute("route", "add");
        req.getRequestDispatcher("jsp/MY_PRODUCT.jsp").forward(req, resp);
    }

    private void confirmRemove(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        int idProduct = Integer.parseInt(req.getParameter("idProduct"));
        if (getProductService().removeProduct(idProduct)) {
            forwardWithMessage(req, resp, "info", "Product deleted successfully.");
        } else {
            forwardWithMessage(req, resp, "error", "Failed to delete Product.");
        }
    }

    private void deleteProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        int idProduct = Integer.parseInt(req.getParameter("idProduct"));
        HttpSession session = req.getSession();
        Product product = getProductService().findProductById(idProduct);
        List<Product> products = getProductsByUserId(req);

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
        int idProduct = Integer.parseInt(req.getParameter("idProduct"));
        Product product = getProductService().findProductById(idProduct);

        List<Product> products = getProductsByUserId(req);
        req.setAttribute("products", products);

        if (product != null) {
            req.setAttribute("product", product);
            req.getRequestDispatcher("jsp/MY_PRODUCT.jsp").forward(req, resp);
        } else {
            session.setAttribute("message", "Product not found");
            resp.sendRedirect("ManageProductsController?route=list");
        }
    }

    private void forwardWithMessage(HttpServletRequest req, HttpServletResponse resp, String messageType, String message) throws ServletException, IOException {
        req.setAttribute("messageType", messageType);
        req.setAttribute("message", message);
        req.getRequestDispatcher("ManageProductsController?route=list").forward(req, resp);
    }

    private void forwardProductsView(HttpServletRequest req, HttpServletResponse resp, List<Product> products, String jspPath) throws ServletException, IOException {
        req.setAttribute("products", products);
        req.getRequestDispatcher(jspPath).forward(req, resp);
    }

    private static User getUser(HttpServletRequest req) {
        HttpSession session = req.getSession();
        return (User) session.getAttribute("user");
    }

    private List<Product> getProductsByUserId(HttpServletRequest req) {
        return getProductService().findAvailableProductsByUserId(getUser(req).getUserId());
    }

    private void saveExistingProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Product product = parseProductFromRequest(req);

        product.setTitle(req.getParameter("txtTitle"));
        product.setDescription(req.getParameter("txtDescription"));

        String stateStr = req.getParameter("txtState");
        ProductState state = null;
        try {
            if (stateStr != null && !stateStr.isEmpty()) {
                stateStr = stateStr.replace("-", "_").replace(" ", "_");
                state = ProductState.valueOf(stateStr);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid state value: " + stateStr);
            state = ProductState.New; // Valor por defecto si falla
        }
        product.setState(state);

        processProductSave(req, resp, product, true);
    }


    private void saveNewProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Product product = parseProductFromRequest(req);
        processProductSave(req, resp, product, false);
    }

    private Product parseProductFromRequest(HttpServletRequest req) throws IOException, ServletException {
        String txtId = req.getParameter("txtIdProduct");
        int idProduct = parseProductId(txtId);
        String title = req.getParameter("txtTitle");
        String description = req.getParameter("txtDescription");
        String categoryStr = req.getParameter("category");
        String stateStr = req.getParameter("txtState");
        String existingPhoto = req.getParameter("existingPhoto"); // Foto previa si no se sube nueva

        // Manejo de estado
        ProductState state = null;
        try {
            if (stateStr != null && !stateStr.isEmpty()) {
                // Convertimos guiones medios a guion bajo para que coincida con enum
                stateStr = stateStr.replace("-", "_").replace(" ", "_");
                state = ProductState.valueOf(stateStr);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid state value: " + stateStr);
            state = ProductState.New; // Estado por defecto
        }

        // Manejo de categoria
        ProductCategory category = null;
        try {
            if (categoryStr != null && !categoryStr.isEmpty()) {
                category = ProductCategory.valueOf(categoryStr);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid category value: " + categoryStr);
            // Manejar error o asignar un valor por defecto
            category = ProductCategory.Other;
        }

        // Manejo de foto subida
        Part photoPart = req.getPart("photo");
        String photoFileName;
        if (photoPart != null && photoPart.getSize() > 0) {
            String uploadPath = "C:\\trukea\\images\\products";
            FileStorageService storageService = new FileStorageService(uploadPath);
            photoFileName = storageService.savePhoto(photoPart);

        } else {
            // Si no hay foto nueva, conservar la existente
            photoFileName = existingPhoto;
        }

        User user = getUser(req);

        return new Product(idProduct, title, description, state, category, photoFileName, user);
    }

    private int parseProductId(String idParam) {
        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                return Integer.parseInt(idParam);
            } catch (NumberFormatException e) {
                System.out.println("Error al convertir el ID: " + e.getMessage());
            }
        }
        return 0;
    }

    private void processProductSave(HttpServletRequest req, HttpServletResponse resp, Product product, boolean isUpdate) throws IOException, ServletException {
        boolean success = isUpdate ? getProductService().updateProduct(product) : getProductService().createProduct(product);

        String messageType = success ? "info" : "error";
        String message = success
                ? (isUpdate ? "Product updated successfully." : "Product created successfully.")
                : (isUpdate ? "Failed to update product." : "Failed to create product.");

        forwardWithMessage(req, resp, messageType, message);
    }

    private void viewProducts(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Product> products = getProductService().findAvailableProductsExceptUser(getUser(req).getUserId());
        forwardProductsView(req, resp, products, "jsp/HOME.jsp");
    }

    private void viewMyProducts(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Product> products = getProductsByUserId(req);
        forwardProductsView(req, resp, products, "jsp/MY_PRODUCT.jsp");
    }
}