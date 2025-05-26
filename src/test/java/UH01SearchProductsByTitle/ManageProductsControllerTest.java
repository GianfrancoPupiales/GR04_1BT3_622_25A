package UH01SearchProductsByTitle;

import controllers.ManageProductsController;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.entities.Product;
import model.enums.ProductCategory;
import model.enums.ProductState;
import model.service.ProductService;
import model.dto.SearchResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManageProductsControllerTest {

    @Mock
    ProductService productService;

    @InjectMocks
    ManageProductsController controller;

    @Mock
    HttpServletRequest req;

    @Mock
    HttpServletResponse resp;

    @Mock
    RequestDispatcher dispatcher;

    @Test
    void givenValidTitle_whenGetProductsByTitle_thenSetProductsAndForward() throws Exception {
        String title = "Pen";
        List<Product> filtered = List.of(new Product(1, "Pen Black", "Desc", ProductState.New, ProductCategory.Stationery, "", null));
        SearchResult serviceResult = new SearchResult(filtered, null);

        when(req.getParameter("title")).thenReturn(title);
        when(productService.searchProductsByTitle(title)).thenReturn(serviceResult);
        when(req.getRequestDispatcher("jsp/HOME.jsp")).thenReturn(dispatcher);

        controller.getProductsByTitle(req, resp);

        verify(req).setAttribute("products", filtered);
        verify(req, never()).setAttribute(eq("message"), any());
        verify(dispatcher).forward(req, resp);
    }

    @Test
    void givenTitleWithNoResults_whenGetProductsByTitle_thenSetEmptyProductsAndMessage() throws Exception {
        String title = "Nonexistent";
        List<Product> emptyList = Collections.emptyList();
        SearchResult serviceResult = new SearchResult(emptyList, "No products were found with this title");

        when(req.getParameter("title")).thenReturn(title);
        when(productService.searchProductsByTitle(title)).thenReturn(serviceResult);
        when(req.getRequestDispatcher("jsp/HOME.jsp")).thenReturn(dispatcher);

        controller.getProductsByTitle(req, resp);

        verify(req).setAttribute("products", emptyList);
        verify(req).setAttribute("message", "No products were found with this title");
        verify(dispatcher).forward(req, resp);
    }

    @Test
    void givenEmptyOrWhitespaceTitle_whenGetProductsByTitle_thenReturnAllProducts() throws Exception {
        String title = "  ";
        List<Product> allProducts = List.of(
                new Product(1, "Pen Black", "Desc", ProductState.New, ProductCategory.Stationery, "", null),
                new Product(2, "Notebook", "Desc", ProductState.New, ProductCategory.Stationery, "", null)
        );
        SearchResult serviceResult = new SearchResult(allProducts, null);

        when(req.getParameter("title")).thenReturn(title);
        when(productService.searchProductsByTitle(title)).thenReturn(serviceResult);
        when(req.getRequestDispatcher("jsp/HOME.jsp")).thenReturn(dispatcher);

        controller.getProductsByTitle(req, resp);

        verify(req).setAttribute("products", allProducts);
        verify(req, never()).setAttribute(eq("message"), any());
        verify(dispatcher).forward(req, resp);
    }

    @Test
    void givenTitleLongerThan50Chars_whenGetProductsByTitle_thenSetErrorMessage() throws Exception {
        String longTitle = "a".repeat(51);
        List<Product> allProducts = List.of(new Product(1, "Pen Black", "Desc", ProductState.New, ProductCategory.Stationery, "", null));
        SearchResult serviceResult = new SearchResult(allProducts, "The search text must not exceed 50 characters.");

        when(req.getParameter("title")).thenReturn(longTitle);
        when(productService.searchProductsByTitle(longTitle)).thenReturn(serviceResult);
        when(req.getRequestDispatcher("jsp/HOME.jsp")).thenReturn(dispatcher);

        controller.getProductsByTitle(req, resp);

        verify(req).setAttribute("products", allProducts);
        verify(req).setAttribute("message", "The search text must not exceed 50 characters.");
        verify(dispatcher).forward(req, resp);
    }
}
