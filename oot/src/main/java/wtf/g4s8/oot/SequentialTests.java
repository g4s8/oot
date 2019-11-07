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
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Chain of test runs.
 * @since 1.0
 */
public final class SequentialTests implements TestCase {

    /**
     * Test runs.
     */
    private final Iterable<TestCase> tests;

    /**
     * From test runs.
     * @param tests Test runs
     */
    public SequentialTests(final TestCase... tests) {
        this(Arrays.asList(tests));
    }

    /**
     * Primary ctor.
     * @param tests Test runs
     */
    public SequentialTests(final Iterable<TestCase> tests) {
        this.tests = tests;
    }

    @Override
    public String name() {
        return StreamSupport.stream(this.tests.spliterator(), false)
            .map(TestCase::name)
            .collect(Collectors.joining(","));
    }

    @Override
    public void run(final TestReport report) throws IOException {
        for (final TestCase test : this.tests) {
            test.run(report);
        }
    }
}
