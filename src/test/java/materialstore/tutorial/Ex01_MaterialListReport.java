package materialstore.tutorial;

import com.kazurayam.materialstore.filesystem.FileType;
import com.kazurayam.materialstore.filesystem.JobName;
import com.kazurayam.materialstore.filesystem.JobTimestamp;
import com.kazurayam.materialstore.filesystem.MaterialList;
import com.kazurayam.materialstore.filesystem.MaterialstoreException;
import com.kazurayam.materialstore.filesystem.Metadata;
import com.kazurayam.materialstore.filesystem.SortKeys;
import com.kazurayam.materialstore.filesystem.Store;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import com.kazurayam.materialstore.inspector.Inspector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Ex01_MaterialListReport {


    private Store store;
    private JobName jobName;
    private JobTimestamp jobTimestamp;

    @BeforeAll
    public static void beforeAll() throws IOException { Helper.initializeOutputDir(); }

    @BeforeEach
    public void setup() throws IOException, MaterialstoreException {
        store = Helper.initializeStore(this);
        jobName = new JobName(Helper.classNameToJobName(this));
        jobTimestamp = JobTimestamp.now();
    }

    @Test
    public void testCompileReport() throws MalformedURLException, MaterialstoreException {
        // create 3 materials in the store
        Path screenshotDir = Helper.getCWD().resolve("src/test/resources/screenshot");
        writePngIntoStore(store, jobName, jobTimestamp,
                screenshotDir.resolve("DuckDuckGo-blank.png"), "01", "blank search page");
        writePngIntoStore(store, jobName, jobTimestamp,
                screenshotDir.resolve("DuckDuckGo-query.png"), "02", "search page with query");
        writePngIntoStore(store, jobName, jobTimestamp,
                screenshotDir.resolve("DuckDuckGo-result.png"), "03", "result page");
        MaterialList materialList = store.select(jobName, jobTimestamp);
        assertEquals(3, materialList.size());

        // compile a report
        Inspector inspector = Inspector.newInstance(store);
        String fileName = jobName.toString() + "-list.html";
        SortKeys sortKeys = new SortKeys("step", "label", "URL.path");
        inspector.setSortKeys(sortKeys);
        Path report = inspector.report(materialList, fileName);
        assertTrue(Files.exists(report));
        System.out.println("report is found at " + report.toString());
    }

    private void writePngIntoStore(Store store,
                                   JobName jobName,
                                   JobTimestamp jobTimestamp,
                                   Path pngFile,
                                   String step, String label) throws MalformedURLException, MaterialstoreException {
        URL url = pngFile.toFile().toURI().toURL();
        Metadata metadata = new Metadata.Builder(url)
                .put("step", step)
                .put("label", label)
                .exclude("URL.host").build();
        store.write(jobName, jobTimestamp, FileType.PNG, metadata, pngFile);
    }
}
