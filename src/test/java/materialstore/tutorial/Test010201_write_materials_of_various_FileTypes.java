package materialstore.tutorial;

import com.kazurayam.materialstore.filesystem.FileType;
import com.kazurayam.materialstore.filesystem.JobName;
import com.kazurayam.materialstore.filesystem.JobTimestamp;
import com.kazurayam.materialstore.filesystem.Material;
import com.kazurayam.materialstore.filesystem.MaterialstoreException;
import com.kazurayam.materialstore.filesystem.Metadata;
import com.kazurayam.materialstore.filesystem.Store;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This code shows how to write a text file into the store.
 */
public class Test010201_write_materials_of_various_FileTypes extends Base {

    private Store store;
    private static JobName jobName;
    private JobTimestamp jobTimestamp;

    @BeforeAll
    public static void beforeAll() throws IOException { initializeTestOutput(); }

    @BeforeEach
    public void setup() throws IOException {
        store = initializeStore(this);
    }

    @Test
    public void writeTXT() throws MaterialstoreException {
        jobName = new JobName("writeTXT");
        jobTimestamp = JobTimestamp.now();
        Metadata metadata = Metadata.NULL_OBJECT;
        String text = "Hello, world!";
        Material material = store.write(jobName, jobTimestamp, FileType.TXT, metadata, text);
        assertTrue(Files.exists(material.toPath(store)));
    }

    @Test
    public void writePNG() throws MaterialstoreException {
        jobName = new JobName("writePNG");
        jobTimestamp = JobTimestamp.now();
        Metadata metadata = Metadata.NULL_OBJECT;
        Path png = getCurrentDir().resolve("src/test/resources/images/apple.png");
        Material material = store.write(jobName, jobTimestamp, FileType.PNG, metadata, png);
        assertTrue(Files.exists(material.toPath(store)));
    }

    @Test
    public void writeCSV() throws MaterialstoreException {
        jobName = new JobName("writeCSV");
        jobTimestamp = JobTimestamp.now();
        Metadata metadata = Metadata.NULL_OBJECT;
        Path csv = getCurrentDir().resolve("src/test/resources/tabular/cart.csv");
        Material material = store.write(jobName, jobTimestamp, FileType.CSV, metadata, csv);
        assertTrue(Files.exists(material.toPath(store)));
    }

    @Test
    public void writeXLSX() throws MaterialstoreException {
        jobName = new JobName("writeXLSX");
        jobTimestamp = JobTimestamp.now();
        Metadata metadata = Metadata.NULL_OBJECT;
        Path xlsx = getCurrentDir().resolve("src/test/resources/tabular/cart.xlsx");
        Material material = store.write(jobName, jobTimestamp, FileType.XLSX, metadata, xlsx);
        assertTrue(Files.exists(material.toPath(store)));
    }

    @Test
    public void writePDF() throws MaterialstoreException {
        jobName = new JobName("writePDF");
        jobTimestamp = JobTimestamp.now();
        Metadata metadata = Metadata.NULL_OBJECT;
        Path pdf = getCurrentDir().resolve("src/test/resources/tabular/cart.pdf");
        Material material = store.write(jobName, jobTimestamp, FileType.PDF, metadata, pdf);
        assertTrue(Files.exists(material.toPath(store)));
    }

}
