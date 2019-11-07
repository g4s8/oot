package wtf.g4s8;

import java.io.IOException;
import java.util.function.Supplier;
import wtf.g4s8.oot.ConsoleReport;
import wtf.g4s8.oot.FailingReport;
import wtf.g4s8.oot.ParallelTests;
import wtf.g4s8.oot.SequentialTests;
import wtf.g4s8.oot.SimpleTest;
import wtf.g4s8.oot.TestCase;

/**
 * Unit test for simple App.
 */
public class MainTest extends TestCase.Wrap {

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
                    withDelay(new Sum(2, 2)),
                    new SumIs(4)
                ),
                new SimpleTest<>(
                    "two plus one is three",
                    withDelay(new Sum(2, 1)),
                    new SumIs(3)
                ),
                new SequentialTests(
                    new SimpleTest<>(
                        "two minus one is one",
                        withDelay(new Sum(2, -1)),
                        new SumIs(1)
                    ),
                    new SimpleTest<>(
                        "two minus two is zero",
                        withDelay(new Sum(2, -2)),
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
        try (final FailingReport report = new FailingReport(new ConsoleReport())) {
            new MainTest().run(report);
        }
    }

    private static <T> Supplier<T> withDelay(final T target) {
        return () -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
            return target;
        };
    }
}
