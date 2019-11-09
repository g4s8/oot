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
package wtf.g4s8.oot;

import java.io.IOException;
import java.util.function.Supplier;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

/**
 * Simple test run.
 * @param <T> Test case target type
 * @since 1.0
 */
@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation")
public final class SimpleTest<T> implements TestCase {

    /**
     * Target to test.
     */
    private final Supplier<T> target;

    /**
     * Matcher.
     */
    private final Matcher<T> matcher;

    /**
     * Message.
     */
    private final String message;

    /**
     * Ctor.
     * @param message Message
     * @param target Target
     * @param matcher Matcher
     */
    public SimpleTest(final String message, final T target, final Matcher<T> matcher) {
        this(message, new SimpleTest.ConstTarget<>(target), matcher);
    }

    /**
     * Ctor.
     * @param message Message
     * @param target Target
     * @param matcher Matcher
     */
    public SimpleTest(final String message, final Supplier<T> target, final Matcher<T> matcher) {
        this.target = target;
        this.matcher = matcher;
        this.message = message;
    }

    @Override
    public String name() {
        return this.message;
    }

    @Override
    @SuppressWarnings("PMD.SystemPrintln")
    public void run(final TestReport report) throws IOException {
        final T actual = this.target.get();
        if (this.matcher.matches(actual)) {
            report.success(this);
        } else {
            final Description description = new StringDescription();
            description.appendText(this.message)
                .appendText("\nExpected: ")
                .appendDescriptionOf(this.matcher)
                .appendText("\n     but: ");
            this.matcher.describeMismatch(actual, description);
            report.failure(this, description.toString());
        }
    }

    /**
     * Constant target supplier.
     * @param <T> Target type
     * @since 1.0
     */
    private static final class ConstTarget<T> implements Supplier<T> {

        /**
         * Target.
         */
        private final T target;

        /**
         * Ctor.
         * @param target Target
         */
        private ConstTarget(final T target) {
            this.target = target;
        }

        @Override
        public T get() {
            return this.target;
        }
    }
}
