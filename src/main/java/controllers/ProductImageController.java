package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@WebServlet("/product-images/*")
public class ProductImageController extends HttpServlet {

    private final String basePath = "/app/uploads/products";

    @Override
    public void init() throws ServletException {
        // Crear la carpeta base si no existe
        File baseDir = new File(basePath);
        if (!baseDir.exists()) {
            boolean created = baseDir.mkdirs();
            if (!created) {
                throw new ServletException("The base directory could not be created: " + basePath);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestedImage = req.getPathInfo(); // /nombreimagen.jpg

        if (requestedImage == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        File image = new File(basePath, requestedImage);
        if (!image.exists() || !image.isFile()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String mimeType = getServletContext().getMimeType(image.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        resp.setContentType(mimeType);

        Files.copy(image.toPath(), resp.getOutputStream());
    }
}
