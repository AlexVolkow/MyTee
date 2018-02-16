package alex.volkov.tee;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ConfigParser {
    public static Configuration parse(boolean append, List<String> paths) {
        if (paths == null) {
            throw new IllegalArgumentException("Second argument not can be null");
        }

        boolean error = false;
        List<Path> files = new ArrayList<>();
        for (String path : paths) {
            try {
                files.add(Paths.get(path));
            } catch (InvalidPathException e) {
                error = true;
                System.err.println("Incorrect file path " + path);
            }
        }
        return new Configuration(append, files, error);
    }
}
