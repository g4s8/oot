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

import java.io.IOException;
import wtf.g4s8.oot.ConsoleReport;
import wtf.g4s8.oot.FailingReport;
import wtf.g4s8.oot.ParallelTests;
import wtf.g4s8.oot.SequentialTests;
import wtf.g4s8.oot.SimpleTest;
import wtf.g4s8.oot.TestCase;

/**
 * Unit test for simple App.
 * @since 1.0
 * @checkstyle MagicNumberCheck (500 lines)
 */
@SuppressWarnings(
    {
        "PMD.JUnit4TestShouldUseTestAnnotation",
        "PMD.ProhibitPublicStaticMethods",
        "PMD.TestClassWithoutTestCases"
    }
)
public final class MainTest extends TestCase.Wrap {

    /**
     * Ctor of main test.
     */
    private MainTest() {
        super(
            new ParallelTests(
                new SimpleTest<>(
                    "one plus one is two",
                    new Sum(1, 1),
                    new SumIs(2)
                ),
                new SimpleTest<>(
                    "one minus one is zero",
                    new Sum(1, -1),
                    new SumIs(0)
                ),
                new SimpleTest<>(
                    "one plus two is three",
                    new Sum(1, 2),
                    new SumIs(3)
                ),
                new SimpleTest<>(
                    "two plus two is four",
                    new Sum(2, 2),
                    new SumIs(4)
                ),
                new SimpleTest<>(
                    "two plus one is three",
                    new Sum(2, 1),
                    new SumIs(3)
                ),
                new SequentialTests(
                    new SimpleTest<>(
                        "two minus one is one",
                        new Sum(2, -1),
                        new SumIs(1)
                    ),
                    new SimpleTest<>(
                        "two minus two is zero",
                        new Sum(2, -2),
                        new SumIs(0)
                    )
                )
            )
        );
    }

    /**
     * Entry point method.
     * @throws IOException If fails
     */
    public static void test() throws IOException {
        try (FailingReport report = new FailingReport(new ConsoleReport())) {
            new MainTest().run(report);
        }
    }
}
