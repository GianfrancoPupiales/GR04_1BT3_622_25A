package UH03ShowProductsByCategory;

import controllers.ManageProductsController;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dto.SearchResult;
import model.entities.Product;
import model.entities.User;
import model.enums.ProductCategory;
import model.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.servlet.http.HttpSession;

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
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    RequestDispatcher dispatcher;

    @Mock
    HttpSession session;

    @Mock
    User mockUser;

    @BeforeEach
    void setup() {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(mockUser.getUserId()).thenReturn(1);
    }

    @Test
    void givenCategoryParam_whenViewProductsByCategory_thenPassResultToView() throws Exception {
        String categoryParam = "Books";
        ProductCategory categoryEnum = ProductCategory.Books;

        when(request.getParameter("category")).thenReturn(categoryParam);

        List<Product> products = List.of(
                new Product(1, "Title1", "Desc", null, categoryEnum, "", null)
        );
        SearchResult searchResult = new SearchResult(products, null);

        when(productService.searchProductsByCategory(eq(categoryParam), eq(1))).thenReturn(searchResult);
        when(request.getRequestDispatcher("jsp/HOME.jsp")).thenReturn(dispatcher);

        controller.viewProductsByCategory(request, response);

        verify(productService).searchProductsByCategory(categoryParam, 1);
        verify(request).setAttribute("products", products);
        verify(request).setAttribute("selectedCategory", categoryEnum);
        verify(request, never()).setAttribute(eq("message"), any());
        verify(dispatcher).forward(request, response);
    }

    @Test
    void givenSearchResultWithMessage_whenViewProductsByCategory_thenSetMessage() throws Exception {
        String categoryParam = "Electronics";
        ProductCategory categoryEnum = ProductCategory.Electronics;

        when(request.getParameter("category")).thenReturn(categoryParam);

        SearchResult searchResult = new SearchResult(Collections.emptyList(), "There are no products in this category");

        when(productService.searchProductsByCategory(eq(categoryParam), eq(1))).thenReturn(searchResult);
        when(request.getRequestDispatcher("jsp/HOME.jsp")).thenReturn(dispatcher);

        controller.viewProductsByCategory(request, response);

        verify(productService).searchProductsByCategory(categoryParam, 1);
        verify(request).setAttribute("products", Collections.emptyList());
        verify(request).setAttribute("selectedCategory", categoryEnum);
        verify(request).setAttribute("message", "There are no products in this category");
        verify(dispatcher).forward(request, response);
    }
}
