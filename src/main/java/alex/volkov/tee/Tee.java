package alex.volkov.tee;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Tee {
    private static final int BUFFER_SIZE = 8192;

    public static boolean work(InputStream inp, Configuration config) {
        if (config == null) {
            throw new IllegalArgumentException("Configuration is null");
        }

        boolean error = false;

        List<OutputStream> outputs = new ArrayList<>();
        for (Path file : config.getAllFiles()) {
            try {
                outputs.add(new FileOutputStream(file.toFile(), config.isAppend()));
            } catch (FileNotFoundException | SecurityException e) {
                error = true;
                System.err.println("Error opening file " + file);
                e.printStackTrace();
            }
        }

        outputs.add(System.out);

        byte[] buffer = new byte[BUFFER_SIZE];
        int len;
        try {
            while ((len = inp.read(buffer)) != -1) {
                for (int i = 0; i < outputs.size(); i++) {
                    try {
                        outputs.get(i).write(buffer, 0, len);
                    } catch (IOException e) {
                        error = true;
                        System.err.println("Error writing to " +
                                (i < outputs.size() - 1 ? config.getFile(i).getFileName() : "console"));
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            error = true;
            System.err.println("Error occurred while read from input stream");
            e.printStackTrace();
        }

        for (int i = 0; i < outputs.size() - 1; i++) {
            try {
                outputs.get(i).close();
            } catch (IOException e) {
                error = true;
                System.err.println("Error closing file " + config.getFile(i));
                e.printStackTrace();
            }
        }

        return error | config.hasError();
    }
}
