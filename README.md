# SmartException ![GitHub release](https://img.shields.io/badge/release-v0.2.1-blue.svg) ![Maven Central](https://img.shields.io/maven-central/v/com.arthenica/smart-exception-java) ![build status](https://github.com/tanersener/smart-exception/actions/workflows/build.yml/badge.svg)

Utilities to handle throwable objects and format stack trace elements in `JVM` based languages (`Java`, `Scala`, `Kotlin`, `Groovy`, etc.)

<img src="https://github.com/tanersener/smart-exception/blob/main/docs/assets/smart-exception-logo-v5.png" width="240" alt="Smart Exception Logo">

### 1. Features

- Build shorter stack traces
    - Start exception chain from a root package
    - Group stack trace elements from the same package
    - Ignore stack trace elements
    - Ignore causes
    - Define how many stack trace elements will be printed
- Use `SmartExceptionConverter` in `logback` to generate shorter stack traces without a code change
  - Create conversion rules to use within pattern layout 
  - Define a `throwableConverter` to format stack traces in `logstash-logback-encoder`  
- Do not include module name while printing a stack trace element
- Do not print suppressed exceptions
- Customise how a stack trace element will be printed
- Define how native methods and closed source stack trace elements will be printed
- Search for a cause in an exception chain
- Get the cause of a throwable
- Print package information (jar file and version)

### 2. Using

Binaries are available at [Github](https://github.com/tanersener/smart-exception/releases)
and [Maven Central](https://repo1.maven.org/maven2).

#### 2.1. Java API

Use `Java API` to access all features of the library. See 
[Java API](https://github.com/tanersener/smart-exception/wiki/Java-API) wiki page for the complete list of API methods.

Install `smart-exception` by adding one of the following dependencies to your `build.gradle`.

- Java 9 or later

```
dependencies {
    implementation 'com.arthenica:smart-exception-java9:0.2.1'
}
```

- Java 7/8 or Android

```
dependencies {
    implementation 'com.arthenica:smart-exception-java:0.2.1'
}
```

##### 2.1.1 Import Exceptions class

Import `Exceptions` class, which contains all utility methods, from the correct package.

- Java 9 or later

```
import com.arthenica.smartexception.java9.Exceptions;
```

- Java 7/8 or Android

```
import com.arthenica.smartexception.java.Exceptions;
```

##### 2.1.2 Create shorter stack traces

Use `getStackTraceString` method to create a shorter stack trace. By default, `getStackTraceString` will generate the
same long stack trace as a string for you. You need to define some rules for `getStackTraceString` to use. Those rules
define how the exception stack trace is processed and shortened.

```
Exceptions.getStackTraceString(e);
```  

##### 2.1.3 Use `root` packages.

`root` package is the entry point in an exception chain. If you define a `root` package, then all stack trace elements
before that `root` package will be discarded, and you will get a cleaner, compact stack trace. A `root` package can be
defined in two different ways.

- By registering a global `root` package.

```
Exceptions.registerRootPackage("com.arthenica");
Exceptions.getStackTraceString(e);
```

- By providing a `root` package while getting the stack trace.

```
Exceptions.getStackTraceString(e, "com.arthenica");
```

- You will have the following stack trace string.

```
org.springframework.web.util.NestedServletException: Method not found.
    at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1014)
    at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:909)
    at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)
    at org.springframework.mock.web.MockFilterChain.doFilter(MockFilterChain.java:134)
    at org.springframework.test.web.servlet.MockMvc.perform(MockMvc.java:183)
    at com.arthenica.smartexception.java.SpringTest.putUserTest(SpringTest.java:100)
```

- Instead of this one.

```
org.springframework.web.util.NestedServletException: Method not found.
    at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1014)
    at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:909)
    at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)
    at org.springframework.mock.web.MockFilterChain.doFilter(MockFilterChain.java:134)
    at org.springframework.test.web.servlet.MockMvc.perform(MockMvc.java:183)
    at com.arthenica.smartexception.java.SpringTest.putUserTest(SpringTest.java:100)
    at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at java.base/java.lang.reflect.Method.invoke(Method.java:564)
    at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:59)
    at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
    at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:56)
    at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
    ... 35 more
```

#### 2.2. Logback converters

Install `smart-exception-logback` by adding the following dependency to your `build.gradle`.

Note that `smart-exception-logback` requires Java 9 or later

```
dependencies {
    implementation 'com.arthenica:smart-exception-logback:0.2.1'
}
```

- Create a conversion rule to use `SmartExceptionConverter` in pattern layout

```xml
<conversionRule conversionWord="smartEx" converterClass="com.arthenica.smartexception.logback.SmartExceptionConverter"/>

<appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
        <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level %logger - %msg %smartEx{'rootPackage=com.arthenica','printModuleName=false','ignorePackage=jdk.internal','printPackageInformation=true'}%n</pattern>
    </encoder>
</appender>
```

- Define a `throwableConverter` to format stack traces in `logstash-logback-encoder`

```xml
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="net.logstash.logback.encoder.LogstashEncoder">
            <throwableConverter class="com.arthenica.smartexception.logback.SmartExceptionConverter">
                <printModuleName>false</printModuleName>
                <printPackageInformation>true</printPackageInformation>
                <rootPackage>com.arthenica</rootPackage>
                <groupPackage>org.springframework</groupPackage>
                <ignorePackage>jdk.internal</ignorePackage>
            </throwableConverter>
        </encoder>
    </appender>
```

### 3. Versions

|                           SmartException Version                           | Release Date |
|:--------------------------------------------------------------------------:|:------------:|
| [0.2.1](https://github.com/tanersener/smart-exception/releases/tag/v0.2.1) | May 30, 2022 |
| [0.2.0](https://github.com/tanersener/smart-exception/releases/tag/v0.2.0) | Mar 13, 2022 |
| [0.1.1](https://github.com/tanersener/smart-exception/releases/tag/v0.1.1) | Nov 28, 2021 |
| [0.1.0](https://github.com/tanersener/smart-exception/releases/tag/v0.1.0) | Apr 12, 2020 |

### 4. Building

- Install Java 9 or later

- Run

```
./gradlew clean build
```

See the build status from the table below.

|   branch    | status |
|:-----------:| :---: |
|    main     | ![build status](https://github.com/tanersener/smart-exception/actions/workflows/build.yml/badge.svg) |
| development | ![build status](https://github.com/tanersener/smart-exception/actions/workflows/build.yml/badge.svg?branch=development) |

### 5. Wiki

A more detailed documentation is available at [Wiki](https://github.com/tanersener/smart-exception/wiki).

### 6. Modules

`SmartException` source code is organised into five modules.

- `common` includes shared classes and interfaces
- `java` has Java 7/8 and Android specific implementation
- `java9` has the implementation for Java 9 or later
- `logback` includes `logback` converter implementation
- `test` includes test classes that use library jars published in `mavenCentral()`

### 7. License

This project is licensed under the `BSD 3-Clause License`.

### 8. Contributing

Feel free to submit issues or pull requests. 

