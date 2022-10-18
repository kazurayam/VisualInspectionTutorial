package materialstore.tutorial;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.kazurayam.materialstore.filesystem.Store;
import com.kazurayam.materialstore.filesystem.Stores;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class Base {

    protected static Path currentDir;
    protected static Path testOutputDir;
    protected Store store;

    protected static void initializeTestOutput() throws IOException {
        currentDir = Paths.get(System.getProperty("user.dir"));
        testOutputDir = currentDir.resolve("build/tmp/testOutput");
        if (!Files.exists(testOutputDir)) {
            Files.createDirectories(testOutputDir);
        }
    }

    protected static Path getCurrentDir() {
        return currentDir;
    }

    protected final Store initializeStore(Object testCase) throws IOException {
        Path root = testOutputDir.resolve(testCase.getClass().getSimpleName()).resolve("store");
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }
        return Stores.newInstance(root);
    }
}
