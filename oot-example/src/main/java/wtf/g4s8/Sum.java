package wtf.g4s8;

import java.util.Arrays;

/**
 * Sum of numbers.
 * @since 1.0
 */
public final class Sum {

    /**
     * Numbers.
     */
    private final Iterable<Integer> nums;

    /**
     * Ctor.
     * @param nums Numbers
     */
    public Sum(final Integer... nums) {
        this(Arrays.asList(nums));
    }

    /**
     * Ctor.
     * @param nums Numbers
     */
    public Sum(final Iterable<Integer> nums) {
        this.nums = nums;
    }

    /**
     * Value.
     * @return Sum of numbers
     */
    public int value() {
        int sum = 0;
        for (final Integer num : this.nums) {
            sum += num;
        }
        return sum;
    }

    @Override
    public String toString() {
        return Integer.toString(this.value());
    }
}
