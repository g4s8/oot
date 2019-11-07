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
 * Test run.
 * @since 1.0
 */
public interface TestCase {

    /**
     * Test name.
     * @return Name string
     */
    String name();

    /**
     * Run test.
     * @param report Test report
     * @throws IOException On report error
     */
    void run(TestReport report) throws IOException;

    /**
     * Default decorator.
     * @since 1.0
     */
    abstract class Wrap implements TestCase {

        /**
         * Origin test.
         */
        private final TestCase test;

        /**
         * Ctor.
         * @param test Origin test
         */
        protected Wrap(final TestCase test) {
            this.test = test;
        }

        @Override
        public String name() {
            return this.test.name();
        }

        @Override
        public final void run(final TestReport report) throws IOException {
            this.test.run(report);
        }
    }
}
