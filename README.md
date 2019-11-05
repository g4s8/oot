Experimental Object Oriented Testing Framework for Java

[![EO principles respected here](http://www.elegantobjects.org/badge.svg)](http://www.elegantobjects.org)
[![DevOps By Rultor.com](http://www.rultor.com/b/g4s8/oot)](http://www.rultor.com/p/g4s8/oot)

```
@todo #1:30min Configure CI build.
 CircleCI, Travis and Appveyor for PR and commit statuses.
 Rultor for merging. CI builds should run tests, style checkers,
 pdd validators. Add coverage checks.
```
[![Build Status](https://img.shields.io/travis/g4s8/oot.svg?style=flat-square)](https://travis-ci.org/g4s8/oot)
[![Build status](https://ci.appveyor.com/api/projects/status/true?svg=true)](https://ci.appveyor.com/project/g4s8/oot)
[![PDD status](http://www.0pdd.com/svg?name=g4s8/oot)](http://www.0pdd.com/p?name=g4s8/oot)
[![License](https://img.shields.io/github/license/g4s8/oot.svg?style=flat-square)](https://github.com/g4s8/oot/blob/master/LICENSE)

## Concepts

 1. Use single entry points for all tests
 2. Use composition to construct test cases
 3. Control test runs behavior in the code
 4. Write test-cases objects instead of classes with test functions

## Usage

Add dependency to your `pom.xml`:
```
@todo #1:30min Publish artifact to maven central.
 Then update the readme with dependency references
 and install instructions. Automate publish with CI.
```

Create Junit test class, run any implementation of `TestRun` in the
test method:
```java
@Test
public void mainTest() {
  new SimpleRun(
    new SimpleTest<>("true is not false", Matchers.is(true), !false)
  ).run();
}
```
Implementations:
 - `TestChain` - can aggregate multiple `TestRun`s to run it sequentially
 - `SimpleRun` - wraps generic `TestCase`
 - `TestIf` - conditional test which skips test case if condition didn't pass

Test cases:
 - `SimpleTest` - simple `TestCase` implementation with name, lazy target loader and Hamcrest matcher


## Examples

```java
/**
 * Object to test
 */
class Calc {
    int sum(int x, int y) {
        return x + y;
    }
}

/**
 * Test case object.
 */
class CalcTest extends TestRun.Wrap {
    public CalcTest(final Calc calc) {
        super(
            new TestChain(
                new SimpleTest<Integer>(
                    "sum of zeros is zero",
                    Matchers.is(0), () -> calc.sum(0, 0)
                ),
                new SimpleTest<Integer>(
                    "sum of ones is two",
                    Matchers.is(2), () -> calc.sum(1, 1)
                )
            )
        );
    }
}

/**
 * All tests entrypoint.
 */
class UnitTests {
    @Test
    public void test() {
        new TestChain(
            // run calc test first
            new CalcTest(new Calc()),
            // then other test case
            new OtherTest(),
            // run conditional test only if CI_BUILD property is present
            new TestIf(
              () -> System.getProperty("CI_BUILD") != null,
              new ConditionalTest()
            ),
            // then run one more test case
            new AnotherTest()
        // start them all
        ).run();
    }
}
```
