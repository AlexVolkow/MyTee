package alex.volkov.tee;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TeeTest {
    private static String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new Random(23084701432182342L);

    private static final String[] files = new String[]{"inp1.txt", "inp2.txt"};
    private static final String text = "Hello world!";

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final String[] RIGHT =
            Collections.nCopies(files.length, text).toArray(new String[files.length]);

    private InputStream inp;

    @Before
    public void setUp() throws Exception {
        System.setOut(new PrintStream(outContent));
        StringReader reader = new StringReader(text);
        inp = new ReaderInputStream(reader);

        for (int i = 0; i < files.length; i++) {
            files[i] = randomFileName();
        }
    }

    @Test
    public void testNormalWork() throws IOException {
        Configuration config = ConfigParser.parse(false, Arrays.asList(files));

        Tee.work(inp, config);

        String[] outputs = getOutput();

        Assert.assertEquals(text, outContent.toString());
        Assert.assertArrayEquals(RIGHT, outputs);
    }

    private String[] getOutput() throws IOException {
        String[] outputs = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            BufferedReader reader = Files.newBufferedReader(Paths.get(files[i]));
            outputs[i] = reader.readLine();
        }
        return outputs;
    }

    private String randomFileName() {
        final StringBuilder sb = new StringBuilder();
        for (int j = 0; j < 30; j++) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    @Test
    public void testEmptyFileList() {
        Configuration config = ConfigParser.parse(false, Collections.emptyList());

        Tee.work(inp, config);

        Assert.assertEquals(text, outContent.toString());
    }

    @Test
    public void testIncorrectFiles() throws IOException {
        List<String> paths = new ArrayList<>(Arrays.asList(files));
        paths.add("8:.txt");
        paths.add("*$63.bina");

        Configuration config = ConfigParser.parse(false, paths);

        boolean status = Tee.work(inp, config);

        String[] outputs = getOutput();

        Assert.assertEquals(status, true);
        Assert.assertArrayEquals(outputs, RIGHT);
    }

    @Test
    public void testAppendFiles() throws IOException {
        for (int i = 0; i < files.length; i++) {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(files[i]));
            writer.write(text);
            writer.close();
        }

        Configuration config = ConfigParser.parse(true, Arrays.asList(files));

        Tee.work(inp, config);

        String[] outputs = getOutput();
        String[] right = new String[]{text + text, text + text};

        Assert.assertArrayEquals(outputs, right);
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(System.out);
        for (String file : files) {
            Path path = Paths.get(file);
            if (Files.exists(path)) {
                Files.deleteIfExists(path);
            }
        }
    }
}