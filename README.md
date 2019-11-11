Experimental Object Oriented Testing Framework for Java

[![EO principles respected here](http://www.elegantobjects.org/badge.svg)](http://www.elegantobjects.org)
[![DevOps By Rultor.com](http://www.rultor.com/b/g4s8/oot)](http://www.rultor.com/p/g4s8/oot)

oot:
[![Maven Central](https://img.shields.io/maven-central/v/wtf.g4s8.oot/oot)](https://maven-badges.herokuapp.com/maven-central/wtf.g4s8.oot/oot),
oot-maven-plugin:
[![Maven Central](https://img.shields.io/maven-central/v/wtf.g4s8.oot/oot-maven-plugin)](https://maven-badges.herokuapp.com/maven-central/wtf.g4s8.oot/oot-maven-plugin)

[![Build Status](https://img.shields.io/travis/g4s8/oot.svg?style=flat-square)](https://travis-ci.org/g4s8/oot)
[![Build status](https://ci.appveyor.com/api/projects/status/3wvrniluu0qpmi7d?svg=true)](https://ci.appveyor.com/project/g4s8/oot)
[![CircleCI](https://circleci.com/gh/g4s8/oot.svg?style=svg)](https://circleci.com/gh/g4s8/oot)
[![PDD status](http://www.0pdd.com/svg?name=g4s8/oot)](http://www.0pdd.com/p?name=g4s8/oot)
[![License](https://img.shields.io/github/license/g4s8/oot.svg?style=flat-square)](https://github.com/g4s8/oot/blob/master/LICENSE)

## Concepts

 1. Use single entry points for all tests
 2. Use composition to construct test cases
 3. Control test runs behavior in the code
 4. Write test-cases objects instead of classes with test functions

## Prerequirements

## Usage

Disable `maven-surefire-plugin` by adding `skipTests` property to configuration,
add `oot-maven-plugin` and `oot` library dependency to `pom.xml`:
```xml
<project>
  <dependencies>
    <dependency>
      <groupId>wtf.g4s8.oot</groupId>
      <artifactId>oot</artifactId>
      <!-- use latest release version or add snapshots repo -->
      <version>1.0-SNAPSHOT</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
  <build>
    <plugins>
      <plugin>
        <artifactId>maven-surefire-plugin</artifactId>
        <configuration>
          <skipTests>true</skipTests>
        </configuration>
      </plugin>
      <plugin>
        <groupId>wtf.g4s8.oot</groupId>
        <artifactId>oot-maven-plugin</artifactId>
        <!-- use latest release version or add snapshots repo -->
        <version>1.0-SNAPSHOT</version>
        <executions>
          <execution>
            <goals>
              <goal>test</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
</project>
```

Maven Plugin will be executed on `test` phase, supported properties are:
 - `skipTests` - disable tests (optional, default `false`)

It will search for public class with `public` `static` `void` `test`
method, same as `java` cli searches for `main` method.

*Hint: check `./oot-example` project for real example Maven project.*

To write first test, create public test class with method `test`:
```java
public final MainTest {
  public static void test() {
    // run tests here
  }
}
```

`oot` library use `TestReport` implementations to report test results and
fail `test` phase on test failure. And `TestCase` implementation to wrap test
logic:
```java
public final MainTest {
  public static void test() {
    try (FailingReport report = new FailingReport(new ConsoleReport())) {
      new SimpleTest<>("basic test", () -> 1 + 1, Matchers.is(2));
    }
  }
}
```
In this example:
 - `FailingReport` will fail `test` phase after all tests if any test fails
 - `ConsoleReport` will print all test results to stdout
 - `SimpleTest` - generic object to apply [Hamcrest matcher](http://hamcrest.org/JavaHamcrest/)
 to target object


To run multiple tests sequentially use:
```java
new SequentialTests(
  new FooTest(),
  new BarTest(),
  new BazTest()
)
```

To run it parallelly use:
```java
new ParallelTests(
  new FooTest(),
  new BarTest(),
  new BazTest()
)
```

## Decorating

`TestCase` has default decorator `TestCase.Wrap`.
It can be used to move all tests composition into
main test constructor:
```java
public final class MainTest extends TestCase.Wrap {
  private MainTest() {
    super(
      new ParallelTests(
        new TestOne(),
        new TestTwo(),
        new TestThree()
      )
    );
  }

  public static void test() throws IOException {
    try (FailingReport report = new FailingReport(new ConsoleReport())) {
      new MainTest().run(report);
    }
  }
}
```
