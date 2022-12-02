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
public class Ex22_write_materials_of_various_FileTypes {

    private Store store;
    private JobName jobName;
    private JobTimestamp jobTimestamp;

    @BeforeAll
    public static void beforeAll() throws IOException { TestHelper.initializeOutputDir(); }

    @BeforeEach
    public void setup() throws IOException {
        store = TestHelper.initializeStore(this);
    }

    @Test
    public void writeTXT() throws MaterialstoreException {
        jobName = new JobName(TestHelper.classNameToJobName(this));
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
        Path png = TestHelper.getCWD().resolve("src/test/resources/images/apple.png");
        Material material = store.write(jobName, jobTimestamp, FileType.PNG, metadata, png);
        assertTrue(Files.exists(material.toPath(store)));
    }

    @Test
    public void writeCSV() throws MaterialstoreException {
        jobName = new JobName("writeCSV");
        jobTimestamp = JobTimestamp.now();
        Metadata metadata = Metadata.NULL_OBJECT;
        Path csv = TestHelper.getCWD().resolve("src/test/resources/tabular/cart.csv");
        Material material = store.write(jobName, jobTimestamp, FileType.CSV, metadata, csv);
        assertTrue(Files.exists(material.toPath(store)));
    }

    @Test
    public void writeXLSX() throws MaterialstoreException {
        jobName = new JobName("writeXLSX");
        jobTimestamp = JobTimestamp.now();
        Metadata metadata = Metadata.NULL_OBJECT;
        Path xlsx = TestHelper.getCWD().resolve("src/test/resources/tabular/cart.xlsx");
        Material material = store.write(jobName, jobTimestamp, FileType.XLSX, metadata, xlsx);
        assertTrue(Files.exists(material.toPath(store)));
    }

    @Test
    public void writePDF() throws MaterialstoreException {
        jobName = new JobName("writePDF");
        jobTimestamp = JobTimestamp.now();
        Metadata metadata = Metadata.NULL_OBJECT;
        Path pdf = TestHelper.getCWD().resolve("src/test/resources/tabular/cart.pdf");
        Material material = store.write(jobName, jobTimestamp, FileType.PDF, metadata, pdf);
        assertTrue(Files.exists(material.toPath(store)));
    }

}
