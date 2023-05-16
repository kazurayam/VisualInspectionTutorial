package my.sample;

import com.kazurayam.materialstore.core.JobName;
import com.kazurayam.materialstore.core.JobTimestamp;
import com.kazurayam.materialstore.core.Material;
import com.kazurayam.materialstore.core.MaterialList;
import com.kazurayam.materialstore.core.MaterialstoreException;
import com.kazurayam.materialstore.core.QueryOnMetadata;
import com.kazurayam.materialstore.core.Store;
import com.kazurayam.materialstore.core.Stores;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

public class T05SelectMaterialListTest {
    private Store store;
    @BeforeEach
    public void beforeEach() {
        Path testClassOutputDir = TestHelper.createTestClassOutputDir(this);
        store = Stores.newInstance(testClassOutputDir.resolve("store"));
    }

    @Test
    public void test05_select_list_of_material()
            throws MaterialstoreException {
        JobName jobName =
                new JobName("test04_select_lest_of_materials");
        JobTimestamp jobTimestamp = JobTimestamp.now();
        SharedMethods.write3images(store, jobName, jobTimestamp);
        //
        MaterialList materialList =
                store.select(jobName, jobTimestamp,
                        QueryOnMetadata.ANY);              // (18)
        //
        for (Material material : materialList) {           // (19)
            System.out.printf("%s %s\n",
                    material.getFileType().getExtension(),
                    material.getMetadata().getMetadataIdentification());
            System.out.printf("%s '%s' %s\n\n",
                    material.getMetadata().get("step"),
                    material.getMetadata().get("label"),
                    material.getMetadata().toURLAsString());
        }
    }
}
