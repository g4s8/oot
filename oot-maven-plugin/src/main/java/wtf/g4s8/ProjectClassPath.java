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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
        final URL build = ProjectClassPath.toUrl(this.project.getBuild().getOutputDirectory());
        out.add(build);
        this.log.debug(String.format("ProjectClassPath: added builDir URL: %s", build));
        final URL tests =
            ProjectClassPath.toUrl(this.project.getBuild().getTestOutputDirectory());
        out.add(tests);
        this.log.debug(String.format("ProjectClassPath: added testDir URL: %s", tests));
        for (final Artifact dependency : this.project.getArtifacts()) {
            final URL dep = dependency.getFile().toURI().toURL();
            out.add(dep);
            this.log.debug(String.format("ProjectClassPath: added dependency URL: %s", dep));
        }
        return out;
    }

    /**
     * Convert string path to URL.
     * @param path String path
     * @return URL
     * @throws MalformedURLException If path is invalid
     */
    private static URL toUrl(final String path) throws MalformedURLException {
        return new File(path).toURI().toURL();
    }
}
