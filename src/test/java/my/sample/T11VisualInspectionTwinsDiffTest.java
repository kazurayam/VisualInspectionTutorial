package my.sample;

import com.kazurayam.inspectus.core.Environment;
import com.kazurayam.inspectus.core.Inspectus;
import com.kazurayam.inspectus.core.InspectusException;
import com.kazurayam.inspectus.core.Intermediates;
import com.kazurayam.inspectus.core.Parameters;
import com.kazurayam.inspectus.core.UncheckedInspectusException;
import com.kazurayam.inspectus.fn.FnTwinsDiff;
import com.kazurayam.inspectus.materialize.discovery.Sitemap;
import com.kazurayam.inspectus.materialize.discovery.SitemapLoader;
import com.kazurayam.inspectus.materialize.discovery.Target;
import com.kazurayam.inspectus.materialize.selenium.WebDriverFormulas;
import com.kazurayam.materialstore.core.JobName;
import com.kazurayam.materialstore.core.JobTimestamp;
import com.kazurayam.materialstore.core.Material;
import com.kazurayam.materialstore.core.Metadata;
import com.kazurayam.materialstore.core.SortKeys;
import com.kazurayam.materialstore.core.Store;
import com.kazurayam.materialstore.core.Stores;
import com.kazurayam.materialstore.core.metadata.IgnoreMetadataKeys;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class T11VisualInspectionTwinsDiffTest {

    private static Path outputDir;
    private static Path dataDir;
    private Path root;
    private Store store;
    private WebDriver driver;
    private WebDriverFormulas wdf;

    @BeforeAll
    public static void beforeAll() throws IOException {
        Path projectDir = Paths.get(".").toAbsolutePath();
        outputDir = projectDir.resolve("build/tmp/testOutput")
                .resolve(T11VisualInspectionTwinsDiffTest.class.getName());
        if (Files.exists(outputDir)) {
            FileUtils.deleteDirectory(outputDir.toFile());
        }
        Files.createDirectories(outputDir);
        dataDir = projectDir.resolve("src/test/fixtures")
                .resolve("T09SeleniumTwinsDiffTest");
        assert Files.exists(dataDir) : dataDir.toString() + " is not found";
    }

    @BeforeEach
    public void beforeEach() {
        root = outputDir.resolve("store");
        store = Stores.newInstance(root);
        WebDriverManager.chromedriver().setup();
        //
        ChromeOptions opt = new ChromeOptions();
        opt.addArguments("headless");
        opt.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(opt);
        driver.manage().window().setSize(new Dimension(1024, 1000));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wdf = new WebDriverFormulas();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void performTwinsDiff() throws InspectusException {
        Parameters parameters = new Parameters.Builder()
                .store(Stores.newInstance(outputDir.resolve("store")))
                .jobName(new JobName("performTwinsDiff"))
                .jobTimestamp(JobTimestamp.now())
                .ignoreMetadataKeys(
                        new IgnoreMetadataKeys.Builder()
                                .ignoreKey("URL.host")
                                .build()
                )
                .sortKeys(new SortKeys("step"))
                .build();
        Inspectus twinsDiff =
                new FnTwinsDiff(fn,
                        new Environment("ProductionEnv"),
                        new Environment("DevelopmentEnv"));
        twinsDiff.execute(parameters);
    }

    /*
     * invoked by the FnTwinsDiff#execute() internally.
     */
    private final BiFunction<Parameters, Intermediates, Intermediates> fn = (parameters, intermediates) -> {
        assert parameters.getEnvironment() != Environment.NULL_OBJECT : "parameters.Environment() must not be null";
        Environment env = parameters.getEnvironment();
        try {
            List<Target> targetList;
            switch (env.toString()) {
                case "ProductionEnv":
                    targetList =
                            getTargetList(new URL("http://myadmin.kazurayam.com"),
                                    dataDir.resolve("targetList.csv"));
                    break;
                case "DevelopmentEnv":
                    targetList =
                            getTargetList(new URL("http://devadmin.kazurayam.com"),
                                    dataDir.resolve("targetList.csv"));
                    break;
                default:
                    throw new UncheckedInspectusException(
                            String.format("unknown Environment env=%s", env));
            }
            JobTimestamp jobTimestamp = parameters.getJobTimestamp();
            // process the targets
            assert targetList.size() != 0 : "targetList is empty";
            for (int i = 0; i < targetList.size(); i++) {
                Target target = targetList.get(i);
                processTarget(parameters, jobTimestamp, target.getUrl(),
                        target.getHandle().getBy(),
                        env, String.format("%02d", i + 1));
            }
        } catch (MalformedURLException | InspectusException e) {
            throw new UncheckedInspectusException(e);
        }
        return new Intermediates.Builder(intermediates).build();
    };

    private void processTarget(Parameters p, JobTimestamp jt,
                               URL url, By handle,
                               Environment environment, String step) {
        driver.get(url.toExternalForm());
        wdf.waitForElementPresent(driver, handle, 3);
        Metadata md = Metadata.builder(url)
                .put("environment", environment.toString())
                .put("step", step)
                .build();
        Material mt = MaterializeUtils.takePageScreenshotSaveIntoStore(driver,
                p.getStore(), p.getJobName(), jt, md);
        assertNotNull(mt);
        assertNotEquals(Material.NULL_OBJECT, mt);
    }

    /*
     * read a CSV file located in the `src/test/fixtures/FnTwinsDiffTest` directory,
     * construct a list of Target objects which contains URLs to materialize.
     */
    private List<Target> getTargetList(URL baseTopPageURL, Path targetFile)
            throws InspectusException {
        assert Files.exists(targetFile) : targetFile + " is not found";
        //
        Target baseTopPage = Target.builder(baseTopPageURL).build();
        SitemapLoader loader = new SitemapLoader(baseTopPage);
        loader.setWithHeaderRecord(false);
        Sitemap sitemap = loader.parseCSV(targetFile);
        return sitemap.getBaseTargetList();
    }

}
