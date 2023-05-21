package my.sample;

import com.kazurayam.materialstore.core.MaterialstoreException;
import com.kazurayam.materialstore.core.Store;
import com.kazurayam.materialstore.core.Stores;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class T09TwinsDiff {

    private static Path projectDir;
    private static Path outputDir;
    private Path root;
    private Store store;

    @BeforeAll
    public static void beforeAll() throws IOException {
        projectDir = Paths.get(".").toAbsolutePath();
        outputDir = projectDir.resolve("build/tmp/testOutput")
                .resolve(T09TwinsDiff.class.getName());
        if (Files.exists(outputDir)) {
            FileUtils.deleteDirectory(outputDir.toFile());
        }
        Files.createDirectories(outputDir);
    }

    @BeforeEach
    public void beforeEach() {
        root = outputDir.resolve("store");
        store = Stores.newInstance(root);
    }

    @Test
    public void perform_twins_diff() throws MaterialstoreException {
        throw new MaterialstoreException("TODO");
    }
}
