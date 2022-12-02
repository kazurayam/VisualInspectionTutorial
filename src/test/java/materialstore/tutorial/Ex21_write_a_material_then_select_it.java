package materialstore.tutorial;

import com.kazurayam.materialstore.filesystem.FileType;
import com.kazurayam.materialstore.filesystem.JobName;
import com.kazurayam.materialstore.filesystem.JobTimestamp;
import com.kazurayam.materialstore.filesystem.Material;
import com.kazurayam.materialstore.filesystem.MaterialstoreException;
import com.kazurayam.materialstore.filesystem.Metadata;
import com.kazurayam.materialstore.filesystem.QueryOnMetadata;
import com.kazurayam.materialstore.filesystem.Store;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Ex21_write_a_material_then_select_it {

    private Store store;
    private JobName jobName;
    private JobTimestamp jobTimestamp;

    @BeforeAll
    public static void beforeAll() throws IOException { TestHelper.initializeOutputDir(); }

    @BeforeEach
    public void setup() throws IOException, MaterialstoreException {
        store = TestHelper.initializeStore(this);
        jobName = new JobName(TestHelper.classNameToJobName(this));
        jobTimestamp = JobTimestamp.now();
    }

    @Test
    public void selectSingleMaterial() throws MaterialstoreException, IOException {
        // write a file into the store
        Metadata metadata = Metadata.NULL_OBJECT;
        store.write(jobName, jobTimestamp, FileType.TXT, metadata, "Hello, world!");
        // select a material that wraps the file
        Material material =
                store.selectSingle(jobName, jobTimestamp, FileType.TXT,
                        QueryOnMetadata.ANY);
        assertNotNull(material);
        List<String> allLines = Files.readAllLines(material.toPath(store));
        assertEquals("Hello, world!", allLines.get(0));
        allLines.forEach((String line) -> {
            System.out.println(line);
        });
    }




}
