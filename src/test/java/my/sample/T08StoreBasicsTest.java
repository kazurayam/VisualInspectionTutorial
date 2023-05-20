package my.sample;

import com.kazurayam.materialstore.core.DuplicatingMaterialException;
import com.kazurayam.materialstore.core.FileType;
import com.kazurayam.materialstore.core.JobName;
import com.kazurayam.materialstore.core.JobTimestamp;
import com.kazurayam.materialstore.core.Material;
import com.kazurayam.materialstore.core.MaterialList;
import com.kazurayam.materialstore.core.MaterialstoreException;
import com.kazurayam.materialstore.core.Metadata;
import com.kazurayam.materialstore.core.QueryOnMetadata;
import com.kazurayam.materialstore.core.Store;
import com.kazurayam.materialstore.core.Stores;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class T08StoreBasicsTest {

    private static Path projectDir;
    private static Path outputDir;
    private Path root;
    private Store store;

    @BeforeAll
    public static void beforeAll() throws IOException {
        projectDir = Paths.get(".").toAbsolutePath();
        outputDir = projectDir.resolve("build/tmp/testOutput")
                .resolve(T08StoreBasicsTest.class.getName());
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
    public void test_write_a_Material_into_the_store() throws MaterialstoreException {
        URL url = SharedMethods.createURL(
                "https://kazurayam.github.io/materialstore-tutorial/images/tutorial/03_apple.png");
        // download the image into byte[]
        byte[] bytes = SharedMethods.downloadUrlToByteArray(url);
        // write the byte[] into the store
        JobName jobName = new JobName("test_write_a_Material_into_the_store");
        JobTimestamp jobTimestamp = JobTimestamp.now();
        Metadata metadata = Metadata.builder(url).build();
        // write a Material into the store
        // the directory tree of "store/jobName/jobTimestamp" will be automatically created
        Material m = store.write(jobName, jobTimestamp, FileType.PNG, metadata, bytes);
        assertNotNull(m, "m should not be null");
        System.out.println(String.format("%s\t%s\t%s",
                m.getID(),
                m.getFileType().getExtension(),
                m.getMetadata().getMetadataIdentification().getIdentification()));
    }

    @Test
    public void test_read_bytes_from_Material() throws MaterialstoreException {
        URL url = SharedMethods.createURL(
                "https://kazurayam.github.io/materialstore-tutorial/images/tutorial/03_apple.png");
        byte[] bytes = SharedMethods.downloadUrlToByteArray(url);
        JobName jobName = new JobName("test_read_bytes_from_Material");
        JobTimestamp jobTimestamp = JobTimestamp.now();
        Metadata metadata = Metadata.builder(url).build();
        Material m = store.write(jobName, jobTimestamp, FileType.PNG, metadata, bytes);

        // read all bytes from the Material
        byte[] content = store.read(m);
        assertTrue(content.length > 0);
    }

    @Test
    public void test_readAllLines_from_Material() throws MaterialstoreException {
        JobName jobName = new JobName("test_readAllLines_from_Material");
        JobTimestamp jobTimestamp = JobTimestamp.now();
        Material m = store.write(jobName, jobTimestamp, FileType.TXT,
                Metadata.NULL_OBJECT, "aaa\nbbb\nccc\n");
        List<String> lines = store.readAllLines(m);
        for (String line : lines) {
            System.out.println(line);
        }
    }

    @Test
    public void test_findAllJobNames() throws MaterialstoreException {
        // create test fixtures
        JobName jobName = new JobName("test_findAllJobNames");
        JobTimestamp jobTimestamp = JobTimestamp.now();
        SharedMethods.write3images(store, jobName, jobTimestamp);
        // list all JobNames in the store
        List<JobName> allJobNames = store.findAllJobNames();
        for (JobName jn : allJobNames) {
            System.out.println(jn.toString());
        }
    }

    @Test
    public void test_findAllJobTimestamps() throws MaterialstoreException {
        // create test fixtures
        JobName jobName = new JobName("test_findAllJobTimestamps");
        JobTimestamp jobTimestamp = JobTimestamp.now();
        SharedMethods.write3images(store, jobName, jobTimestamp);
        // list all JobTimestamps in the store/JobName
        List<JobTimestamp> allJobTimestamps = store.findAllJobTimestamps(jobName);
        for (JobTimestamp jt : allJobTimestamps) {
            System.out.println(jt.toString());
        }
    }

    @Test
    public void test_findLatestJobTimestamp() throws MaterialstoreException {
        // create test fixtures
        JobName jobName = new JobName("test_findLatestJobTimestamp");
        JobTimestamp jt = JobTimestamp.now();
        SharedMethods.write3images(store, jobName, jt);
        // find the latest JobTimestamp in the store/JobName
        JobTimestamp latest = store.findLatestJobTimestamp(jobName);
        assertEquals(jt, latest);
    }

    @Test
    public void test_findAllJobTimestampsPriorTo() throws MaterialstoreException {
        // create test fixtures
        JobName jobName = new JobName("test_findAllJobTimestampsPriorTo");
        JobTimestamp jt = JobTimestamp.now();
        SharedMethods.write3images(store, jobName, jt);
        // list all JobTimestamps in the store/JobName prior to a certain timing
        List<JobTimestamp> jtList =
                store.findAllJobTimestampsPriorTo(jobName, jt);
        assertEquals(0, jtList.size());
        jtList = store.findAllJobTimestampsPriorTo(jobName, JobTimestamp.laterThan(jt));
        assertEquals(1, jtList.size());
    }

    @Test
    public void test_contains() throws MaterialstoreException {
        // create test fixtures
        JobName jobName = new JobName("test_contains");
        JobTimestamp jt = JobTimestamp.now();
        SharedMethods.write3images(store, jobName, jt);
        // use store.contains() method
        assertTrue(store.contains(jobName));
        assertFalse(store.contains(new JobName("no such JobName")));
        assertTrue(store.contains(jobName, jt));
        assertFalse(store.contains(jobName, JobTimestamp.laterThan(jt)));
    }

    @Test
    public void test_copyMaterials() throws MaterialstoreException {
        // create test fixtures
        JobName jobName = new JobName("test_copyMaterials");
        JobTimestamp sourceJT = JobTimestamp.now();
        SharedMethods.write3images(store, jobName, sourceJT);
        //
        JobTimestamp targetJT = JobTimestamp.laterThan(sourceJT);
        store.copyMaterials(jobName, sourceJT, targetJT);
        assertTrue(store.contains(jobName, targetJT));
        MaterialList materialList = store.select(jobName, targetJT);
        assertEquals(3, materialList.size());
    }

    public void test_retrieve() throws MaterialstoreException {
        // create test fixtures
        JobName jobName = new JobName("test_copyMaterials");
        JobTimestamp jobTimestamp = JobTimestamp.now();
        SharedMethods.write3images(store, jobName, jobTimestamp);
        Material apple = store.selectSingle(jobName, jobTimestamp, FileType.PNG,
                QueryOnMetadata.builder().put("label", "apple").build());
        assertNotNull(apple);
        //
        Path outFile = Paths.get(System.getProperty("user.home"))
                .resolve("tmp/retrieved.png");
        store.retrieve(apple, outFile);
        assertTrue(Files.exists(outFile));
        assertTrue(outFile.toFile().length() > 0);
    }

    @Test
    public void test_deleteJobTimestamp() throws MaterialstoreException {
        // create test fixtures
        JobName jobName = new JobName("test_deleteJobTimestamp");
        JobTimestamp sourceJT = JobTimestamp.now();
        SharedMethods.write3images(store, jobName, sourceJT);
        JobTimestamp targetJT = JobTimestamp.laterThan(sourceJT);
        store.copyMaterials(jobName, sourceJT, targetJT);
        assertTrue(store.contains(jobName, targetJT));
        // now delete the targetJT and files contained there
        store.deleteJobTimestamp(jobName, targetJT);
        assertFalse(store.contains(jobName, targetJT));
    }

    @Test
    public void test_deleteJobName() throws MaterialstoreException {
        // create test fixtures
        JobName jobName = new JobName("test_deleteJobName");
        JobTimestamp sourceJT = JobTimestamp.now();
        SharedMethods.write3images(store, jobName, sourceJT);
        assertTrue(store.contains(jobName));
        // now delete the JobName and files contained there
        store.deleteJobName(jobName);
        assertFalse(store.contains(jobName));
    }

    @Test
    public void test_unable_to_write_material_with_duplicating_Metadata()
            throws MalformedURLException, MaterialstoreException {
        // create test fixtures
        JobName jobName = new JobName("test_unable_to_write_material_with_duplicating_Metadata");
        JobTimestamp jobTimestamp = JobTimestamp.now();
        URL url = new URL("https://github.com/kazurayam/materialstore-tutorial");
        Metadata metadata = Metadata.builder(url).put("foo", "bar").build();
        Material mt1 = store.write(jobName, jobTimestamp, FileType.TXT,
                metadata, "Hello, Materialstore!");
        try {
            // this code will cause a DuplicatingMaterialException to be raised
            // as you can not write a Material with a duplicating combination of
            // FileType + Metadata
            byte[] bytes = store.read(mt1);
            Material mt2 = store.write(jobName, jobTimestamp, FileType.TXT,
                    metadata, bytes);

            throw new RuntimeException("expected to raise a DuplicatingMaterialException, but not");
        } catch (DuplicatingMaterialException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_able_to_write_materials_with_same_ID_unique_Metadata()
            throws MalformedURLException, MaterialstoreException {
        // create test fixtures
        JobName jobName =
                new JobName("test_able_to_write_materials_with_same_ID_unique_Metadata");
        JobTimestamp jobTimestamp = JobTimestamp.now();
        URL url = new URL("https://github.com/kazurayam/materialstore-tutorial");
        //
        Metadata metadata1 = Metadata.builder(url).put("step", "01").build();
        Material mt1 = store.write(jobName, jobTimestamp, FileType.TXT,
                metadata1, "Hello, Materialstore!");
        Metadata metadata2 = Metadata.builder(url).put("step", "02").build();

        // write one more Material with the same ID but with unique Metadata
        byte[] bytes = store.read(mt1);
        Material mt2 = store.write(jobName, jobTimestamp, FileType.TXT,
                metadata2, bytes);
        MaterialList materialList = store.select(jobName, jobTimestamp);

        // make sure there are 2 Materials writen
        assertEquals(2, materialList.size());
        for (Material m : materialList) {
            System.out.printf("%s\t%s\t%s\n",
                    m.getID().toString(),
                    m.getFileType().getExtension(),
                    m.getMetadata().getMetadataIdentification().toString());
        }
    }

    @Test
    public void test_getRoot() {
        Path storeDir = store.getRoot();
        assertEquals("store", storeDir.getFileName().toString());
        assertEquals(root, storeDir);
    }

    @Test
    public void test_getPathOf() throws MalformedURLException, MaterialstoreException {
        // create test fixtures
        JobName jobName =
                new JobName("test_getPathOf");
        JobTimestamp jobTimestamp = JobTimestamp.now();
        URL url = new URL("https://github.com/kazurayam/materialstore-tutorial");
        Metadata metadata1 = Metadata.builder(url).put("step", "01").build();
        Material mt1 = store.write(jobName, jobTimestamp, FileType.TXT,
                metadata1, "Hello, Materialstore!");

        // test getting the Path of JobName directory
        Path jobNamePath = store.getPathOf(jobName);
        System.out.println("jobNamePath=" +
                projectDir.relativize(jobNamePath).toString());
        assertEquals(jobName.getJobName(), jobNamePath.getFileName().toString());

        // test getting the Path of JobTimestamp directory
        Path jobTimestampPath = store.getPathOf(jobName, jobTimestamp);
        System.out.println("jobTimestampPath=" +
                projectDir.relativize(jobTimestampPath).toString());
        assertEquals(jobTimestamp.toString(), jobTimestampPath.getFileName().toString());

        // test getting the Path of Material file
        Path materialPath = store.getPathOf(mt1);
        System.out.println("materialPath=" +
                projectDir.relativize(materialPath).toString());
        assertTrue(materialPath.getFileName().toString().startsWith(mt1.getID().toString()));

        // test getting the Path of Material, a simpler way
        Path materialPathAlt = mt1.toPath();
        System.out.println("materialPathAlt=" +
                projectDir.relativize(materialPathAlt).toString());
        assertTrue(materialPathAlt.getFileName().toString().startsWith(mt1.getID().toString()));
    }
}
