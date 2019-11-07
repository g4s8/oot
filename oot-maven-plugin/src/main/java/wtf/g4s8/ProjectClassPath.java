package wtf.g4s8;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

/**
 * Project classpath with target output dir, test output dir
 * and all dependencies.
 * @since 1.0
 */
public final class ProjectClassPath implements ClassPath {

    /**
     * Maven project.
     */
    private final MavenProject project;

    /**
     * Log.
     */
    private final Log log;

    /**
     * Ctor.
     * @param project Maven project
     * @param log Log
     */
    public ProjectClassPath(final MavenProject project, final Log log) {
        this.project = project;
        this.log = log;
    }

    @Override
    public Set<URL> urls() throws IOException {
        final Set<URL> out = new HashSet<>();
        final URL build = new File(this.project.getBuild().getOutputDirectory()).toURI().toURL();
        out.add(build);
        this.log.debug(String.format("ProjectClassPath: added builDir URL: %s", build));
        final URL tests = new File(this.project.getBuild().getTestOutputDirectory()).toURI().toURL();
        out.add(tests);
        this.log.debug(String.format("ProjectClassPath: added testDir URL: %s", tests));
        for (final Artifact dependency : this.project.getArtifacts()) {
            final URL dep = dependency.getFile().toURI().toURL();
            out.add(dep);
            this.log.debug(String.format("ProjectClassPath: added dependency URL: %s", dep));
        }
        return out;
    }
}
