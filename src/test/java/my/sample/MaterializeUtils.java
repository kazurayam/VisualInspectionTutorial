package my.sample;

import com.kazurayam.ashotwrapper.AShotWrapper;
import com.kazurayam.materialstore.core.FileType;
import com.kazurayam.materialstore.core.JobName;
import com.kazurayam.materialstore.core.JobTimestamp;
import com.kazurayam.materialstore.core.Material;
import com.kazurayam.materialstore.core.MaterialstoreException;
import com.kazurayam.materialstore.core.Metadata;
import com.kazurayam.materialstore.core.Store;
import org.openqa.selenium.WebDriver;

import java.awt.image.BufferedImage;

public class MaterializeUtils {

    /**
     * take screenshot of web pages using selenium API wrapped by the AShotWrapper,
     * store the image into the store
     */
    public static Material takePageScreenshotSaveIntoStore(
            WebDriver driver,
            Store store, JobName jobName, JobTimestamp jobTimestamp, Metadata md) {
        try {
            BufferedImage bufferedImage = AShotWrapper.takeEntirePageImage(driver);
            return store.write(jobName, jobTimestamp, FileType.PNG, md, bufferedImage);
        } catch (MaterialstoreException e) {
            throw new RuntimeException(e);
        }
    }
}
