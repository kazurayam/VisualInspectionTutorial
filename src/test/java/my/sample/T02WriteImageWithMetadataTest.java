package my.sample;

import com.kazurayam.materialstore.core.FileType;
import com.kazurayam.materialstore.core.JobName;
import com.kazurayam.materialstore.core.JobTimestamp;
import com.kazurayam.materialstore.core.Material;
import com.kazurayam.materialstore.core.MaterialstoreException;
import com.kazurayam.materialstore.core.Metadata;
import com.kazurayam.materialstore.core.Store;
import com.kazurayam.materialstore.core.Stores;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class T02WriteImageWithMetadataTest {

    private Store store;

    @BeforeEach
    public void beforeEach() {
        Path testClassOutputDir = TestHelper.createTestClassOutputDir(this);
        store = Stores.newInstance(testClassOutputDir.resolve("store"));
    }

    @Test
    public void test02_write_image_with_metadata() throws MaterialstoreException {
        JobName jobName = new JobName("test02_write_image_with_metadata");
        JobTimestamp jobTimestamp = JobTimestamp.now();
        URL url = SharedMethods.createURL(                     // (10)
                "https://kazurayam.github.io/materialstore-tutorial/images/tutorial/03_apple.png");
        byte[] bytes = SharedMethods.downloadUrl(url);         // (11)
        Material material =
                store.write(jobName, jobTimestamp,             // (12)
                        FileType.PNG,
                        Metadata.builder(url)                  // (13)
                                .put("step", "01")
                                .put("label", "red apple")
                                .build(),
                        bytes);

        assertNotNull(material);
        System.out.println(material.getID() + " "
                + material.getDescription());                   // (14)

        assertEquals(FileType.PNG, material.getFileType());
        assertEquals("https",
                material.getMetadata().get("URL.protocol"));
        assertEquals("kazurayam.github.io",
                material.getMetadata().get("URL.host"));        // (15)
        assertEquals("/materialstore-tutorial/images/tutorial/03_apple.png",
                material.getMetadata().get("URL.path"));
        assertEquals("01", material.getMetadata().get("step"));
    }
}
