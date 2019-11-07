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

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Test report which throw exception on failure.
 * @since 1.0
 */
public final class FailingReport implements TestReport, Closeable {

    /**
     * Any test failed.
     */
    private final AtomicBoolean err;

    /**
     * Origin report.
     */
    private final TestReport report;

    /**
     * Ctor.
     * @param report Report
     */
    public FailingReport(final TestReport report) {
        this.err = new AtomicBoolean();
        this.report = report;
    }

    @Override
    public void success(final TestCase test) throws IOException {
        this.report.success(test);
    }

    @Override
    public void failure(final TestCase test, final String error) throws IOException {
        this.err.set(true);
        this.report.failure(test, error);
    }

    @Override
    public void close() {
        if (this.err.get()) {
            throw new AssertionError("One or more tests failed");
        }
    }
}
