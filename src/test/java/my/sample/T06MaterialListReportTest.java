package my.sample;

import com.kazurayam.materialstore.base.inspector.Inspector;
import com.kazurayam.materialstore.core.JobName;
import com.kazurayam.materialstore.core.JobTimestamp;
import com.kazurayam.materialstore.core.MaterialList;
import com.kazurayam.materialstore.core.MaterialstoreException;
import com.kazurayam.materialstore.core.QueryOnMetadata;
import com.kazurayam.materialstore.core.SortKeys;
import com.kazurayam.materialstore.core.Store;
import com.kazurayam.materialstore.core.Stores;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class T06MaterialListReportTest {
    private Store store;
    @BeforeEach
    public void beforeEach() {
        Path testClassOutputDir = TestHelper.createTestClassOutputDir(this);
        store = Stores.newInstance(testClassOutputDir.resolve("store"));
    }

    @Test
    public void test06_makeMaterialListReport() throws MaterialstoreException {
        JobName jobName =
                new JobName("test06_makeMaterialListReport");
        JobTimestamp jobTimestamp = JobTimestamp.now();
        // write 3 PNG files into the store
        SharedMethods.write3images(store, jobName, jobTimestamp);

        MaterialList materialList =
                store.select(jobName, jobTimestamp,
                        QueryOnMetadata.ANY);

        Inspector inspector = Inspector.newInstance(store);  // (22)
        inspector.setSortKeys(new SortKeys("step"));   // (23)

        Path report = inspector.report(materialList);        // (24)
        assertNotNull(report);
        System.out.println("report is found at " + report);
    }
}
