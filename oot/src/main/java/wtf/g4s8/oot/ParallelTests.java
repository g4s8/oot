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
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import net.jcip.annotations.ThreadSafe;

/**
 * Parallel tests.
 * @since 1.0
 */
public final class ParallelTests implements TestCase {

    /**
     * Test runs.
     */
    private final Iterable<TestCase> tests;

    /**
     * Executors.
     */
    private final ExecutorService exec;

    /**
     * From test runs.
     * @param tests Test runs
     */
    public ParallelTests(final TestCase... tests) {
        this(Arrays.asList(tests));
    }

    /**
     * Ctor.
     * @param tests Test cases
     */
    public ParallelTests(final Iterable<TestCase> tests) {
        this(tests, Executors.newCachedThreadPool());
    }

    /**
     * Primary ctor.
     * @param tests Test runs
     * @param exec Executors
     */
    public ParallelTests(final Iterable<TestCase> tests, final ExecutorService exec) {
        this.tests = tests;
        this.exec = exec;
    }

    @Override
    public String name() {
        return StreamSupport.stream(this.tests.spliterator(), false)
            .map(TestCase::name)
            .collect(Collectors.joining(","));
    }

    @Override
    public void run(final TestReport report) throws IOException {
        final SynchronizedReport wrap = new SynchronizedReport(report);
        final List<Callable<Void>> tasks = StreamSupport.stream(this.tests.spliterator(), false)
            .map(test -> new TestRunnable(test, wrap))
            .collect(Collectors.toList());
        try {
            this.exec.invokeAll(tasks);
        } catch (final InterruptedException ignore) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Synchronized test report.
     * @since 1.0
     */
    @ThreadSafe
    private static final class SynchronizedReport implements TestReport {

        /**
         * Origin report.
         */
        private final TestReport origin;

        /**
         * Lock object.
         */
        private final Object lock;

        /**
         * Ctor.
         * @param origin Origin report
         */
        SynchronizedReport(final TestReport origin) {
            this.origin = origin;
            this.lock = new Object();
        }

        @Override
        public void success(final TestCase test) throws IOException {
            synchronized (this.lock) {
                this.origin.success(test);
            }
        }

        @Override
        public void failure(final TestCase test, final String error) throws IOException {
            synchronized (this.lock) {
                this.origin.failure(test, error);
            }
        }
    }

    /**
     * Test runnable.
     * @since 1.0
     */
    private static final class TestRunnable implements Callable<Void> {

        /**
         * Test case.
         */
        private final TestCase test;

        /**
         * Test report.
         */
        private final TestReport report;

        /**
         * Ctor.
         * @param test Test case
         * @param report Test report
         */
        TestRunnable(final TestCase test, final TestReport report) {
            this.test = test;
            this.report = report;
        }

        @Override
        public Void call() throws Exception {
            this.test.run(this.report);
            return null;
        }
    }
}
