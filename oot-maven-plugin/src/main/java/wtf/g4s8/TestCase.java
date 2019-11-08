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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.apache.maven.plugin.logging.Log;

/**
 * Test case.
 * @since 1.0
 */
public final class TestCase {

    /**
     * Test case class.
     */
    private final Class<?> cls;

    /**
     * Dry run.
     */
    private final boolean dry;

    /**
     * Log.
     */
    private final Log log;

    /**
     * Ctor.
     * @param cls Class
     * @param dry Dry run
     * @param log Log
     */
    public TestCase(final Class<?> cls, final boolean dry, final Log log) {
        this.cls = cls;
        this.dry = dry;
        this.log = log;
    }

    /**
     * Run test case.
     * @return True if run
     * @throws InvocationTargetException If failed to invoke test method
     * @throws IllegalAccessException On illegal access
     * @checkstyle ReturnCountCheck (50 lines)
     */
    @SuppressWarnings("PMD.OnlyOneReturn")
    public boolean run() throws InvocationTargetException, IllegalAccessException {
        for (final Method method : this.cls.getMethods()) {
            if (TestCase.isMain(method)) {
                this.log.debug(
                    String.format(
                        "TestCase: starting %s#%s",
                        this.cls.getCanonicalName(), method.getName()
                    )
                );
                if (this.dry) {
                    this.log.info("TestCase: skipped due to dry run");
                } else {
                    method.invoke(null);
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

    /**
     * Check method is test main method.
     * @param method Method to check
     * @return True if main
     */
    private static boolean isMain(final Method method) {
        return method.getName().equals("test")
            && method.getParameterCount() == 0
            && Modifier.isStatic(method.getModifiers());
    }
}
