package wtf.g4s8;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Sum matcher.
 * @since 1.0
 */
public final class SumIs extends TypeSafeMatcher<Sum> {

    /**
     * Expected sum;
     */
    private final int expected;

    /**
     * Ctor.
     * @param expected Expected sum
     */
    public SumIs(final int expected) {
        this.expected = expected;
    }

    @Override
    public boolean matchesSafely(final Sum item) {
        return this.expected == item.value();
    }

    @Override
    public void describeTo(final Description description) {
        description.appendValue(this.expected);
    }
}
