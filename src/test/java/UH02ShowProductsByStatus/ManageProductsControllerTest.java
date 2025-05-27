package UH02ShowProductsByStatus;

import static org.mockito.Mockito.*;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.http.HttpSession;
import model.dto.SearchResult;
import model.entities.Product;
import model.entities.User;
import model.enums.ProductCategory;
import model.enums.ProductState;
import model.service.ProductService;
import controllers.ManageProductsController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

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

    @Mock
    HttpSession session;

    @Mock
    User mockUser;

    @BeforeEach
    void setup() {
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(mockUser.getUserId()).thenReturn(1);
    }

    @Test
    void whenGetProductsByStateWithValidState_thenSetProductsAndForwardWithoutMessage() throws Exception {
        String stateParam = "New";
        List<Product> products = List.of(
                new Product(1, "Prod1", "Desc", ProductState.New, ProductCategory.Books, "", null)
        );
        SearchResult searchResult = new SearchResult(products, null);

        when(req.getParameter("state")).thenReturn(stateParam);
        when(productService.searchProductsByState(eq(stateParam), eq(1))).thenReturn(searchResult);
        when(req.getRequestDispatcher("jsp/HOME.jsp")).thenReturn(dispatcher);

        controller.getProductsByState(req, resp);

        verify(req).setAttribute("products", products);
        verify(req, never()).setAttribute(eq("message"), any());
        verify(dispatcher).forward(req, resp);
    }

    @Test
    void whenGetProductsByStateWithMessage_thenSetProductsAndMessageAndForward() throws Exception {
        String stateParam = "Repaired";
        List<Product> emptyList = Collections.emptyList();
        SearchResult searchResult = new SearchResult(emptyList, "There are no products with this status");

        when(req.getParameter("state")).thenReturn(stateParam);
        when(productService.searchProductsByState(eq(stateParam), eq(1))).thenReturn(searchResult);
        when(req.getRequestDispatcher("jsp/HOME.jsp")).thenReturn(dispatcher);

        controller.getProductsByState(req, resp);

        verify(req).setAttribute("products", emptyList);
        verify(req).setAttribute("message", "There are no products with this status");
        verify(dispatcher).forward(req, resp);
    }

    @Test
    void whenGetProductsByStateWithSemiNewStateWithDash_thenSetProductsAndForward() throws Exception {
        String stateParam = "Semi-new";
        List<Product> products = List.of(
                new Product(2, "Prod2", "Desc", ProductState.Semi_new, ProductCategory.Books, "", null)
        );
        SearchResult searchResult = new SearchResult(products, null);

        when(req.getParameter("state")).thenReturn(stateParam);
        when(productService.searchProductsByState(eq(stateParam), eq(1))).thenReturn(searchResult);
        when(req.getRequestDispatcher("jsp/HOME.jsp")).thenReturn(dispatcher);

        controller.getProductsByState(req, resp);

        verify(req).setAttribute("products", products);
        verify(req, never()).setAttribute(eq("message"), any());
        verify(dispatcher).forward(req, resp);
    }
}