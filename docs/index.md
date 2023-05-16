-   <a href="#materialstore-tutorial" id="toc-materialstore-tutorial">Materialstore Tutorial</a>
    -   <a href="#setting-up-a-project" id="toc-setting-up-a-project">Setting up a project</a>
    -   <a href="#1st-example-hello-materialstore" id="toc-1st-example-hello-materialstore">1st example : "Hello, materialstore!"</a>
        -   <a href="#file-tree-created-by-hello-materialstore" id="toc-file-tree-created-by-hello-materialstore">File tree created by "Hello, materialstore"</a>
        -   <a href="#create-a-base-directory" id="toc-create-a-base-directory">Create a base directory</a>
        -   <a href="#create-the-store-directory" id="toc-create-the-store-directory">Create the "store" directory</a>
        -   <a href="#store-class" id="toc-store-class">Store class</a>
        -   <a href="#jobname-class" id="toc-jobname-class">JobName class</a>
        -   <a href="#jobtimestamp-class" id="toc-jobtimestamp-class">JobTimestamp class</a>
        -   <a href="#create-a-file-tree-write-a-material" id="toc-create-a-file-tree-write-a-material">create a file tree, write a "Material"</a>
        -   <a href="#file-name-of-the-materials" id="toc-file-name-of-the-materials">File name of the Materials</a>
        -   <a href="#filetype" id="toc-filetype">FileType</a>
        -   <a href="#metadata" id="toc-metadata">Metadata</a>
        -   <a href="#types-of-objects-writeable-into-the-store" id="toc-types-of-objects-writeable-into-the-store">Types of objects writeable into the Store</a>
    -   <a href="#2nd-example-write-an-image-with-metadata" id="toc-2nd-example-write-an-image-with-metadata">2nd example: write an image with Metadata</a>
        -   <a href="#metadata-attributes-based-on-key-value-pairs-explicitly-specified-plus-attributes-based-on-a-url" id="toc-metadata-attributes-based-on-key-value-pairs-explicitly-specified-plus-attributes-based-on-a-url">Metadata attributes based on key-value pairs explicitly specified, plus attributes based on a URL</a>
        -   <a href="#order-of-key-value-pairs-in-metadata" id="toc-order-of-key-value-pairs-in-metadata">Order of key-value pairs in Metadata</a>
        -   <a href="#keyvalue-pairs-explicitly-specified" id="toc-keyvalue-pairs-explicitly-specified">key:value pairs explicitly specified</a>
        -   <a href="#retrieving-information-of-a-material" id="toc-retrieving-information-of-a-material">Retrieving information of a Material</a>
    -   <a href="#3rd-example-writing-multiple-images" id="toc-3rd-example-writing-multiple-images">3rd example: writing multiple images</a>
    -   <a href="#4th-example-retrieving-a-saved-material" id="toc-4th-example-retrieving-a-saved-material">4th example : retrieving a saved material</a>
    -   <a href="#5th-example-selecting-a-materiallist" id="toc-5th-example-selecting-a-materiallist">5th example : Selecting a MaterialList</a>
    -   <a href="#6th-example-generate-a-html-report-of-a-materiallist" id="toc-6th-example-generate-a-html-report-of-a-materiallist">6th example : generate a HTML report of a MaterialList</a>

# Materialstore Tutorial

