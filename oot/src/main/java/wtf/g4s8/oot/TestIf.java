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

/**
 * Conditional test.
 * @since 1.0
 */
public final class TestIf implements TestCase {

    /**
     * Condition.
     */
    private final Supplier<Boolean> cond;

    /**
     * Origin test.
     */
    private final TestCase origin;

    /**
     * Ctor.
     * @param cond Condition to enable the test
     * @param origin Test to run if condition ok
     */
    public TestIf(final Supplier<Boolean> cond, final TestCase origin) {
        this.cond = cond;
        this.origin = origin;
    }

    @Override
    public String name() {
        return this.origin.name();
    }

    @Override
    public void run(final TestReport report) throws IOException {
        if (this.cond.get()) {
            this.origin.run(report);
        }
    }
}
