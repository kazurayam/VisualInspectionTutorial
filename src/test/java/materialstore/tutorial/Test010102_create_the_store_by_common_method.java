package materialstore.tutorial;

import com.kazurayam.materialstore.filesystem.Store;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * This code shows hot to create a "store" directory
 * for each individual JUnit test case, by calling
 * the method implemented in the Base class.
 */
public class Test010102_create_the_store_by_common_method extends Base {

    private Store store;

    @BeforeAll
    public static void beforeAll() throws IOException { initializeTestOutput(); }

    @BeforeEach
    public void setup() throws IOException {
        store = initializeRoot(this.getClass().getSimpleName());
    }

    @Test
    public void show() {
        System.out.println("the store was created at " + store.getRoot().toString());
    }

}
