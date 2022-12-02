package materialstore.tutorial;

import com.kazurayam.materialstore.filesystem.FileType;
import com.kazurayam.materialstore.filesystem.JobName;
import com.kazurayam.materialstore.filesystem.JobTimestamp;
import com.kazurayam.materialstore.filesystem.Material;
import com.kazurayam.materialstore.filesystem.MaterialList;
import com.kazurayam.materialstore.filesystem.MaterialstoreException;
import com.kazurayam.materialstore.filesystem.Metadata;
import com.kazurayam.materialstore.filesystem.Store;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Ex23_write_multiple_materials_with_unique_metadata {

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
    public void testIt() throws MaterialstoreException, IOException {
        Metadata metadata = new Metadata.Builder().put("id", "001").build();
        Material material = store.write(jobName, jobTimestamp, FileType.TXT,
                metadata, "Hello, how are you?");
        assertTrue(Files.exists(material.toPath(store)));
        List<String> lines = Files.readAllLines(material.toPath(store));
        assertTrue(lines.get(0).contains("Hello"));
        //
        metadata = new Metadata.Builder().put("id", "002").build();
        material = store.write(jobName, jobTimestamp, FileType.TXT,
                metadata, "Fine, thank you. And you?");
        assertTrue(Files.exists(material.toPath(store)));
        lines = Files.readAllLines(material.toPath(store));
        assertTrue(lines.get(0).contains("Fine"));
        //
        //
        metadata = new Metadata.Builder().put("id", "003").build();
        material = store.write(jobName, jobTimestamp, FileType.TXT,
                metadata, "Very well, thank you.");
        assertTrue(Files.exists(material.toPath(store)));
        lines = Files.readAllLines(material.toPath(store));
        assertTrue(lines.get(0).contains("Very"));
        //
        MaterialList materialList = store.select(jobName, jobTimestamp);
        assertEquals(3, materialList.size());
    }
}
