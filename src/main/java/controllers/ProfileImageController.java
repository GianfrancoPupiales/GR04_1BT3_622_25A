package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@WebServlet("/profile-images/*")
public class ProfileImageController extends HttpServlet {

    // Ruta donde están almacenadas las fotos en el disco
    private final String basePath = "C:\\trukea\\images\\profiles";

    @Override
    public void init() throws ServletException {
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
        // Obtiene la parte variable de la URL después de /profile-images/
        String requestedImage = req.getPathInfo(); // ej. /foto.jpg

        if (requestedImage == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        File imageFile = new File(basePath, requestedImage);
        if (!imageFile.exists() || !imageFile.isFile()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Detecta el tipo MIME para la imagen
        String mimeType = getServletContext().getMimeType(imageFile.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        resp.setContentType(mimeType);

        // Envía el contenido del archivo imagen al navegador
        Files.copy(imageFile.toPath(), resp.getOutputStream());
    }
}