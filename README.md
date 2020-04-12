# SmartException ![GitHub release](https://img.shields.io/badge/release-v0.1.0-blue.svg) ![Bintray](https://img.shields.io/badge/bintray-v0.1.0-blue.svg) [![Build Status](https://travis-ci.org/tanersener/smart-exception.svg?branch=master)](https://travis-ci.org/tanersener/smart-exception)
Utilities to handle Throwable objects and stack trace elements

<img src="https://github.com/tanersener/smart-exception/blob/master/docs/assets/smart-exception-logo-v2.png" width="300">

### 1. Features
- Build shorter stack traces 
    - Start exception chain from a root package
    - Group stack trace elements from the same package
    - Ignore stack trace elements
    - Ignore causes
- Customise how a stack trace element will be printed
- Do not include module name while printing a stack trace element
- Define how many stack trace elements will be printed
- Define how native methods and closed source stack trace elements will be printed
- Search for a cause in an exception chain
- Get the cause of a throwable

### 2. Using

Binaries are available at [Github](https://github.com/tanersener/mobile-ffmpeg/releases) and [JCenter](https://bintray.com/bintray/jcenter).

1. Add SmartException dependency to your `build.gradle`.

    - Java 9 or later
    ```
    dependencies {
        implementation 'com.arthenica:smart-exception-java9:0.1.0'
    }
    ```

    - Java 7/8 or Android
    ```
    dependencies {
        implementation 'com.arthenica:smart-exception-java:0.1.0'
    }
    ```

2. Import `Exceptions` utility class.
    - Java 9 or later
    ```
    import com.arthenica.smartexception.java9.Exceptions;
    ```

    - Java 7/8 or Android
    ```
    import com.arthenica.smartexception.java.Exceptions;
    ```

3. Create shorter stack traces using `getStackTraceString` method. 

   ```
   Exceptions.getStackTraceString(e);
   ```  

   3.1 Use `root` packages. `root` package is the entry point in an exception chain. If you define a `root` package, 
   then all stack trace elements before that `root` package will be discarded, and you will get a cleaner, compact 
   stack trace. A `root` package can be defined in two different ways. 

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
           at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java)
           at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java)
           at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java)
           at org.springframework.mock.web.MockFilterChain.doFilter(MockFilterChain.java)
           at org.springframework.test.web.servlet.MockMvc.perform(MockMvc.java)
           at com.arthenica.smartexception.java.SpringTest.putUserTest(SpringTest.java)
       ```

    - Instead of this one.
   
       ```
       org.springframework.web.util.NestedServletException: Method not found.
           at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java)
           at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java)
           at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java)
           at org.springframework.mock.web.MockFilterChain.doFilter(MockFilterChain.java)
           at org.springframework.test.web.servlet.MockMvc.perform(MockMvc.java)
           at com.arthenica.smartexception.java.SpringTest.putUserTest(SpringTest.java)
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

   3.2 Use `group` packages. `group` packages can be used to group stack trace elements from the same package. A 
   `group` package can be defined in two different ways. 

    - By registering a global `group` package.

       ```
       Exceptions.registerGroupPackage("jdk.internal.reflect");
       Exceptions.registerGroupPackage("org.junit");
       Exceptions.registerGroupPackage("org.gradle");
       Exceptions.getStackTraceString(e);
       ```

    - By providing a `group` package while getting the stack trace.

       ```
       Exceptions.getStackTraceString(e, Collections.emptySet(), new HashSet<String>(Arrays.asList("org.junit", "jdk.internal.reflect", "org.gradle")), Collections.emptySet());
       ```

    - You will have the following stack trace string.
   
       ```
       org.springframework.web.util.NestedServletException: Method not found.
           at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java)
           at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java)
           at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java)
           at org.springframework.mock.web.MockFilterChain.doFilter(MockFilterChain.java)
           at org.springframework.test.web.servlet.MockMvc.perform(MockMvc.java)
           at com.arthenica.smartexception.java.SpringTest.putUserTest(SpringTest.java)           
           at jdk.internal.reflect ... 2 more
           at org.junit ... 3 more
           at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.run(SpringJUnit4ClassRunner.java)
           at org.gradle ... 4 more
           at jdk.internal.reflect ... 2 more
           at org.gradle ... 4 more
           at java.base/java.lang.Thread.run(Thread.java:844)
       ```

    - Instead of this one.
   
       ```
       org.springframework.web.util.NestedServletException: Method not found.
           at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java)
           at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java)
           at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java)
           at org.springframework.mock.web.MockFilterChain.doFilter(MockFilterChain.java)
           at org.springframework.test.web.servlet.MockMvc.perform(MockMvc.java)
           at com.arthenica.smartexception.java.SpringTest.putUserTest(SpringTest.java)
           at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
           at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
           at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
           at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:59)
           at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
           at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:56)
           at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
           at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.run(SpringJUnit4ClassRunner.java)
           at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.runTestClass(JUnitTestClassExecutor.java)
           at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.execute(JUnitTestClassExecutor.java)
           at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.execute(JUnitTestClassExecutor.java)
           at org.gradle.api.internal.tasks.testing.junit.AbstractJUnitTestClassProcessor.processTestClass(AbstractJUnitTestClassProcessor.java)
           at org.gradle.api.internal.tasks.testing.SuiteTestClassProcessor.processTestClass(SuiteTestClassProcessor.java)
           at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
           at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
           at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
           at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java)
           at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java)
           at org.gradle.internal.dispatch.ContextClassLoaderDispatch.dispatch(ContextClassLoaderDispatch.java)
           at org.gradle.internal.dispatch.ProxyDispatchAdapter$DispatchingInvocationHandler.invoke(ProxyDispatchAdapter
           at org.gradle.internal.concurrent.ThreadFactoryImpl$ManagedThreadRunnable.run(ThreadFactoryImpl.java:56)
           at java.base/java.lang.Thread.run(Thread.java:844)
       ```

### 3. Versions

|  SmartException Version | Release Date |
| :----: | :----: |
| [0.1.0](https://github.com/tanersener/smart-exception/releases/tag/v0.1.0) | April 12, 2020 |

### 4. Building

- Install Java 9 or later

- Run

```
./gradlew clean build
```

See the building status from the table below.

| branch | status |
| :---: | :---: |
|  master | [![Build Status](https://travis-ci.org/tanersener/smart-exception.svg?branch=master)](https://travis-ci.org/tanersener/smart-exception) |
|  development | [![Build Status](https://travis-ci.org/tanersener/smart-exception.svg?branch=development)](https://travis-ci.org/tanersener/smart-exception) |

### 5. Modules

`SmartException` source code is organised into three modules.

- `common` includes shared classes and interfaces 
- `java` has Java 7/8 and Android specific implementation
- `java9` has the implementation for Java 9 or later 

### 6. License

This project is licensed under the `BSD 3-Clause License`.

### 7. Contributing

Feel free to submit issues or pull requests. 

