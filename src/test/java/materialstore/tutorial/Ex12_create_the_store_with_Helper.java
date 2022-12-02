package materialstore.tutorial;

import com.kazurayam.materialstore.filesystem.Store;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * This code shows hot to create a "store" directory
 * for each individual JUnit test case, by calling
 * the methods implemented in the Base class.
 */
public class Ex12_create_the_store_with_Helper {

    private Store store;

    @BeforeAll
    public static void beforeAll() throws IOException { TestHelper.initializeOutputDir(); }

    @BeforeEach
    public void setup() throws IOException {
        store = TestHelper.initializeStore(this);
    }

    @Test
    public void showStorePath() {
        System.out.println(store.getRoot().toString());
    }

}
