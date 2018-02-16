package alex.volkov.tee;

import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class ConfigParserTest {

    @Test
    public void testNormalPaths() {
        String[] files = new String[]{"from.txt", "to1.txt", "to2.txt"};
        Path[] paths = new Path[]{Paths.get("from.txt"), Paths.get("to1.txt"), Paths.get("to2.txt")};

        Configuration config = ConfigParser.parse(true, Arrays.asList(files));
        Configuration rightConfig = new Configuration(true, Arrays.asList(paths), false);
        Assert.assertEquals(config, rightConfig);
    }

    @Test
    public void testInvalidPaths() {
        String[] files = new String[]{"*.in", ":.vvv", "normal.txt"};
        Path[] paths = new Path[]{Paths.get("normal.txt")};

        Configuration config = ConfigParser.parse(false, Arrays.asList(files));
        Configuration rightConfig = new Configuration(false, Arrays.asList(paths), true);
        Assert.assertEquals(config, rightConfig);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSecondArgNull() {
        Configuration config = ConfigParser.parse(true, null);
    }
}