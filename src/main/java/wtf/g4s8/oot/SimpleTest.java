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

import java.util.function.Supplier;
import org.hamcrest.Matcher;

/**
 * Simple test case.
 * @param <T> Target type
 * @since 1.0
 */
public final class SimpleTest<T> implements TestCase<T> {

    /**
     * Matcher.
     */
    private final Matcher<T> mtchr;

    /**
     * Name.
     */
    private final String nme;

    /**
     * Target.
     */
    private final Supplier<T> trgt;

    /**
     * Ctor.
     * @param name Name
     * @param matcher Matcher
     * @param target Target
     */
    public SimpleTest(final String name, final Matcher<T> matcher, final T target) {
        this(name, matcher, () -> target);
    }

    /**
     * Ctor.
     * @param name Name
     * @param matcher Matcher
     * @param target Target
     */
    public SimpleTest(final String name, final Matcher<T> matcher, final Supplier<T> target) {
        this.mtchr = matcher;
        this.nme = name;
        this.trgt = target;
    }

    @Override
    public String name() {
        return this.nme;
    }

    @Override
    public Matcher<T> matcher() {
        return this.mtchr;
    }

    @Override
    public T target() {
        return this.trgt.get();
    }
}