-   [API javadoc](https://kazurayam.github.io/materialstore/api/index.html)

-   back to the [repository](https://github.com/kazurayam/materialstore)

This is a quick introduction to a Java library named `materialstore` that I (kazurayam) developed.

## Setting up a project

Here I assume you have a seasoned programming skill in Java, and you have installed the build tool [Gradle](https://gradle.org/install/). Now let us create a project where you write some Java code for practice.

I created a working directory under my home directory: `~/tmp/sampleProject`.

    $ cd ~/tmp/
    $ mkdir sampleProject

You want to initialize it as a Gradle project, so you would operate in the console as this:

    $ cd ~/tmp/sampleProject
    $ gradle init

    Select type of project to generate:
      1: basic
      2: application
      3: library
      4: Gradle plugin
    Enter selection (default: basic) [1..4] 1

    Select build script DSL:
      1: Groovy
      2: Kotlin
    Enter selection (default: Groovy) [1..2] 1

    Generate build using new APIs and behavior (some features may change in the next minor release)? (default

    Project name (default: sampleProject):

    > Task :init
    Get more help with your project: Learn more about Gradle by exploring our samples at https://docs.gradle.org/7.4.2/samples

    BUILD SUCCESSFUL in 28s

Then you will find a file `sampleProject/settings.gradle` has been created, which looks like:

**settings.gradle**

    rootProject.name = 'sampleProject'

You will also find a file `sampleProject/build.gradle` file, but it will be empty (comments only). So you want to edit it, like this.

**build.gradle**

    plugins {
        id 'java'
    }

    group 'com.kazurayam'
    version '0.3.0'

    repositories {
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        testImplementation group: 'com.kazurayam', name: 'materialstore', version: '0.16.5'
        implementation group: 'com.google.guava', name: 'guava', version: '31.1-jre'
        testImplementation group: 'org.slf4j', name: 'slf4j-api', version: '1.7.25'
        testImplementation group: 'org.slf4j', name: 'slf4j-simple', version: '1.7.25'
        testImplementation 'org.junit.jupiter:junit-jupiter-api:5.9.2'
        testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.9.2'
    }

    test {
        useJUnitPlatform()
    }

    task compileTutorial {
        // run docs/indexconv.sh to generate index.md from index_.adoc
        doLast {
            exec {
                workingDir './docs'

Please note that in the build.gradle we declared the dependency to the `materialstore` library, which is published at the Maven Central repository.

-   <https://mvnrepository.com/artifact/com.kazurayam/materialstore>

You can check if the project is properly setup by executing a command, as follows:

    $ cd ~/tmp/sampleProject/
    $ gradle dependencies --configuration testImplementation

    > Task :dependencies

    ------------------------------------------------------------
    Root project 'materialstore-tutorial'
    ------------------------------------------------------------

    testImplementation - Implementation only dependencies for source set 'test'. (n)
    +--- com.kazurayam:materialstore:0.16.5 (n)
    +--- org.freemarker:freemarker:2.3.31 (n)
    +--- com.google.guava:guava:31.1-jre (n)
    +--- com.google.code.gson:gson:2.8.9 (n)
    +--- org.slf4j:slf4j-api:1.7.25 (n)
    +--- org.slf4j:slf4j-simple:1.7.25 (n)
    \--- org.junit.jupiter:junit-jupiter-api:5.9.2 (n)

    (n) - A dependency or dependency configuration that cannot be resolved.

    A web-based, searchable dependency report is available by adding the --scan option.

    BUILD SUCCESSFUL in 1s
    1 actionable task: 1 executed

## 1st example : "Hello, materialstore!"

I have created a JUnit-based Java code that uses the materialstore library: `sampleProject/src/test/java/my/sample/T1HelloMaterialstoreTest.java`, as follows:

**T01HelloMaterialstoreTest.java**

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

    import java.io.IOException;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;

    import static org.junit.jupiter.api.Assertions.assertNotNull;

    /*
     * This code demonstrate how to save a text string into an instance of
     * "materialstore" backed with a directory on the local OS file system.
     */
    public class T01HelloMaterialstoreTest {

        // central abstraction of Material storage
        private Store store;

        @BeforeEach
        public void beforeEach() {
            // create a base directory
            Path dir = createTestClassOutputDir(this);   // (1)
            // create a directory named "store"
            Path storeDir = dir.resolve("store");   // (2)
            // instantiate a Store object
            store = Stores.newInstance(storeDir);        // (3)
        }

        @Test
        public void test01_hello_materialstore() throws MaterialstoreException {
            JobName jobName =
                    new JobName("test01_hello_materialstore");       // (4)
            JobTimestamp jobTimestamp = JobTimestamp.now();          // (5)
            String text = "Hello, materialstore!";
            Material material = store.write(jobName, jobTimestamp,   // (6)
                    FileType.TXT,                            // (7)
                    Metadata.NULL_OBJECT,                    // (8)
                    text);                                   // (9)
            System.out.printf("wrote a text '%s'%n", text);
            assertNotNull(material);
        }

        //-----------------------------------------------------------------

        Path createTestClassOutputDir(Object testClass) {
            Path output = getTestOutputDir()
                    .resolve(testClass.getClass().getName());
            try {
                if (!Files.exists(output)) {
                    Files.createDirectories(output);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return output;
        }

        Path getTestOutputDir() {
            return Paths.get(System.getProperty("user.dir"))
                    .resolve("build/tmp/testOutput");
        }
    }

You can run this by running the `test` task of Gradle:

    $ gradle test --tests  my.sample.T1HelloMaterialstore -i
    > Task :compileJava NO-SOURCE
    > Task :processResources NO-SOURCE
    > Task :classes UP-TO-DATE
    > Task :compileTestJava
    > Task :processTestResources NO-SOURCE
    > Task :testClasses
    > Task :test

    BUILD SUCCESSFUL in 2s
    2 actionable tasks: 2 executed

The `test` task of Gradle will create a report in HTML format where you can find all output from the test execution. You can find the report at `build/reports/tests/test/index.html`.

    $ cd ~/tmp/sampleProject
    $ tree build/reports/tests/
    build/reports/tests/
    └── test
        ├── classes
        │   └── my.sample.T1HelloMaterialstoreTest.html
        ├── css
        │   ├── base-style.css
        │   └── style.css
        ├── index.html
        ├── js
        │   └── report.js
        └── packages
            └── my.sample.html

    5 directories, 6 files

You want to open the `index.html` in your Web browser to have a look at the test result.

![01 test report](../images/tutorial/01_test_report.png)

### File tree created by "Hello, materialstore"

The 1st test will create a new file tree as output:

![02 test output file tree](../images/tutorial/02_test_output_file_tree.png)

Let us read the Java source of the test `T1HelloMaterialstoreTest` line by line to understand the basic concept and classes of the "materialstore" library. Here I assume that you are a well-trained Java programmer who requires no explanation how to code this using JUnit.

### Create a base directory

    import java.nio.file.Path;
    ...
        @BeforeEach
        public void beforeEach() {
            Path dir = createTestClassOutputDir(this);   // (1)

The statement commented as (1) creates a directory `build/tmp/testOutput/<fully qualified class name>`. In this directory the test will save all output files during its run. The helper method `createTestClassOutputDir(Object)` is defined later in the source file.

### Create the "store" directory

            Path storeDir = dir.resolve("store");   // (2)

The statement (2) declares an instance of `java.nio.file.Path` class, store it into a variable `storeDir`. The Path object corresponds to the directory `build/tmp/testOutput/<fully qualified class name>/store`.

### Store class

    import com.kazurayam.materialstore.core.Store;
    ...

        private Store store;
    ...

            store = Stores.newInstance(storeDir);        // (3)

The statement (3) creates an instance of `com.kazurayam.materialstore.core.Store` class. The statement (3) will create a physical directory `store` if not yet present.

The `Store` class is the core part of the materialstore library. The `Store` class implements methods to write the materials into the OS file system. It also implements methods to select (=retrieve) one or more `Material` object(s) (=file) out of the store.

The `Stores` class is the factory that is capable of creating instance of the `Store` class.

### JobName class

    import com.kazurayam.materialstore.core.JobName;
    ...
        @Test
        public void test01_hello_materialstore() throws MaterialstoreException {
            JobName jobName =
                    new JobName("test01_hello_materialstore");       // (4)

The statement (4) declares the name of a subdirectory under the `store` directory. You can specify a string value as the parameter to the constructor of `com.kazurayam.materialstore.core.JobName` class. It is just a directory name; no deep semantic meaning is enforced. However, you should remember that some ASCII characters are prohibited as a part of file/directory names by the underlying OS; therefore you can not use them as the `JobName` object’s value. For example, Windows OS does not allow you to use the following characters:

-   `<` (less than)

-   `>` (greater than)

-   `:` (colon)

-   `"` (double quote)

-   `/` (forward slash)

-   `\` (backslash)

-   `|` (vertical bar or pipe)

-   `?` (question mark)

-   `*` (asterisk)

You can use non-latin characters as JobName. JobName can contain white spaces if necessary. For example, you can write:

        JobName jobName = new JobName("わたしの仕事 means my job");

### JobTimestamp class

    import com.kazurayam.materialstore.core.JobTimestamp;
    ...
            JobTimestamp jobTimestamp = JobTimestamp.now();          // (5)

The statement (5) declares the name of a new directory, which I will call as **JobTimestamp**, under a **JobName** directory. The JobTimestamp will be in a fixed format of `uuuuMMdd_hhmmss` (year, month, day, hours, minutes, seconds). A call to `JobTimestamp.now()` will return a JobTimestamp object which corresponds to a directory of which name stands for the current timestamp provided by OS.

The `JobTimestamp` class implements various methods that help you work on. See the [javadoc](https://kazurayam.github.io/materialstore/api/com/kazurayam/materialstore/core/JobTimestamp.html) for detail.

### create a file tree, write a "Material"

    import com.kazurayam.materialstore.core.FileType;
    import com.kazurayam.materialstore.core.Material;
    import com.kazurayam.materialstore.core.Metadata;
    ...
            String text = "Hello, materialstore!";
            Material material = store.write(jobName, jobTimestamp,   // (6)
                    FileType.TXT,                            // (7)
                    Metadata.NULL_OBJECT,                    // (8)
                    text);                                   // (9)

The lines (6) to (9) creates a file tree under the \`store\`directory, like this:

    $ cd build/tmp/testOutput/my.sample.T1HelloMaterialstoreTest/
    $ tree .
    store/
    └── test01_hello_materialstore
        └── 20221128_082216
            ├── index
            └── objects
                └── 4eb4efec3324a630e0d3d96e355261da638c8285.txt

The structure of the file tree under the `store` directory is specially designed to save the **Materials**. The tree structure is fixed: a file named `store/<JobName>/<JobTimestamp>/index` plus one or more files under the `store/<JobName>/<JobTimestamp>/objects/` directory. You are not suppose to **customize** this tructure. You would delegate all tasks of creating + naming + locating files and directories under the `store` directory to the `Store` object.

As the line commented as (6) tells, a "Material" (actually, is a file) is always saved under the sub-tree `store/<JobName>/<JobTimestamp>/objects`.

### File name of the Materials

All files under the `objects` have a fixed format of file name, that is:

**&lt;40 characters in alpha-numeric, calcurated by the SHA1 hash function&gt;.&lt;file extention&gt;**

for example, a Material could have a file name:

`4eb4efec3324a630e0d3d96e355261da638c8285.txt`

Ths `Store#write()` method call produces the leading 40 characters using the [SHA1](https://en.wikipedia.org/wiki/SHA-1) message digest function taking the byte array of the file content as the input. This cryptic 40 characters uniquely identifies the input files regardless which type of the file content: a plain text, CSV, HTML, JSON, XML, PNG image, PDF, zipped archive, MS Excel xlsx, etc. This 40 characters is called as `ID` of a Material.

**You are not supposed to specify the name on the file in the materialstore. The ID of a Material is calculated based on the file content by the `Store` class. A single byte change in the file content will result a completely different value of the `ID`.**

### FileType

The line (7) specifies `FileType.TXT`.

                    FileType.TXT,                            // (7)

\(7\) assigns a file extension `txt` to the file name. The `com.kazurayam.materialstore.FileType` enum declares many concrete FileType instances ready to use. See
[`com.kazurayam.materialstore.core.FileType`](https://kazurayam.github.io/materialstore/api/com/kazurayam/materialstore/core/FileType.html) for the complete list. You can also create your own class that implements `com.kazurayam.materialstore.IFileType`. See <https://kazurayam.github.io/materialstore/api/com/kazurayam/materialstore/core/IFileType.html>. You can use your custom FileType wherever a FileType enum is accepted.

### Metadata

You can associate various metadata to each Material instances. The URL string (e.g., "https://www.google.com/?q=selenium") is a typical metadata of a screenshot of a web page.

The `T01HelloMaterialstoreTest` does not really make use of the Metadata. So, I wrote `Metadata.NULL_OBJECT` to fill the required parameter.

                    Metadata.NULL_OBJECT,                (8)

I will cover how to make full use of Metadata later.

### Types of objects writeable into the Store

The javadoc of the [`Store`](https://kazurayam.github.io/materialstore/api/com/kazurayam/materialstore/core/filesystem/Store.html) shows that it can accept multiple types of object as input to write into the `store`:

-   `java.lang.String`

-   `byte[]`

-   `java.nio.file.Path`

-   `java.io.File`

-   `java.awt.image.BufferedImage`

These types will cover the most cases in your automated UI testing.

## 2nd example: write an image with Metadata

I will show you next sample code `test02_write_image_with_metadata` of `T2MetadataTest` class.

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

At the line (10), we create an instance of `java.net.URL` with a String argument "<https://kazurayam.github.io/materialstore-tutorial/images/tutorial/03_apple.png>". You can click this URL to see the image yourself. You should see an apple.

I create a helper class named `my.sample.SharedMethod` with a method `createURL(String)` that instantiate an instance of `URL`.

**createURL(String)**

        public static URL createURL(String urlString) throws MaterialstoreException {
            try {
                return new URL(urlString);
            } catch (MalformedURLException e) {
                throw new MaterialstoreException(e);
            }
        }

At the statement (11) we get access to the URL. We will effectively download a PNG image file from the URL and obtain a large array of bytes.
The `downloadURL(URL)` method of `SharedMethods` class implements this processing: converting a URL to an array of bytes.

**downloadUrl(URL)**

        public static byte[] downloadUrl(URL toDownload) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                byte[] chunk = new byte[4096];
                int bytesRead;
                InputStream stream = toDownload.openStream();
                while ((bytesRead = stream.read(chunk)) > 0) {
                    outputStream.write(chunk, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            byte[] bytes = outputStream.toByteArray();
            assert bytes != null;
            assert bytes.length != 0;
            return bytes;
        }

The statement (12) invokes `store.write()` method, which create a new file tree, as this:

![07 writing image with metadata](../images/tutorial/07_writing_image_with_metadata.png)

### Metadata attributes based on key-value pairs explicitly specified, plus attributes based on a URL

the `index` file contains a single line of text, which is something like:

**index**

    27b2d39436d0655e7e8885c7f2a568a646164280 png {"label":"red apple", "step":"01", "URL.host":"kazurayam.github.io", "URL.path":"/materialstore-tutorial/images/tutorial/03_apple.png", "URL.port":"80", "URL.protocol":"https"}

Please find a JSON-like string enclosed by a pair of curly braces (`{` and `}`). I call this section as **Metadata** of a material. The Metadata contains several **"key":"value"** pairs. The metadata was created as specified by the line (13).

            Metadata.builder(url)           // (13)
                    .put("step", "01")
                    .put("label", "red apple")
                    .build(),

The `url` variable is an instance of `java.net.URL`. You should check the [Javadoc of `URL`](https://docs.oracle.com/javase/7/docs/api/java/net/URL.html). The constructor `new URL(String spec)` can accept a string like "<https://kazurayam.github.io/materialstore/images/tutorial/03_apple.png>" and parse it to its components: `protocol`, `host`, `port`, `path`, `query` and `fragment`. The `url` variable passed to the `Metadata.builder(url)` call is parsed by the URL class and transformed into key-value pairs like `"URL.hostname": "kazurayam.github.io"`.

Let me show you a few more examples.

The URL string
`"https://duckduckgo.com/?q=materialstore+kazurayam&atb=v314-1&ia=images"` will make the following Metadata instance:

    {"URL.host":"duckduckgo.com", "URL.path":"/", "URL.port":"80", "URL.protocol":"https", "URL.query":"q=materialstore+kazurayam&atb=v314-1&ia=images"}

The URL string `"https://kazurayam.github.io/materialstore/#first-sample-code-hello-materialstore"` will make the following Metadata instance:

    {"URL.fragment":"first-sample-code-hello-materialstore", URL.host":"kazurayam.github.io", "URL.path":"/", "URL.port":"80", "URL.protocol":"https"}

### Order of key-value pairs in Metadata

the key-value pairs in the pair of curly braces (`{` .. `}`) are arranged by the "key". Unless explicitly specified, the "keys" are sorted by the ascending order as string. Therefore, in the above example, the key `URL.fragment` comes first, the key "URL.protocol" comes last.

### key:value pairs explicitly specified

Also the line (13) explicitly created 2 pairs of key:value, that is `"step": "01"` and `"label":"red apple"`.

You can create key:value pairs as many as you want. Both of key and value must be the type String. No number, no boolean values are allowed. The key can be any string, the value can be any string as well. You can use any characters including `/` (forward slash), `\` (back slash), `:` (colon). You can use non-ASCII characters. For example: you can create a key-value pair `"番号": "123/456 xyz"`. The Character escaping rule of JSON applies here: a double quote character `"` will be escaped to `\"`; a back-slash character `\` will be escaped to be `\\`.

Metadata is stored in the `index` file, which is apart from the material file itself. The byte array of the image downloaded from a URL is not altered at all. The image is saved into the `objects` directory as is. And then, you can associate a flexible set of Metadata to each individual materials. What sort of Metadata to associate? --- it is completely up to you.

Metadata plays an important role later when you start compiling advanced reports "Chronos Diff" and "Twins Diff".

### Retrieving information of a Material

The line (14) prints the summarized information of the material: the ID, the FileType, and the Metadata.

You can also get various information out of the `material` variable. For example, the line (15) retrieves the value of `"URL.host"` out of the material object, compares it with the expected staring value.

            assertEquals("kazurayam.github.io",
                    material.getMetadata().get("URL.host"));        // (15)

Please check the [Javadoc of Material](https://kazurayam.github.io/materialstore/api/com/kazurayam/materialstore/core/Material.html) for what sort of accessor methods are implemented.

## 3rd example: writing multiple images

Web will make a test case which downloads 3 image files from public URL and store them into the store.

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

This code calls `SharedMethods.write3images(Store, JobName, JobTimestap)` method. It is implemented as this:

**SharedMethod.write3images**

            return bytes;
        }

        public static void write3images(Store store, JobName jn, JobTimestamp jt)          // (16)
                throws MaterialstoreException {
            String prefix =
                    "https://kazurayam.github.io/materialstore-tutorial/images/tutorial/";
            // Apple
            URL url1 = SharedMethods.createURL(prefix + "03_apple.png");
            store.write(jn, jt, FileType.PNG,
                    Metadata.builder(url1)
                            .putAll(ImmutableMap.of(
                                    "step", "01",
                                    "label", "red apple"))
                            .build(),
                    SharedMethods.downloadUrl(url1));
            // Mikan
            URL url2 = SharedMethods.createURL(prefix + "04_mikan.png");
            store.write(jn, jt, FileType.PNG,
                    Metadata.builder(url2)
                            .putAll(ImmutableMap.of(
                                    "step", "02",
                                    "label", "mikan"))
                            .build(),
                    SharedMethods.downloadUrl(url2));
            // Money
            URL url3 = SharedMethods.createURL(prefix + "05_money.png");
            store.write(jn, jt, FileType.PNG,
                    Metadata.builder(url3)
                            .putAll(ImmutableMap.of(
                                    "step", "03",
                                    "label", "money"))

This code makes HTTP requests to

-   <http://kazurayam.github.io/materialstore/images/tutorial/03_apple.png>

-   <http://kazurayam.github.io/materialstore/images/tutorial/04_mikan.png>

-   <http://kazurayam.github.io/materialstore/images/tutorial/05_money.png>

and save the image files into a directory inside the store. When you run this test case, you will get a new file tree as follows.

![08 writing multiple images](../images/tutorial/08_writing_multiple_images.png)

The `index` file will contain 3 lines, one for each PNG image file.

**index**

    8a997bec64cd056c2075da95c0c281320ee7a7c1 png {"label":"mikan", "step":"02", "URL.host":"kazurayam.github.io", "URL.path":"/materialstore/images/tutorial/04_mikan.png", "URL.port":"80", "URL.protocol":"https"}
    36f9f62bdb3ad45cb8c6bc1f4062fbbd4fd180db    png {"label":"money", "step":"03", "URL.host":"kazurayam.github.io", "URL.path":"/materialstore/images/tutorial/05_money.png", "URL.port":"80", "URL.protocol":"https"}
    27b2d39436d0655e7e8885c7f2a568a646164280    png {"label":"red apple", "step":"01", "URL.host":"kazurayam.github.io", "URL.path":"/materialstore/images/tutorial/03_apple.png", "URL.port":"80", "URL.protocol":"https"}

## 4th example : retrieving a saved material

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

## 5th example : Selecting a MaterialList

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

## 6th example : generate a HTML report of a MaterialList

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
            SharedMethods.write3images(store, jobName, jobTimestamp);

            MaterialList materialList =
                    store.select(jobName, jobTimestamp,
                            QueryOnMetadata.ANY);              // (18)

            Inspector inspector = Inspector.newInstance(store);
            inspector.setSortKeys(new SortKeys("step"));
            Path report = inspector.report(materialList);
            assertNotNull(report);
            System.out.println("report is found at " + report);
        }
    }
