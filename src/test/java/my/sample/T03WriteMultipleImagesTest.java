package my.sample;

import com.kazurayam.materialstore.core.JobName;
import com.kazurayam.materialstore.core.JobTimestamp;
import com.kazurayam.materialstore.core.MaterialList;
import com.kazurayam.materialstore.core.MaterialstoreException;
import com.kazurayam.materialstore.core.QueryOnMetadata;
import com.kazurayam.materialstore.core.Store;
import com.kazurayam.materialstore.core.Stores;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class T03WriteMultipleImagesTest {
    private Store store;
    @BeforeEach
    public void beforeEach() {
        Path testClassOutputDir = TestHelper.createTestClassOutputDir(this);
        store = Stores.newInstance(testClassOutputDir.resolve("store"));
    }

    @Test
    public void test03_write_multiple_images()
            throws MaterialstoreException {
        JobName jobName = new JobName("test03_write_multiple_images");
        JobTimestamp jobTimestamp = JobTimestamp.now();
        SharedMethods.write3images(store, jobName, jobTimestamp);  // (16)
        MaterialList allMaterialList =
                store.select(jobName, jobTimestamp,
                        QueryOnMetadata.ANY);                      // (17)
        assertEquals(3, allMaterialList.size());
    }
}
