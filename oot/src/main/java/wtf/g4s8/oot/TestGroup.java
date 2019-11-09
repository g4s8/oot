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
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Group of tests.
 * <p>
 * If group name specified, each test will be prefixed with group name.
 * </p>
 * @since 1.0
 */
@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation")
public interface TestGroup {

    /**
     * Groupped tests.
     * @return Collection of test cases
     */
    Collection<TestCase> tests();

    /**
     * Default decorator.
     * @since 1.0
     */
    abstract class Wrap implements TestGroup {

        /**
         * Origin group.
         */
        private final TestGroup origin;

        /**
         * Wrap tests.
         * @param tests Tests to wrap
         */
        protected Wrap(final TestCase... tests) {
            this(Arrays.asList(tests));
        }

        /**
         * Wrap collection of tests.
         * @param tests Test to wrap
         */
        protected Wrap(final Collection<TestCase> tests) {
            this(new TestGroup.Of(tests));
        }

        /**
         * Wrap test group.
         * @param origin Origin group
         */
        protected Wrap(final TestGroup origin) {
            this.origin = origin;
        }

        @Override
        public final Collection<TestCase> tests() {
            return this.origin.tests();
        }
    }

    /**
     * Test group of tests.
     * @since 1.0
     */
    final class Of implements TestGroup {

        /**
         * Group name.
         */
        private final String name;

        /**
         * Tests.
         */
        private final Collection<TestCase> tsts;

        /**
         * Ctor without group name.
         * @param tests Tests
         */
        public Of(final TestCase... tests) {
            this("", tests);
        }

        /**
         * Ctor.
         * @param name Group name
         * @param tests Tests
         */
        public Of(final String name, final TestCase... tests) {
            this(name, Arrays.asList(tests));
        }

        /**
         * Ctor without group name.
         * @param tests Tests
         */
        public Of(final Collection<TestCase> tests) {
            this("", tests);
        }

        /**
         * Ctor.
         * @param name Group name
         * @param tests Tests
         */
        public Of(final String name, final Collection<TestCase> tests) {
            this.name = Objects.requireNonNull(name);
            this.tsts = Collections.unmodifiableCollection(Objects.requireNonNull(tests));
        }

        @Override
        public Collection<TestCase> tests() {
            final Collection<TestCase> result;
            if (this.name.isEmpty()) {
                result = this.tsts.stream()
                    .map(test -> new PrefixedTest(test, String.format("%s: ", this.name)))
                    .collect(Collectors.toList());
            } else {
                result = this.tsts;
            }
            return result;
        }
    }

    /**
     * Joined test groups.
     * @since 1.0
     */
    final class Joined implements TestGroup {

        /**
         * Groups.
         */
        private final Collection<TestGroup> groups;

        /**
         * Ctor.
         * @param groups Groups to join
         */
        public Joined(final TestGroup... groups) {
            this(Arrays.asList(groups));
        }

        /**
         * Ctor.
         * @param groups Groups to join
         */
        public Joined(final Collection<TestGroup> groups) {
            this.groups = groups;
        }

        @Override
        public Collection<TestCase> tests() {
            return this.groups.stream()
                .flatMap(group -> group.tests().stream())
                .collect(Collectors.toList());
        }
    }
}
