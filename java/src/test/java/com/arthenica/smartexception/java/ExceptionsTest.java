/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020-2022, Taner Sener
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.arthenica.smartexception.java;

import com.arthenica.smartexception.AbstractExceptions;
import com.arthenica.smartexception.ThrowableWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

import javax.management.MBeanException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.DigestException;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.concurrent.Callable;

public class ExceptionsTest {

    static String trimDynamicParts(final String stackTraceLine) {
        // TRIM LINE NUMBER - ADDING NEW TEST CASES CAUSES EXISTING TESTS TO FAIL
        // TRIM MODULE NAME - DEPENDING ON THE CLASS LOADER MODULE NAME CAN BE PRINTED OR NOT
        return stackTraceLine.replaceAll(":[0-9]*\\)", "\\)").replaceAll("at .*/", "at ");
    }

    @Test
    public void getStackTrace() {
        Exception level4Exception = new ArrayIndexOutOfBoundsException("Index not valid.");
        Exception level3Exception = new ConcurrentModificationException(level4Exception);
        Exception level2Exception = new IllegalStateException(level3Exception);
        final Exception level1Exception = new MBeanException(level2Exception);

        try {
            Exceptions.registerRootPackage("com.arthenica");
            Exceptions.registerIgnorePackage("java.util", false);

            Callable<String> stringCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    throw level1Exception;
                }
            };
            stringCallable.call();
        } catch (Exception e) {
            String expectedStackTrace = "javax.management.MBeanException\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTrace(ExceptionsTest.java:54)\n" + "Caused by: java.lang.IllegalStateException: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTrace(ExceptionsTest.java:53)\n" + "Caused by: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTrace(ExceptionsTest.java:52)\n" + "Caused by: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTrace(ExceptionsTest.java:51)";

            Assert.assertEquals(trimDynamicParts(expectedStackTrace), trimDynamicParts(Exceptions.getStackTraceString(e)));
        } finally {
            Exceptions.clearRootPackages();
            Exceptions.clearIgnorePackages();
        }

        try {
            Exceptions.registerRootPackage("com.arthenica");
            Exceptions.registerIgnorePackage("java.util", true);

            Callable<String> stringCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    throw level1Exception;
                }
            };
            stringCallable.call();
        } catch (Exception e) {
            String expectedStackTrace = "javax.management.MBeanException\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTrace(ExceptionsTest.java:54)\n" + "Caused by: java.lang.IllegalStateException: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTrace(ExceptionsTest.java:53)\n" + "Caused by: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTrace(ExceptionsTest.java:52)";

            Assert.assertEquals(trimDynamicParts(expectedStackTrace), trimDynamicParts(Exceptions.getStackTraceString(e)));

        } finally {
            Exceptions.clearRootPackages();
            Exceptions.clearIgnorePackages();
        }

        try {
            Exceptions.registerRootPackage("com.arthenica");
            Exceptions.registerIgnorePackage("java.util", true);

            Callable<String> stringCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    throw level1Exception;
                }
            };
            stringCallable.call();
        } catch (Exception e) {
            String expectedStackTrace = "javax.management.MBeanException\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTrace(ExceptionsTest.java:54)";

            Assert.assertEquals(trimDynamicParts(expectedStackTrace), trimDynamicParts(Exceptions.getStackTraceString(e, true)));
        } finally {
            Exceptions.clearRootPackages();
            Exceptions.clearIgnorePackages();
        }
    }

    @Test
    public void getStackTraceWithPackages() {
        Exception level4Exception = new ArrayIndexOutOfBoundsException("Index not valid.");
        Exception level3Exception = new ConcurrentModificationException(level4Exception);
        Exception level2Exception = new IllegalStateException(level3Exception);
        final Exception level1Exception = new MBeanException(level2Exception);

        try {
            Callable<String> stringCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    throw level1Exception;
                }
            };
            stringCallable.call();
        } catch (Exception e) {
            String expectedStackTrace = "javax.management.MBeanException\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTraceWithPackages(ExceptionsTest.java:129)\n" + "Caused by: java.lang.IllegalStateException: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTraceWithPackages(ExceptionsTest.java:128)\n" + "Caused by: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTraceWithPackages(ExceptionsTest.java:127)\n" + "Caused by: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTraceWithPackages(ExceptionsTest.java:126)";

            Assert.assertEquals(trimDynamicParts(expectedStackTrace), trimDynamicParts(Exceptions.getStackTraceString(e, Collections.singleton("com.arthenica"), new HashSet<String>(), new HashSet<String>())));
        }

        try {
            Callable<String> stringCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    throw level1Exception;
                }
            };
            stringCallable.call();
        } catch (Exception e) {
            String expectedStackTrace = "javax.management.MBeanException\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTraceWithPackages(ExceptionsTest.java:129)\n" + "Caused by: java.lang.IllegalStateException: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTraceWithPackages(ExceptionsTest.java:128)\n" + "Caused by: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTraceWithPackages(ExceptionsTest.java:127)\n" + "Caused by: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTraceWithPackages(ExceptionsTest.java:126)";

            Assert.assertEquals(trimDynamicParts(expectedStackTrace), trimDynamicParts(Exceptions.getStackTraceString(e, Collections.singleton("com.arthenica"), new HashSet<String>(), new HashSet<String>())));
        }
    }

    @Test
    public void getStackTraceWithRootPackage() {
        Exception level4Exception = new ArrayIndexOutOfBoundsException("Index not valid.");
        Exception level3Exception = new ConcurrentModificationException(level4Exception);
        Exception level2Exception = new IllegalStateException(level3Exception);
        final Exception level1Exception = new MBeanException(level2Exception);

        try {
            Callable<String> stringCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    throw level1Exception;
                }
            };
            stringCallable.call();
        } catch (Exception e) {
            String expectedStackTrace = "javax.management.MBeanException\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTraceWithRootPackage(ExceptionsTest.java:175)\n" + "Caused by: java.lang.IllegalStateException: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTraceWithRootPackage(ExceptionsTest.java:174)\n" + "Caused by: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTraceWithRootPackage(ExceptionsTest.java:173)\n" + "Caused by: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTraceWithRootPackage(ExceptionsTest.java:172)";

            Assert.assertEquals(trimDynamicParts(expectedStackTrace), trimDynamicParts(Exceptions.getStackTraceString(e, "com.arthenica")));
        }
    }

    @Test
    public void getStackTraceWithRootPackageAndGroupPackage() {
        try {
            Integer.parseInt("ABC");
        } catch (NumberFormatException e) {
            String expectedStackTrace = "java.lang.NumberFormatException: For input string: \"ABC\"\n" + "\tat java.base/java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)\n" + "\tat java.base/java.lang.Integer.parseInt(Integer.java:652)\n" + "\tat java.base/java.lang.Integer.parseInt(Integer.java:770)\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTraceWithRootPackageAndGroupPackage(ExceptionsTest.java:200)";

            Assert.assertEquals(trimDynamicParts(expectedStackTrace), trimDynamicParts(Exceptions.getStackTraceString(e, "com.arthenica", "org.gradle")));
        }

        try {
            Exceptions.registerGroupPackage("org.junit");
            Exceptions.registerGroupPackage("sun.reflect");
            Exceptions.registerGroupPackage("org.gradle");

            Integer.parseInt("ABC");
        } catch (NumberFormatException e) {
            String expectedStackTrace = "java.lang.NumberFormatException: For input string: \"ABC\"\n" + "\tat java.base/java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)\n" + "\tat java.base/java.lang.Integer.parseInt(Integer.java:652)\n" + "\tat java.base/java.lang.Integer.parseInt(Integer.java:770)\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTraceWithRootPackageAndGroupPackage(ExceptionsTest.java:217)\n" + "\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" + "\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n" + "\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n" + "\tat java.base/java.lang.reflect.Method.invoke(Method.java:564)\n" + "\tat org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:59)\n" + "\tat org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)\n" + "\tat org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:56)\n" + "\tat org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)\n" + "\tat org.junit.runners.ParentRunner$3.evaluate(ParentRunner.java:306)\n" + "\tat org.junit.runners.BlockJUnit4ClassRunner$1.evaluate(BlockJUnit4ClassRunner.java:100)\n" + "\tat org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:366)\n" + "\tat org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:103)\n" + "\tat org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:63)\n" + "\tat org.junit.runners.ParentRunner$4.run(ParentRunner.java:331)\n" + "\tat org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:79)\n" + "\tat org.junit.runners.ParentRunner.runChildren(ParentRunner.java:329)\n" + "\tat org.junit.runners.ParentRunner.access$100(ParentRunner.java:66)\n" + "\tat org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:293)\n" + "\tat org.junit.runners.ParentRunner$3.evaluate(ParentRunner.java:306)\n" + "\tat org.junit.runners.ParentRunner.run(ParentRunner.java:413)\n" + "\tat org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.runTestClass(JUnitTestClassExecutor.java:110)\n" + "\tat org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.execute(JUnitTestClassExecutor.java:58)\n" + "\tat org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.execute(JUnitTestClassExecutor.java:38)\n" + "\tat org.gradle.api.internal.tasks.testing.junit.AbstractJUnitTestClassProcessor.processTestClass(AbstractJUnitTestClassProcessor.java:62)\n" + "\tat org.gradle.api.internal.tasks.testing.SuiteTestClassProcessor.processTestClass(SuiteTestClassProcessor.java:51)\n" + "\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" + "\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n" + "\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n" + "\tat java.base/java.lang.reflect.Method.invoke(Method.java:564)\n" + "\tat org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:36)\n" + "\tat org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:24)\n" + "\tat org.gradle.internal.dispatch.ContextClassLoaderDispatch.dispatch(ContextClassLoaderDispatch.java:33)\n" + "\tat org.gradle.internal.dispatch.ProxyDispatchAdapter$DispatchingInvocationHandler.invoke(ProxyDispatchAdapter.java:94)\n" + "\tat com.sun.proxy.$Proxy2.processTestClass(Unknown Source)\n" + "\tat org.gradle.api.internal.tasks.testing.worker.TestWorker.processTestClass(TestWorker.java:118)\n" + "\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" + "\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n" + "\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n" + "\tat java.base/java.lang.reflect.Method.invoke(Method.java:564)\n" + "\tat org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:36)\n" + "\tat org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:24)\n" + "\tat org.gradle.internal.remote.internal.hub.MessageHubBackedObjectConnection$DispatchWrapper.dispatch(MessageHubBackedObjectConnection.java:182)\n" + "\tat org.gradle.internal.remote.internal.hub.MessageHubBackedObjectConnection$DispatchWrapper.dispatch(MessageHubBackedObjectConnection.java:164)\n" + "\tat org.gradle.internal.remote.internal.hub.MessageHub$Handler.run(MessageHub.java:412)\n" + "\tat org.gradle.internal.concurrent.ExecutorPolicy$CatchAndRecordFailures.onExecute(ExecutorPolicy.java:64)\n" + "\tat org.gradle.internal.concurrent.ManagedExecutorImpl$1.run(ManagedExecutorImpl.java:48)\n" + "\tat java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)\n" + "\tat java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)\n" + "\tat org.gradle.internal.concurrent.ThreadFactoryImpl$ManagedThreadRunnable.run(ThreadFactoryImpl.java:56)\n" + "\tat java.base/java.lang.Thread.run(Thread.java:844)";

            Assert.assertEquals(trimDynamicParts(expectedStackTrace), trimDynamicParts(Exceptions.getStackTraceString(e, new HashSet<String>(), new HashSet<String>(), new HashSet<String>())));
        } finally {
            Exceptions.clearGroupPackages();
        }
    }

    @Test
    public void getStackTraceWithMaxDepth() {
        Exception level4Exception = new ArrayIndexOutOfBoundsException("Index not valid.");
        Exception level3Exception = new ConcurrentModificationException(level4Exception);
        Exception level2Exception = new IllegalStateException(level3Exception);
        final Exception level1Exception = new MBeanException(level2Exception);

        try {
            Callable<String> stringCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    throw level1Exception;
                }
            };
            stringCallable.call();
        } catch (Exception e) {
            String expectedStackTrace = "javax.management.MBeanException\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTraceWithMaxDepth(ExceptionsTest.java:287)\n" + "\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" + "Caused by: java.lang.IllegalStateException: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTraceWithMaxDepth(ExceptionsTest.java:286)\n" + "\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" + "Caused by: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTraceWithMaxDepth(ExceptionsTest.java:285)\n" + "\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" + "Caused by: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTraceWithMaxDepth(ExceptionsTest.java:284)\n" + "\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)";

            Assert.assertEquals(trimDynamicParts(expectedStackTrace), trimDynamicParts(Exceptions.getStackTraceString(e, 2)));
        }

        try {
            Exceptions.setIgnoreAllCauses(true);
            Callable<String> stringCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    throw level1Exception;
                }
            };
            stringCallable.call();
        } catch (Exception e) {
            String expectedStackTrace = "javax.management.MBeanException\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTraceWithMaxDepth(ExceptionsTest.java:287)\n" + "\tat jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)";

            Assert.assertEquals(trimDynamicParts(expectedStackTrace), trimDynamicParts(Exceptions.getStackTraceString(e, 2)));
        } finally {
            Exceptions.setIgnoreAllCauses(false);
        }

        try {
            Callable<String> stringCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    throw level1Exception;
                }
            };
            stringCallable.call();
        } catch (Exception e) {
            String expectedStackTrace = "javax.management.MBeanException\n" + "\tat com.arthenica.smartexception.java.ExceptionsTest.getStackTraceWithMaxDepth(ExceptionsTest.java:287)\n" + "\tat jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)";

            Assert.assertEquals(trimDynamicParts(expectedStackTrace), trimDynamicParts(Exceptions.getStackTraceString(e, 2, true)));
        }
    }

    @Test
    public void getAllMessages() {
        Exception level2Exception = new IllegalStateException("Invalid running state.");
        final Exception level1Exception = new MBeanException(level2Exception, "Bean creation failed.");

        try {
            Callable<String> stringCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    throw level1Exception;
                }
            };
            stringCallable.call();
        } catch (Exception e) {
            Assert.assertEquals("Bean creation failed.\n - Caused by: Invalid running state.", trimDynamicParts(Exceptions.getAllMessages(e)));
        }
    }

    @Test
    public void containsCauseWithException() {
        String randomFileNumber = String.format("random-file-%d.log", System.currentTimeMillis());

        try {
            new FileInputStream(randomFileNumber);
        } catch (FileNotFoundException e) {
            Assert.assertTrue(Exceptions.containsCause(e, FileNotFoundException.class));
        }
    }

    @Test
    public void containsCauseWithExceptionAndMessage() {
        try {
            Integer.parseInt("ABC");
        } catch (NumberFormatException e) {
            Assert.assertTrue(Exceptions.containsCause(e, NumberFormatException.class, "For input string: \"ABC\""));
        }
    }

    @Test
    public void getCause() {
        Exception level3Exception = new FileNotFoundException("Can not open file.");
        Exception level2Exception = new IllegalStateException(level3Exception);
        final Exception level1Exception = new Exception(level2Exception);

        try {
            Callable<String> stringCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    throw level1Exception;
                }
            };
            stringCallable.call();
        } catch (Exception e) {
            Assert.assertEquals(level3Exception, Exceptions.getCause(e));
        }
    }

    @Test
    public void getCauseWithMaxDepth() {
        Exception level4Exception = new ArrayIndexOutOfBoundsException("Index not valid.");
        Exception level3Exception = new ConcurrentModificationException(level4Exception);
        Exception level2Exception = new DigestException(level3Exception);
        final Exception level1Exception = new MBeanException(level2Exception);

        try {
            Callable<String> stringCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    throw level1Exception;
                }
            };
            stringCallable.call();
        } catch (Exception e) {
            Assert.assertEquals(level3Exception, Exceptions.getCause(e, 2));
        }
    }

    @Test
    public void searchCause() {
        Exception level4Exception = new ArrayIndexOutOfBoundsException("Index not valid.");
        Exception level3Exception = new ConcurrentModificationException(level4Exception);
        Exception level2Exception = new IllegalStateException(level3Exception);
        final Exception level1Exception = new MBeanException(level2Exception);

        try {
            Callable<String> stringCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    throw level1Exception;
                }
            };
            stringCallable.call();
        } catch (Exception e) {
            Assert.assertNull(Exceptions.searchCause(e, ArrayIndexOutOfBoundsException.class, 2));
            Assert.assertNull(Exceptions.searchCause(e, ConcurrentModificationException.class, 1));
            Assert.assertNull(Exceptions.searchCause(e, IllegalArgumentException.class, 10));
        }
    }

    @Test
    public void searchCauseWithCauseClass() {
        Exception level4Exception = new ArrayIndexOutOfBoundsException("Index not valid.");
        Exception level3Exception = new ConcurrentModificationException(level4Exception);
        Exception level2Exception = new IllegalStateException(level3Exception);
        final Exception level1Exception = new MBeanException(level2Exception);

        try {
            Callable<String> stringCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    throw level1Exception;
                }
            };
            stringCallable.call();
        } catch (Exception e) {
            Assert.assertEquals(level3Exception, Exceptions.searchCause(e, ConcurrentModificationException.class));
            Assert.assertEquals(level3Exception, Exceptions.searchCause(e, ConcurrentModificationException.class, "Index not valid."));
            Assert.assertNull(Exceptions.searchCause(e, ConcurrentModificationException.class, "I am valid index."));
        }
    }

    @Test
    public void searchCauseWithCauseClassAndCauseMessageAndMaxDepth() {
        Exception level4Exception = new ArrayIndexOutOfBoundsException("Index not valid.");
        Exception level3Exception = new ConcurrentModificationException(level4Exception);
        Exception level2Exception = new IllegalStateException(level3Exception);
        final Exception level1Exception = new MBeanException(level2Exception);

        try {
            Callable<String> stringCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    throw level1Exception;
                }
            };
            stringCallable.call();
        } catch (Exception e) {
            Assert.assertNull(Exceptions.searchCause(e, ArrayIndexOutOfBoundsException.class, "Index not valid.", 2));
            Assert.assertNull(Exceptions.searchCause(e, ArrayIndexOutOfBoundsException.class, "Index valid.", 3));
            Assert.assertEquals(level4Exception, Exceptions.searchCause(e, ArrayIndexOutOfBoundsException.class, "Index not valid.", 3));
        }
    }

    @Test
    public void packageInformationJunit() {
        RuntimeException runtimeException = new RuntimeException("Fail!");

        StackTraceElement stackTraceElement = Arrays.asList(AbstractExceptions.getStackTrace(new ThrowableWrapper(runtimeException), 6)).get(5);
        Assert.assertEquals("junit-4.13.2.jar", libraryName(stackTraceElement));
        Assert.assertEquals("4.13.2", version(stackTraceElement));
    }

    @Test
    public void packageInformationApacheCommons() {
        try {
            Hex.decodeHex("12345".toCharArray());
        } catch (DecoderException exception) {
            StackTraceElement[] stackTrace = AbstractExceptions.getStackTrace(new ThrowableWrapper(exception), 10);

            StackTraceElement stackTraceElement = Arrays.asList(stackTrace).get(0);
            Assert.assertEquals("commons-codec-1.15.jar", libraryName(stackTraceElement));
            Assert.assertEquals("1.15", version(stackTraceElement));
        }
    }

    @Test
    public void packageInformationJson() {
        try {
            new JSONArray("");
        } catch (JSONException exception) {
            StackTraceElement[] stackTrace = AbstractExceptions.getStackTrace(new ThrowableWrapper(exception), 10);

            StackTraceElement stackTraceElement = Arrays.asList(stackTrace).get(0);
            Assert.assertEquals("android-json-0.0.20131108.vaadin1.jar", libraryName(stackTraceElement));
            Assert.assertEquals("0.0.20131108.vaadin1", version(stackTraceElement));
        }
    }

    @Test
    public void packageInformationJackson() {
        try {
            new ObjectMapper().readValue("", Long.class);
        } catch (JsonProcessingException exception) {
            StackTraceElement[] stackTrace = AbstractExceptions.getStackTrace(new ThrowableWrapper(exception), 10);

            StackTraceElement stackTraceElement = Arrays.asList(stackTrace).get(0);
            Assert.assertEquals("jackson-databind-2.10.5.1.jar", libraryName(stackTraceElement));
            Assert.assertEquals("2.10.5.1", version(stackTraceElement));
        }
    }

    String libraryName(final StackTraceElement stackTraceElement) {
        String className = stackTraceElement.getClassName();
        Class<?> loadedClass = Exceptions.classLoader.loadClass(className);
        if (loadedClass != null) {
            return AbstractExceptions.libraryName(loadedClass);
        } else {
            return null;
        }
    }

    String version(final StackTraceElement stackTraceElement) {
        String className = stackTraceElement.getClassName();
        Class<?> loadedClass = Exceptions.classLoader.loadClass(className);
        if (loadedClass != null) {
            return AbstractExceptions.version(Exceptions.packageLoader, loadedClass, AbstractExceptions.packageName(className));
        } else {
            return null;
        }
    }

}
