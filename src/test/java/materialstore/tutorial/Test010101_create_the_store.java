package materialstore.tutorial;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.kazurayam.materialstore.filesystem.Store;
import com.kazurayam.materialstore.filesystem.Stores;

/**
 * This code shows how to create a "store" directory
 * for each individual JUnit test case.
 */
public class Test010101_create_the_store {
    private static Path testOutputDir;
    private Store store;

    @BeforeAll
    public static void beforeAll() throws IOException {
        Path cwd = Paths.get(System.getProperty("user.dir"));
        testOutputDir = cwd.resolve("build/tmp/testOutput");
        if (!Files.exists(testOutputDir)) {
            Files.createDirectories(testOutputDir);
        }
    }

    @BeforeEach
    public void setup() throws IOException {
        Path root = testOutputDir
                .resolve(this.getClass().getSimpleName())
                .resolve("store");
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }
        store = Stores.newInstance(root);
    }

    @Test
    public void showStorePath() {
        System.out.println(store.getRoot().toString());
    }
}
