package alex.volkov.tee;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class Configuration {
    private boolean append;
    private List<Path> files;
    private boolean error;

    public Configuration(boolean append, List<Path> files, boolean error) {
        this.append = append;
        this.files = files;
        this.error = error;
    }

    public boolean isAppend() {
        return append;
    }

    public List<Path> getAllFiles() {
        return files;
    }

    public Path getFile(int i) {
        return files.get(i);
    }

    public boolean hasError() {
        return error;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "append=" + append +
                ", files=" + files +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return append == that.append &&
                error == that.error &&
                Objects.equals(files, that.files);
    }

    @Override
    public int hashCode() {
        return Objects.hash(append, files, error);
    }
}
