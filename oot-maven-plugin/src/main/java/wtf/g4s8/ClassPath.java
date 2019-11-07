package wtf.g4s8;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

/**
 * Classpath entries.
 * @since 1.0
 */
public interface ClassPath {

    Set<URL> urls() throws IOException;
}
