package wtf.g4s8;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Test classes names.
 * @since 1.0
 */
public final class TestNames {

    private final Path base;

    public TestNames(final Path base) {
        this.base = base;
    }

    public Set<String> names() throws IOException {
        return Files.find(
            this.base, 100,
            (path, attr) -> path.getFileName().toString().endsWith(".class"),
            FileVisitOption.FOLLOW_LINKS
        ).map(this.base::relativize).map(TestNames::toPackageName).collect(Collectors.toSet());
    }

    private static String toPackageName(final Path path) {
        final StringBuilder builder = new StringBuilder();
        for (int part = 0; part < path.getNameCount(); part++) {
            if (part < path.getNameCount() - 1) {
                builder.append(path.getName(part).toString()).append(".");
            } else {
                builder.append(path.getName(part).toString().replace(".class", ""));
            }
        }
        return builder.toString();
    }
}
