package materialstore.tutorial;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import com.kazurayam.materialstore.filesystem.Store;
import com.kazurayam.materialstore.filesystem.Stores;

public abstract class TestHelper {

    private static Path currentWorkingDir;
    private static Path testOutputDir;

    static {
        currentWorkingDir = Paths.get(System.getProperty("user.dir"));
        testOutputDir = currentWorkingDir.resolve("build/tmp/testOutput");
    }

    static void initializeOutputDir() throws IOException {
        if (!Files.exists(testOutputDir)) {
            Files.createDirectories(testOutputDir);
        }
    }

    static Path getCWD() {
        return getCurrentWorkingDirectory();
    }

    static Path getCurrentWorkingDirectory() {
        return currentWorkingDir;
    }

    static Store initializeStore(Object testCase) throws IOException {
        Path root = testOutputDir.resolve(testCase.getClass().getSimpleName()).resolve("store");
        if (Files.exists(root)) {
            // delete the store directory recursively
            Files.walk(root)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
        // recreate the store directory
        Files.createDirectories(root);
        return Stores.newInstance(root);
    }

    /**
     *
     * @param testCase a object of which simple class name is "Test010201_write_a_material_then_select_it"
     * @return "write_a_material_then_select_it"
     */
    static String classNameToJobName(Object testCase) {
        String className = testCase.getClass().getSimpleName();
        return className.substring(className.indexOf("_") + 1);
    }

}
