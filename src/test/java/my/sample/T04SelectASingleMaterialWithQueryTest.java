package my.sample;

import com.kazurayam.materialstore.core.JobName;
import com.kazurayam.materialstore.core.JobTimestamp;
import com.kazurayam.materialstore.core.Material;
import com.kazurayam.materialstore.core.MaterialstoreException;
import com.kazurayam.materialstore.core.QueryOnMetadata;
import com.kazurayam.materialstore.core.Store;
import com.kazurayam.materialstore.core.Stores;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class T04SelectASingleMaterialWithQueryTest {

    private Store store;

    @BeforeEach
    public void beforeEach() {
        Path testClassOutputDir = TestHelper.createTestClassOutputDir(this);
        store = Stores.newInstance(testClassOutputDir.resolve("store"));
    }

    @Test
    public void test04_select_a_single_material_with_query()
            throws MaterialstoreException {
        JobName jobName =
                new JobName("test04_select_a_single_material_with_query");
        JobTimestamp jobTimestamp = JobTimestamp.now();
        SharedMethods.write3images(store, jobName, jobTimestamp);
        //
        Material material = store.selectSingle(jobName, jobTimestamp,
                QueryOnMetadata.builder().put("step", "02").build()); // (20)
        assertNotNull(material);
        //
        System.out.printf("%s %s\n",
                material.getFileType().getExtension(),
                material.getMetadata().getMetadataIdentification());

        System.out.printf("%s '%s' %s\n\n",
                material.getMetadata().get("step"),
                material.getMetadata().get("label"),
                material.getMetadata().toURLAsString());
    }
}
