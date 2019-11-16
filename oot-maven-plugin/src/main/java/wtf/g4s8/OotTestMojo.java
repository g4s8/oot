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
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

/**
 * Goal to run test cases.
 * @since 1.0
 * @checkstyle ExecutableStatementCountCheck (500 lines)
 */
@Mojo(
    name = "test", defaultPhase = LifecyclePhase.TEST,
    requiresDependencyResolution = ResolutionScope.TEST
)
public final class OotTestMojo extends AbstractMojo {

    /**
     * Maven project.
     */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    /**
     * Skip tests.
     */
    @Parameter(defaultValue = "false", property = "skipTests")
    private boolean skip;

    @Override
    @SuppressWarnings("PMD.CyclomaticComplexity")
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (this.skip) {
            this.getLog().info("Tests are skipped");
            return;
        }
        final Set<String> tests;
        try {
            tests = new TestNames(
                Paths.get(this.project.getBuild().getTestOutputDirectory())
            ).names();
        } catch (final IOException err) {
            throw new MojoExecutionException("Failed to resolve test files", err);
        }
        tests.forEach(
            test -> this.getLog().debug(String.format("Found test class: %s", test))
        );
        final Set<URL> classpath;
        try {
            classpath = new ProjectClassPath(this.project, this.getLog()).urls();
        } catch (final IOException err) {
            throw new MojoExecutionException("Failed to resolve classpath", err);
        }
        this.getLog().debug(
            String.format(
                "resolved classpath:\n\t%s",
                classpath.stream().map(URL::toString)
                    .collect(Collectors.joining("\n\t"))
            )
        );
        try (URLClassLoader loader = new URLClassLoader(classpath.toArray(new URL[0]))) {
            for (final String test : tests) {
                if (new TestCase(loader.loadClass(test), false, this.getLog()).run()) {
                    this.getLog().info(String.format("Successfully run the test: %s", test));
                    break;
                }
            }
        } catch (final ClassNotFoundException | IllegalAccessException err) {
            throw new MojoExecutionException("Failed to run test", err);
        } catch (final InvocationTargetException err) {
            final String name = err.getTargetException().getClass().getCanonicalName();
            if (name.equals(AssertionError.class.getCanonicalName())) {
                this.getLog().error("Test failed", err.getTargetException());
                throw new MojoFailureException("Tests failed", err);
            } else {
                this.getLog().error("Test error", err.getTargetException());
                throw new MojoExecutionException("Tests error", err);
            }
        } catch (final IOException err) {
            throw new MojoExecutionException("Failed to close classloader", err);
        }
    }
}
