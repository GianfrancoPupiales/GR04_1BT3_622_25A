package UH02ShowProductsByStatus;

import model.enums.ProductState;
import model.utils.ProductStateHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductStateHelperTest {

    @Test
    void givenNullOrAllString_thenIsAllOrNullReturnsTrue() {
        assertTrue(ProductStateHelper.isAllOrNull(null), "Null input should return true");
        assertTrue(ProductStateHelper.isAllOrNull("All"), "'All' input should return true");
        assertTrue(ProductStateHelper.isAllOrNull("all"), "'all' input (case insensitive) should return true");
        assertTrue(ProductStateHelper.isAllOrNull("ALL"), "'ALL' input (case insensitive) should return true");
    }

    @Test
    void givenValidStateStrings_thenParseStateReturnsCorrectEnum() {
        assertEquals(ProductState.New, ProductStateHelper.parseState("new").orElse(null));
        assertEquals(ProductState.Repaired, ProductStateHelper.parseState("REPAIRED").orElse(null));
        assertEquals(null, ProductStateHelper.parseState("Semi new").orElse(null));
        assertEquals(ProductState.Semi_new, ProductStateHelper.parseState("Semi_new").orElse(null));
    }

    @Test
    void givenInvalidStateStrings_thenParseStateReturnsEmpty() {
        assertTrue(ProductStateHelper.parseState("InvalidState").isEmpty());
        assertTrue(ProductStateHelper.parseState("Semi+new").isEmpty());
        assertTrue(ProductStateHelper.parseState("").isEmpty());
        assertTrue(ProductStateHelper.parseState("  ").isEmpty());
    }
}
