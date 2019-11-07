package wtf.g4s8;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.apache.maven.plugin.logging.Log;

/**
 *
 * @since
 */
public final class TestCase {

    private final Class<?> cls;

    private final boolean dry;

    private final Log log;

    public TestCase(final Class<?> cls, final boolean dry, final Log log) {
        this.cls = cls;
        this.dry = dry;
        this.log = log;
    }

    public boolean run() throws InvocationTargetException, IllegalAccessException {
        for (final Method method : this.cls.getMethods()) {
            if (Modifier.isStatic(method.getModifiers()) && method.getParameterCount() == 0 && method.getName().equals("test")) {
                this.log.debug(
                    String.format(
                        "TestCase: starting %s#%s",
                        this.cls.getCanonicalName(), method.getName()
                    )
                );
                if (!this.dry) {
                    method.invoke(null);
                } else {
                    this.log.debug("TestCase: skipped due to dry run");
                }
                this.log.debug(
                    String.format(
                        "TestCase: successfully run the test %s",
                        this.cls.getCanonicalName()
                    )
                );
                return true;
            }
        }
        this.log.debug(
            String.format("TestCase: no test methods found for %s", this.cls.getCanonicalName())
        );
        return false;
    }
}
