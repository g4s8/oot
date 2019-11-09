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

/**
 * Test case decorator which adds prefix to test name.
 * @since 1.0
 */
@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation")
public final class PrefixedTest implements TestCase {

    /**
     * Origin test case.
     */
    private final TestCase origin;

    /**
     * Prefix.
     */
    private final String prefix;

    /**
     * Ctor.
     * @param origin Origin test case
     * @param prefix Prefix to add
     */
    public PrefixedTest(final TestCase origin, final String prefix) {
        this.origin = origin;
        this.prefix = prefix;
    }

    @Override
    public String name() {
        return String.join("", this.prefix, this.origin.name());
    }

    @Override
    public void run(final TestReport report) throws IOException {
        this.origin.run(report);
    }
}
