package bench;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Csv {
    private final PrintWriter out;

    public Csv(String path, String header) throws IOException {
        Files.createDirectories(Path.of(path).toAbsolutePath().getParent());
        out = new PrintWriter(Files.newBufferedWriter(Path.of(path)));
        out.println(header);
    }

    public void row(Object... cols)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cols.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(cols[i]);
        }
        out.println(sb);
        out.flush();
    }

    public void close() { out.close(); }
}
