/*
 * MIT License
 *
 * Copyright (c) 2019 g4s8
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files
 * (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights * to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
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

    /**
     * Test classes lookup max depth.
     */
    private static final int MAX_DEPTH = 100;

    /**
     * Class file extension.
     */
    private static final String CLASS_EXT = ".class";

    /**
     * Base path.
     */
    private final Path base;

    /**
     * Ctor.
     * @param base Base path
     */
    public TestNames(final Path base) {
        this.base = base;
    }

    /**
     * Test names.
     * @return Names set
     * @throws IOException If fails
     */
    public Set<String> names() throws IOException {
        return Files.find(
            this.base, TestNames.MAX_DEPTH,
            (path, attr) -> path.getFileName().toString().endsWith(TestNames.CLASS_EXT),
            FileVisitOption.FOLLOW_LINKS
        ).map(this.base::relativize).map(TestNames::toPackageName).collect(Collectors.toSet());
    }

    /**
     * Package name from path.
     * @param path Path
     * @return Name string
     */
    private static String toPackageName(final Path path) {
        final StringBuilder builder = new StringBuilder();
        for (int part = 0; part < path.getNameCount(); ++part) {
            if (part < path.getNameCount() - 1) {
                builder.append(path.getName(part).toString()).append('.');
            } else {
                builder.append(path.getName(part).toString().replace(TestNames.CLASS_EXT, ""));
            }
        }
        return builder.toString();
    }
}
