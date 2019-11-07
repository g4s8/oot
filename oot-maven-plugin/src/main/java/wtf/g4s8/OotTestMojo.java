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
 */
@Mojo(name = "test", defaultPhase = LifecyclePhase.TEST, requiresDependencyResolution = ResolutionScope.TEST)
public class OotTestMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(defaultValue = "false", property = "skipTests")
    private boolean skip;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (this.skip) {
            this.getLog().info("Tests are skipped");
            return;
        }
        final Set<String> tests;
        try {
            tests = new TestNames(Paths.get(this.project.getBuild().getTestOutputDirectory())).names();
        } catch (final IOException err) {
            throw new MojoExecutionException("Failed to resolve test files", err);
        }
        tests.forEach(test -> this.getLog().debug("Found test class: " + test));
        final Set<URL> classpath;
        try {
            classpath = new ProjectClassPath(this.project, this.getLog()).urls();
        } catch (final IOException err) {
            throw new MojoExecutionException("Failed to resolve classpath", err);
        }
        this.getLog().debug("resolved classpath:\n\t" + classpath.stream().map(URL::toString).collect(Collectors.joining("\n\t")));
        try (final URLClassLoader loader = new URLClassLoader(classpath.toArray(new URL[0]))) {
            for (final String test : tests) {
                if (new TestCase(loader.loadClass(test), false, this.getLog()).run()) {
                    this.getLog().info("Successfully run the test: " + test);
                    break;
                }
            }
        } catch (final ClassNotFoundException | IllegalAccessException err) {
            throw new MojoExecutionException("Failed to run test", err);
        } catch (final InvocationTargetException err) {
            if (err.getTargetException().getClass().getCanonicalName().equals(AssertionError.class.getCanonicalName())) {
                throw new MojoFailureException("Tests failed", err.getTargetException());
            }
        } catch (final IOException err) {
            throw new MojoExecutionException("Failed to close classloader", err);
        }
    }
}
