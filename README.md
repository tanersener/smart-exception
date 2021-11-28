# SmartException ![GitHub release](https://img.shields.io/badge/release-v0.1.1-blue.svg) ![Maven Central](https://img.shields.io/maven-central/v/com.arthenica/smart-exception-java) [![Build Status](https://travis-ci.org/tanersener/smart-exception.svg?branch=master)](https://travis-ci.org/tanersener/smart-exception)

Utilities to handle Throwable objects and stack trace elements

<img src="https://github.com/tanersener/smart-exception/blob/development/docs/assets/smart-exception-logo-v5.png" width="240">

### 1. Features

- Build shorter stack traces
    - Start exception chain from a root package
    - Group stack trace elements from the same package
    - Ignore stack trace elements
    - Ignore causes
    - Define how many stack trace elements will be printed
- Do not include module name while printing a stack trace element
- Customise how a stack trace element will be printed
- Define how native methods and closed source stack trace elements will be printed
- Search for a cause in an exception chain
- Get the cause of a throwable
- Print package information (jar file and version)

### 2. Using

Binaries are available at [Github](https://github.com/tanersener/smart-exception/releases)
and [Maven Central](https://repo1.maven.org/maven2).

#### 2.1. Gradle

Add SmartException dependency to your `build.gradle`.

- Java 9 or later

```
dependencies {
    implementation 'com.arthenica:smart-exception-java9:0.1.1'
}
```

- Java 7/8 or Android

```
dependencies {
    implementation 'com.arthenica:smart-exception-java:0.1.1'
}
```

#### 2.2. Manual

You can import SmartException jars into your IDE manually. Remember that both `smart-exception-java9` and
`smart-exception-java` jars depend on `smart-exception-common`. So, do not forget to import `smart-exception-common`
too.

#### 2.3. Import Exceptions class

Import `Exceptions` class, which contains all utility methods, from the correct package.

- Java 9 or later

```
import com.arthenica.smartexception.java9.Exceptions;
```

- Java 7/8 or Android

```
import com.arthenica.smartexception.java.Exceptions;
```

#### 2.4. Create shorter stack traces

Use `getStackTraceString` method to create a shorter stack trace. By default, `getStackTraceString` will generate the
same long stack trace as a string for you. You need to define some rules for `getStackTraceString` to use. Those rules
define how the exception stack trace is processed and shortened.

```
Exceptions.getStackTraceString(e);
```  

##### 2.4.1 Use `root` packages.

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

##### 2.4.2 Use `group` packages.

`group` packages can be used to group stack trace elements from the same package into a single line. A `group` package
can be defined in two different ways.

- By registering global `group` packages.

```
Exceptions.registerGroupPackage("jdk.internal.reflect");
Exceptions.registerGroupPackage("org.junit");
Exceptions.registerGroupPackage("org.gradle");
Exceptions.getStackTraceString(e);
```

- By providing `group` packages while getting the stack trace.

```
Exceptions.getStackTraceString(e, Collections.emptySet(), new HashSet<String>(Arrays.asList("org.junit", "jdk.internal.reflect", "org.gradle")), Collections.emptySet());
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
    at jdk.internal.reflect ... 2 more
    at org.junit ... 3 more
    at org.gradle ... 4 more
    at jdk.internal.reflect ... 2 more
    at org.gradle ... 4 more
    at java.base/java.lang.Thread.run(Thread.java:844)
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
    at jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    at jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:59)
    at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
    at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:56)
    at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
    at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.runTestClass(JUnitTestClassExecutor.java:110)
    at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.execute(JUnitTestClassExecutor.java:58)
    at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.execute(JUnitTestClassExecutor.java:38)
    at org.gradle.api.internal.tasks.testing.junit.AbstractJUnitTestClassProcessor.processTestClass(AbstractJUnitTestClassProcessor.java:62)
    at org.gradle.api.internal.tasks.testing.SuiteTestClassProcessor.processTestClass(SuiteTestClassProcessor.java:51)
    at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:24)
    at org.gradle.internal.dispatch.ContextClassLoaderDispatch.dispatch(ContextClassLoaderDispatch.java:33)
    at org.gradle.internal.dispatch.ProxyDispatchAdapter$DispatchingInvocationHandler.invoke(ProxyDispatchAdapter.java:94)
    at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:24)
    at org.gradle.internal.concurrent.ThreadFactoryImpl$ManagedThreadRunnable.run(ThreadFactoryImpl.java:56)
    at java.base/java.lang.Thread.run(Thread.java:844)
```

##### 2.4.3 Use `ignore` packages.

`ignore` packages represent stack trace elements that you do not want to see in your exception chain. An `ignore`
package can be defined in two different ways.

- By registering global `ignore` packages.

```
Exceptions.registerIgnorePackage("jdk.internal.reflect", false);
Exceptions.registerIgnorePackage("org.junit", false);
Exceptions.registerIgnorePackage("org.gradle", false);
Exceptions.getStackTraceString(e);
```

- By providing `ignore` packages while getting the stack trace.

```
Exceptions.getStackTraceString(e, Collections.emptySet(), Collections.emptySet(), new HashSet<String>(Arrays.asList("org.junit", "jdk.internal.reflect", "org.gradle")));
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
    at java.base/java.lang.Thread.run(Thread.java:844)
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
    at jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    at jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:59)
    at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
    at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:56)
    at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
    at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.runTestClass(JUnitTestClassExecutor.java:110)
    at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.execute(JUnitTestClassExecutor.java:58)
    at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.execute(JUnitTestClassExecutor.java:38)
    at org.gradle.api.internal.tasks.testing.junit.AbstractJUnitTestClassProcessor.processTestClass(AbstractJUnitTestClassProcessor.java:62)
    at org.gradle.api.internal.tasks.testing.SuiteTestClassProcessor.processTestClass(SuiteTestClassProcessor.java:51)
    at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)	
    at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:24)
    at org.gradle.internal.dispatch.ContextClassLoaderDispatch.dispatch(ContextClassLoaderDispatch.java:33)
    at org.gradle.internal.dispatch.ProxyDispatchAdapter$DispatchingInvocationHandler.invoke(ProxyDispatchAdapter.java:94)
    at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:24)
    at org.gradle.internal.concurrent.ThreadFactoryImpl$ManagedThreadRunnable.run(ThreadFactoryImpl.java:56)
    at java.base/java.lang.Thread.run(Thread.java:844)
```

##### 2.4.4 Ignore Causes

- You can choose to drop causes from exception chain by ignoring all causes globally.

```
Exceptions.setIgnoreAllCauses(true);
```

- Or you can specify that you want all causes ignored while getting the stack trace.

```
Exceptions.getStackTraceString(e, true);
```

- You will have the following stack trace string.

```
javax.management.MBeanException
    at com.arthenica.smartexception.java.ExceptionsTest.getStackTraceWithMaxDepth(ExceptionsTest.java:287)
    at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
```

- Instead of this one.

```
javax.management.MBeanException
    at com.arthenica.smartexception.java.ExceptionsTest.getStackTraceWithMaxDepth(ExceptionsTest.java:287)
    at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
Caused by: java.lang.IllegalStateException: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.
    at com.arthenica.smartexception.java.ExceptionsTest.getStackTraceWithMaxDepth(ExceptionsTest.java:286)
    at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
Caused by: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.
    at com.arthenica.smartexception.java.ExceptionsTest.getStackTraceWithMaxDepth(ExceptionsTest.java:285)
    at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
```

##### 2.4.5 Define max number of stack trace elements that will be printed

- Provide max depth while getting the stack trace.

```
Exceptions.getStackTraceString(e, 5);
```

- You will have the following stack trace string.

```
org.springframework.web.util.NestedServletException: Method not found.
    at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1014)
    at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:909)
    at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)
    at org.springframework.mock.web.MockFilterChain.doFilter(MockFilterChain.java:134)
    at org.springframework.test.web.servlet.MockMvc.perform(MockMvc.java:183)
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
    at jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    at jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:59)
    at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
    at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:56)
    at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
    at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.runTestClass(JUnitTestClassExecutor.java:110)
    at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.execute(JUnitTestClassExecutor.java:58)
    at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.execute(JUnitTestClassExecutor.java:38)
    at org.gradle.api.internal.tasks.testing.junit.AbstractJUnitTestClassProcessor.processTestClass(AbstractJUnitTestClassProcessor.java:62)
    at org.gradle.api.internal.tasks.testing.SuiteTestClassProcessor.processTestClass(SuiteTestClassProcessor.java:51)
    at java.base/java.lang.Thread.run(Thread.java:844)
```

##### 2.4.6 Print package information

Package information is the jar name and/or jar version of the class that is printed in a stack trace. Generating it is
expensive, so it is disabled by default. You can enable printing it in two ways.

- By globally enabling it for all `getStackTraceString` calls.

```
Exceptions.setPrintPackageInformation(true);
```

- By providing `printPackageInformation` option while getting the stack trace.

```
Exceptions.getStackTraceString(e, Collections.singleton("com.arthenica"), Collections.singleton("org.apache.cxf"), new HashSet<String>(Arrays.asList("org.junit", "jdk.internal.reflect", "org.gradle", "java.net", "sun.net")), false, true);
```

- You will have the following stack trace string.

```
javax.ws.rs.ProcessingException: java.net.ConnectException: ConnectException invoking http://localhost:12345/rs/service?param1=value1: Connection refused (Connection refused)
	at org.apache.cxf ... 10 more [cxf-rt-rs-client-3.3.6.jar]
	at com.arthenica.smartexception.java.ApacheCxfTest.accessAndPrintPackageInformation(ApacheCxfTest.java:88)
Caused by: java.net.ConnectException: ConnectException invoking http://localhost:12345/rs/service?param1=value1: Connection refused (Connection refused)
	at java.lang.reflect.Constructor.newInstance(Constructor.java:488)
	at org.apache.cxf ... 15 more [cxf-rt-transports-http-3.3.6.jar]
	at com.arthenica.smartexception.java.ApacheCxfTest.accessAndPrintPackageInformation(ApacheCxfTest.java:88)
Caused by: java.net.ConnectException: Connection refused (Connection refused)
	at org.apache.cxf ... 1 more [cxf-rt-transports-http-3.3.6.jar]
	at java.security.AccessController.doPrivileged(Native Method)
	at org.apache.cxf ... 18 more [cxf-rt-transports-http-3.3.6.jar]
	at com.arthenica.smartexception.java.ApacheCxfTest.accessAndPrintPackageInformation(ApacheCxfTest.java:88)
```

- Instead of this one.

```
javax.ws.rs.ProcessingException: java.net.ConnectException: ConnectException invoking http://localhost:12345/rs/service?param1=value1: Connection refused (Connection refused)
	at org.apache.cxf.jaxrs.client.AbstractClient.checkClientException(AbstractClient.java:629)
	at org.apache.cxf.jaxrs.client.AbstractClient.preProcessResult(AbstractClient.java:605)
	at org.apache.cxf.jaxrs.client.WebClient.doResponse(WebClient.java:1150)
	at org.apache.cxf.jaxrs.client.WebClient.doChainedInvocation(WebClient.java:1087)
	at org.apache.cxf.jaxrs.client.WebClient.doInvoke(WebClient.java:932)
	at org.apache.cxf.jaxrs.client.WebClient.doInvoke(WebClient.java:901)
	at org.apache.cxf.jaxrs.client.WebClient.invoke(WebClient.java:461)
	at org.apache.cxf.jaxrs.client.SyncInvokerImpl.method(SyncInvokerImpl.java:135)
	at org.apache.cxf.jaxrs.client.SyncInvokerImpl.method(SyncInvokerImpl.java:130)
	at org.apache.cxf.jaxrs.client.SyncInvokerImpl.get(SyncInvokerImpl.java:50)
	at org.apache.cxf.jaxrs.client.spec.InvocationBuilderImpl.get(InvocationBuilderImpl.java:88)
	at com.arthenica.smartexception.java.ApacheCxfTest.accessAndPrintPackageInformation(ApacheCxfTest.java:88)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:564)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:59)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:56)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.junit.runners.ParentRunner$3.evaluate(ParentRunner.java:306)
	at org.junit.runners.BlockJUnit4ClassRunner$1.evaluate(BlockJUnit4ClassRunner.java:100)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:366)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:103)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:63)
	at org.junit.runners.ParentRunner$4.run(ParentRunner.java:331)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:79)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:329)
	at org.junit.runners.ParentRunner.access$100(ParentRunner.java:66)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:293)
	at org.junit.runners.ParentRunner$3.evaluate(ParentRunner.java:306)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:413)
	at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.runTestClass(JUnitTestClassExecutor.java:110)
	at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.execute(JUnitTestClassExecutor.java:58)
	at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.execute(JUnitTestClassExecutor.java:38)
	at org.gradle.api.internal.tasks.testing.junit.AbstractJUnitTestClassProcessor.processTestClass(AbstractJUnitTestClassProcessor.java:62)
	at org.gradle.api.internal.tasks.testing.SuiteTestClassProcessor.processTestClass(SuiteTestClassProcessor.java:51)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:564)
	at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:36)
	at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:24)
	at org.gradle.internal.dispatch.ContextClassLoaderDispatch.dispatch(ContextClassLoaderDispatch.java:33)
	at org.gradle.internal.dispatch.ProxyDispatchAdapter$DispatchingInvocationHandler.invoke(ProxyDispatchAdapter.java:94)
	at com.sun.proxy.$Proxy2.processTestClass(Unknown Source)
	at org.gradle.api.internal.tasks.testing.worker.TestWorker.processTestClass(TestWorker.java:118)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:564)
	at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:36)
	at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:24)
	at org.gradle.internal.remote.internal.hub.MessageHubBackedObjectConnection$DispatchWrapper.dispatch(MessageHubBackedObjectConnection.java:182)
	at org.gradle.internal.remote.internal.hub.MessageHubBackedObjectConnection$DispatchWrapper.dispatch(MessageHubBackedObjectConnection.java:164)
	at org.gradle.internal.remote.internal.hub.MessageHub$Handler.run(MessageHub.java:412)
	at org.gradle.internal.concurrent.ExecutorPolicy$CatchAndRecordFailures.onExecute(ExecutorPolicy.java:64)
	at org.gradle.internal.concurrent.ManagedExecutorImpl$1.run(ManagedExecutorImpl.java:48)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
	at org.gradle.internal.concurrent.ThreadFactoryImpl$ManagedThreadRunnable.run(ThreadFactoryImpl.java:56)
	at java.base/java.lang.Thread.run(Thread.java:844)
Caused by: java.net.ConnectException: ConnectException invoking http://localhost:12345/rs/service?param1=value1: Connection refused (Connection refused)
	at java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
	at java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)
	at java.base/jdk.internal.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
	at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:488)
	at org.apache.cxf.transport.http.HTTPConduit$WrappedOutputStream.mapException(HTTPConduit.java:1400)
	at org.apache.cxf.transport.http.HTTPConduit$WrappedOutputStream.close(HTTPConduit.java:1384)
	at org.apache.cxf.io.AbstractWrappedOutputStream.close(AbstractWrappedOutputStream.java:77)
	at org.apache.cxf.transport.AbstractConduit.close(AbstractConduit.java:56)
	at org.apache.cxf.transport.http.HTTPConduit.close(HTTPConduit.java:671)
	at org.apache.cxf.interceptor.MessageSenderInterceptor$MessageSenderEndingInterceptor.handleMessage(MessageSenderInterceptor.java:63)
	at org.apache.cxf.phase.PhaseInterceptorChain.doIntercept(PhaseInterceptorChain.java:308)
	at org.apache.cxf.jaxrs.client.AbstractClient.doRunInterceptorChain(AbstractClient.java:701)
	at org.apache.cxf.jaxrs.client.WebClient.doChainedInvocation(WebClient.java:1086)
	... 58 more
Caused by: java.net.ConnectException: Connection refused (Connection refused)
	at java.base/java.net.PlainSocketImpl.socketConnect(Native Method)
	at java.base/java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:400)
	at java.base/java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:243)
	at java.base/java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:225)
	at java.base/java.net.Socket.connect(Socket.java:591)
	at java.base/sun.net.NetworkClient.doConnect(NetworkClient.java:177)
	at java.base/sun.net.www.http.HttpClient.openServer(HttpClient.java:474)
	at java.base/sun.net.www.http.HttpClient.openServer(HttpClient.java:569)
	at java.base/sun.net.www.http.HttpClient.<init>(HttpClient.java:242)
	at java.base/sun.net.www.http.HttpClient.New(HttpClient.java:341)
	at java.base/sun.net.www.http.HttpClient.New(HttpClient.java:362)
	at java.base/sun.net.www.protocol.http.HttpURLConnection.getNewHttpClient(HttpURLConnection.java:1224)
	at java.base/sun.net.www.protocol.http.HttpURLConnection.plainConnect0(HttpURLConnection.java:1203)
	at java.base/sun.net.www.protocol.http.HttpURLConnection.plainConnect(HttpURLConnection.java:1057)
	at java.base/sun.net.www.protocol.http.HttpURLConnection.connect(HttpURLConnection.java:991)
	at java.base/sun.net.www.protocol.http.HttpURLConnection.getInputStream0(HttpURLConnection.java:1563)
	at java.base/sun.net.www.protocol.http.HttpURLConnection.getInputStream(HttpURLConnection.java:1491)
	at java.base/java.net.HttpURLConnection.getResponseCode(HttpURLConnection.java:527)
	at org.apache.cxf.transport.http.URLConnectionHTTPConduit$URLConnectionWrappedOutputStream$2.run(URLConnectionHTTPConduit.java:377)
	at org.apache.cxf.transport.http.URLConnectionHTTPConduit$URLConnectionWrappedOutputStream$2.run(URLConnectionHTTPConduit.java:373)
	at java.base/java.security.AccessController.doPrivileged(Native Method)
	at org.apache.cxf.transport.http.URLConnectionHTTPConduit$URLConnectionWrappedOutputStream.getResponseCode(URLConnectionHTTPConduit.java:373)
	at org.apache.cxf.transport.http.HTTPConduit$WrappedOutputStream.doProcessResponseCode(HTTPConduit.java:1597)
	at org.apache.cxf.transport.http.HTTPConduit$WrappedOutputStream.handleResponseInternal(HTTPConduit.java:1625)
	at org.apache.cxf.transport.http.HTTPConduit$WrappedOutputStream.handleResponse(HTTPConduit.java:1570)
	at org.apache.cxf.transport.http.HTTPConduit$WrappedOutputStream.close(HTTPConduit.java:1371)
	... 65 more
```

#### 2.5. Customise how a stack trace element will be printed

`com.arthenica.smartexception.StackTraceElementSerializer` interface includes several methods that you can use to
customise the stack trace string. Implement this interface and register your implementation using the following method.

```
Exceptions.setStackTraceElementSerializer(myImplementation);
```

- `String toString(final StackTraceElement stackTraceElement)` method in this interface defines how single stack trace
  element will be printed.

For example, you can use the following method to print only the file name and the line number.

```
public String toString(StackTraceElement stackTraceElement) {
    return stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber();
}
```

You will have the following stack trace string.

```
java.lang.NumberFormatException: For input string: "ABC"
    at NumberFormatException.java:65
    at Integer.java:652
    at Integer.java:770
    at ExceptionsTest.java:266
    at NativeMethodAccessorImpl.java:-2
    at NativeMethodAccessorImpl.java:62
    at DelegatingMethodAccessorImpl.java:43
    at Method.java:564
    at FrameworkMethod.java:59
```

- `String getNativeMethodDefinition()` method defines the text used for native methods. Java uses
  `(Native Method)` text by default.

- `String getUnknownSourceDefinition()` method specifies the text used for stack trace elements without a name and line
  number. `(Unknown Source)` is used by default.

#### 2.6. Search for a cause

Search for a cause by providing the exception type and max depth.

```
Exceptions.searchCause(e, ArrayIndexOutOfBoundsException.class, 2);
```

#### 2.7. Get the cause of a throwable

```
Exceptions.getCause(e);
```

#### 2.8. Check if an exception is found in the exception chain

```
if (Exceptions.containsCause(e, IllegalArgumentException.class))
```

### 4. Versions

|  SmartException Version | Release Date |
| :----: | :----: |
| [0.1.1](https://github.com/tanersener/smart-exception/releases/tag/v0.1.1) | Nov 28, 2021 |
| [0.1.0](https://github.com/tanersener/smart-exception/releases/tag/v0.1.0) | Apr 12, 2020 |

### 5. Building

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

### 6. Modules

`SmartException` source code is organised into three modules.

- `common` includes shared classes and interfaces
- `java` has Java 7/8 and Android specific implementation
- `java9` has the implementation for Java 9 or later
- `test` includes test classes that use library jars published in `mavenCentral()`

### 7. License

This project is licensed under the `BSD 3-Clause License`.

### 8. Contributing

Feel free to submit issues or pull requests. 

