package UH03ShowProductsByCategory;

import model.enums.ProductCategory;
import model.utils.ProductCategoryHelper;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductCategoryHelperTest {

    @Test
    void givenValidCategoryString_whenParseCategory_thenReturnCategory() {
        Optional<ProductCategory> category = ProductCategoryHelper.parseCategory("Books");
        assertTrue(category.isPresent());
        assertEquals(ProductCategory.Books, category.get());
    }

    @Test
    void givenValidCategoryStringWithDifferentCase_whenParseCategory_thenReturnCategory() {
        Optional<ProductCategory> category = ProductCategoryHelper.parseCategory("electronics");
        assertTrue(category.isPresent());
        assertEquals(ProductCategory.Electronics, category.get());
    }

    @Test
    void givenNullOrBlankString_whenParseCategory_thenReturnEmpty() {
        assertTrue(ProductCategoryHelper.parseCategory(null).isEmpty());
        assertTrue(ProductCategoryHelper.parseCategory("").isEmpty());
        assertTrue(ProductCategoryHelper.parseCategory("   ").isEmpty());
    }

    @Test
    void givenInvalidCategoryString_whenParseCategory_thenReturnEmpty() {
        assertTrue(ProductCategoryHelper.parseCategory("InvalidCategory").isEmpty());
        assertTrue(ProductCategoryHelper.parseCategory("123").isEmpty());
    }

    @Test
    void givenNullOrAllString_whenIsAllOrNull_thenReturnTrue() {
        assertTrue(ProductCategoryHelper.isAllOrNull(null));
        assertTrue(ProductCategoryHelper.isAllOrNull("All"));
        assertTrue(ProductCategoryHelper.isAllOrNull("all"));
        assertTrue(ProductCategoryHelper.isAllOrNull("ALL"));
    }

    @Test
    void givenOtherStrings_whenIsAllOrNull_thenReturnFalse() {
        assertFalse(ProductCategoryHelper.isAllOrNull("Books"));
        assertFalse(ProductCategoryHelper.isAllOrNull("electronics"));
        assertFalse(ProductCategoryHelper.isAllOrNull(""));
        assertFalse(ProductCategoryHelper.isAllOrNull(" "));
        assertFalse(ProductCategoryHelper.isAllOrNull("SomeOtherString"));
    }
}
