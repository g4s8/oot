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

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Chain of test runs.
 * @since 1.0
 */
public final class TestChain implements TestRun {

    /**
     * Test runs.
     */
    private final Iterable<TestRun> tests;

    /**
     * From test cases.
     * @param cases Test cases
     */
    public TestChain(final TestCase<?>... cases) {
        this(
            Stream.of(cases).map((Function<TestCase<?>, SimpleRun<?>>) SimpleRun::new)
                .collect(Collectors.toList())
        );
    }

    /**
     * From test runs.
     * @param tests Test runs
     */
    public TestChain(final TestRun... tests) {
        this(Arrays.asList(tests));
    }

    /**
     * Primary ctor.
     * @param tests Test runs
     */
    public TestChain(final Iterable<TestRun> tests) {
        this.tests = tests;
    }

    @Override
    public void run() throws AssertionError {
        for (final TestRun test : this.tests) {
            test.run();
        }
    }
}
