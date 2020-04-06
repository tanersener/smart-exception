/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020, Taner Sener
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

package com.arthenica.smartexception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.management.MBeanException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.DigestException;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.concurrent.Callable;

public class ExceptionsTest {

    @Test
    public void getStackTrace() {
        Exception level4Exception = new ArrayIndexOutOfBoundsException("Index not valid.");
        Exception level3Exception = new ConcurrentModificationException(level4Exception);
        Exception level2Exception = new IllegalStateException(level3Exception);
        Exception level1Exception = new MBeanException(level2Exception);

        try {
            Exceptions.registerRootPackage("com.arthenica");
            Exceptions.registerIgnorePackage("java.util", false);

            Callable<String> stringCallable = () -> {
                throw level1Exception;
            };
            stringCallable.call();
        } catch (Exception e) {
            String expectedStackTrace = "\n" +
                    "javax.management.MBeanException\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTrace(ExceptionsTest.java:56)\n" +
                    "Caused by: java.lang.IllegalStateException: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTrace(ExceptionsTest.java:55)\n" +
                    "Caused by: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTrace(ExceptionsTest.java:54)\n" +
                    "Caused by: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTrace(ExceptionsTest.java:53)";

            Assertions.assertEquals(expectedStackTrace, Exceptions.getStackTraceString(e));
        } finally {
            Exceptions.clearRootPackages();
            Exceptions.clearIgnorePackages();
        }

        try {
            Exceptions.registerRootPackage("com.arthenica");
            Exceptions.registerIgnorePackage("java.util", true);

            Callable<String> stringCallable = () -> {
                throw level1Exception;
            };
            stringCallable.call();
        } catch (Exception e) {
            String expectedStackTrace = "\n" +
                    "javax.management.MBeanException\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTrace(ExceptionsTest.java:56)\n" +
                    "Caused by: java.lang.IllegalStateException: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTrace(ExceptionsTest.java:55)\n" +
                    "Caused by: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTrace(ExceptionsTest.java:54)";

            Assertions.assertEquals(expectedStackTrace, Exceptions.getStackTraceString(e));
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
        Exception level1Exception = new MBeanException(level2Exception);

        try {
            Callable<String> stringCallable = () -> {
                throw level1Exception;
            };
            stringCallable.call();
        } catch (Exception e) {
            String expectedStackTrace = "\n" +
                    "javax.management.MBeanException\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTraceWithPackages(ExceptionsTest.java:112)\n" +
                    "Caused by: java.lang.IllegalStateException: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTraceWithPackages(ExceptionsTest.java:111)\n" +
                    "Caused by: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTraceWithPackages(ExceptionsTest.java:110)\n" +
                    "Caused by: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTraceWithPackages(ExceptionsTest.java:109)";

            Assertions.assertEquals(expectedStackTrace, Exceptions.getStackTraceString(e, Collections.singleton("com.arthenica"), Collections.emptySet(), Collections.emptySet()));
        }

        try {
            Callable<String> stringCallable = () -> {
                throw level1Exception;
            };
            stringCallable.call();
        } catch (Exception e) {
            String expectedStackTrace = "\n" +
                    "javax.management.MBeanException\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTraceWithPackages(ExceptionsTest.java:112)\n" +
                    "Caused by: java.lang.IllegalStateException: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTraceWithPackages(ExceptionsTest.java:111)\n" +
                    "Caused by: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTraceWithPackages(ExceptionsTest.java:110)\n" +
                    "Caused by: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTraceWithPackages(ExceptionsTest.java:109)";

            Assertions.assertEquals(expectedStackTrace, Exceptions.getStackTraceString(e, Collections.singleton("com.arthenica"), Collections.emptySet(), Collections.emptySet()));
        }
    }

    @Test
    public void getStackTraceWithRootPackage() {
        Exception level4Exception = new ArrayIndexOutOfBoundsException("Index not valid.");
        Exception level3Exception = new ConcurrentModificationException(level4Exception);
        Exception level2Exception = new IllegalStateException(level3Exception);
        Exception level1Exception = new MBeanException(level2Exception);

        try {
            Callable<String> stringCallable = () -> {
                throw level1Exception;
            };
            stringCallable.call();
        } catch (Exception e) {
            String expectedStackTrace = "\n" +
                    "javax.management.MBeanException\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTraceWithRootPackage(ExceptionsTest.java:158)\n" +
                    "Caused by: java.lang.IllegalStateException: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTraceWithRootPackage(ExceptionsTest.java:157)\n" +
                    "Caused by: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTraceWithRootPackage(ExceptionsTest.java:156)\n" +
                    "Caused by: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTraceWithRootPackage(ExceptionsTest.java:155)";

            Assertions.assertEquals(expectedStackTrace, Exceptions.getStackTraceString(e, "com.arthenica"));
        }
    }

    @Test
    public void getStackTraceWithRootPackageAndGroupPackage() {
        try {
            Integer.parseInt("ABC");
        } catch (NumberFormatException e) {
            String expectedStackTrace = "\n" +
                    "java.lang.NumberFormatException: For input string: \"ABC\"\n" +
                    "\tat java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)\n" +
                    "\tat java.lang.Integer.parseInt(Integer.java:580)\n" +
                    "\tat java.lang.Integer.parseInt(Integer.java:615)\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTraceWithRootPackageAndGroupPackage(ExceptionsTest.java:183)\n" +
                    "\tat sun.reflect ... 2 more\n" +
                    "\tat java.lang.reflect.Method.invoke(Method.java:498)\n" +
                    "\tat org.junit ... 26 more\n" +
                    "\tat java.util.ArrayList.forEach(ArrayList.java:1257)\n" +
                    "\tat org.junit ... 8 more\n" +
                    "\tat java.util.ArrayList.forEach(ArrayList.java:1257)\n" +
                    "\tat org.junit ... 16 more\n" +
                    "\tat org.gradle ... 3 more\n" +
                    "\tat sun.reflect ... 2 more\n" +
                    "\tat java.lang.reflect.Method.invoke(Method.java:498)\n" +
                    "\tat org.gradle ... 3 more\n" +
                    "\tat com.sun.proxy.$Proxy2.stop(Unknown Source)\n" +
                    "\tat org.gradle.api.internal.tasks.testing.worker.TestWorker.stop(TestWorker.java:132)\n" +
                    "\tat sun.reflect ... 2 more\n" +
                    "\tat java.lang.reflect.Method.invoke(Method.java:498)\n" +
                    "\tat org.gradle ... 6 more\n" +
                    "\tat java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)\n" +
                    "\tat java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)\n" +
                    "\tat org.gradle.internal.concurrent.ThreadFactoryImpl$ManagedThreadRunnable.run(ThreadFactoryImpl.java:56)\n" +
                    "\tat java.lang.Thread.run(Thread.java:748)";

            Assertions.assertEquals(expectedStackTrace, Exceptions.getStackTraceString(e, Collections.emptySet(), new HashSet<>(Arrays.asList("org.junit", "sun.reflect", "org.gradle")), Collections.emptySet()));
        }
    }

    @Test
    public void getStackTraceWithMaxDepth() {
        Exception level4Exception = new ArrayIndexOutOfBoundsException("Index not valid.");
        Exception level3Exception = new ConcurrentModificationException(level4Exception);
        Exception level2Exception = new IllegalStateException(level3Exception);
        Exception level1Exception = new MBeanException(level2Exception);

        try {
            Callable<String> stringCallable = () -> {
                throw level1Exception;
            };
            stringCallable.call();
        } catch (Exception e) {
            String expectedStackTrace = "\n" +
                    "javax.management.MBeanException\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTraceWithMaxDepth(ExceptionsTest.java:221)\n" +
                    "\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" +
                    "Caused by: java.lang.IllegalStateException: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTraceWithMaxDepth(ExceptionsTest.java:220)\n" +
                    "\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" +
                    "Caused by: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTraceWithMaxDepth(ExceptionsTest.java:219)\n" +
                    "\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" +
                    "Caused by: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTraceWithMaxDepth(ExceptionsTest.java:218)\n" +
                    "\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)";

            Assertions.assertEquals(expectedStackTrace, Exceptions.getStackTraceString(e, 2));
        }
    }

    @Test
    public void getAllMessages() {
        Exception level2Exception = new IllegalStateException("Invalid running state.");
        Exception level1Exception = new MBeanException(level2Exception, "Bean creation failed.");

        try {
            Callable<String> stringCallable = () -> {
                throw level1Exception;
            };
            stringCallable.call();
        } catch (Exception e) {
            Assertions.assertEquals("Bean creation failed.\n - Caused by: Invalid running state.", Exceptions.getAllMessages(e));
        }
    }

    @Test
    public void containsCauseWithException() {
        String randomFileNumber = String.format("random-file-%d.log", System.currentTimeMillis());

        try {
            new FileInputStream(new File(randomFileNumber));
        } catch (FileNotFoundException e) {
            Assertions.assertTrue(Exceptions.containsCause(e, FileNotFoundException.class));
        }
    }

    @Test
    public void containsCauseWithExceptionAndMessage() {
        try {
            Integer.parseInt("ABC");
        } catch (NumberFormatException e) {
            Assertions.assertTrue(Exceptions.containsCause(e, NumberFormatException.class, "For input string: \"ABC\""));
        }
    }

    @Test
    public void getCause() {
        Exception level3Exception = new FileNotFoundException("Can not open file.");
        Exception level2Exception = new IllegalStateException(level3Exception);
        Exception level1Exception = new Exception(level2Exception);

        try {
            Callable<String> stringCallable = () -> {
                throw level1Exception;
            };
            stringCallable.call();
        } catch (Exception e) {
            Assertions.assertEquals(level3Exception, Exceptions.getCause(e));
        }
    }

    @Test
    public void getCauseWithMaxDepth() {
        Exception level4Exception = new ArrayIndexOutOfBoundsException("Index not valid.");
        Exception level3Exception = new ConcurrentModificationException(level4Exception);
        Exception level2Exception = new DigestException(level3Exception);
        Exception level1Exception = new MBeanException(level2Exception);

        try {
            Callable<String> stringCallable = () -> {
                throw level1Exception;
            };
            stringCallable.call();
        } catch (Exception e) {
            Assertions.assertEquals(level3Exception, Exceptions.getCause(e, 2));
        }
    }

    @Test
    public void searchCause() {
        Exception level4Exception = new ArrayIndexOutOfBoundsException("Index not valid.");
        Exception level3Exception = new ConcurrentModificationException(level4Exception);
        Exception level2Exception = new IllegalStateException(level3Exception);
        Exception level1Exception = new MBeanException(level2Exception);

        try {
            Callable<String> stringCallable = () -> {
                throw level1Exception;
            };
            stringCallable.call();
        } catch (Exception e) {
            Assertions.assertNull(Exceptions.searchCause(e, ArrayIndexOutOfBoundsException.class, 2));
            Assertions.assertNull(Exceptions.searchCause(e, ConcurrentModificationException.class, 1));
            Assertions.assertNull(Exceptions.searchCause(e, IllegalArgumentException.class, 10));
        }
    }

    @Test
    public void searchCauseWithCauseClass() {
        Exception level4Exception = new ArrayIndexOutOfBoundsException("Index not valid.");
        Exception level3Exception = new ConcurrentModificationException(level4Exception);
        Exception level2Exception = new IllegalStateException(level3Exception);
        Exception level1Exception = new MBeanException(level2Exception);

        try {
            Callable<String> stringCallable = () -> {
                throw level1Exception;
            };
            stringCallable.call();
        } catch (Exception e) {
            Assertions.assertEquals(level3Exception, Exceptions.searchCause(e, ConcurrentModificationException.class));
            Assertions.assertEquals(level3Exception, Exceptions.searchCause(e, ConcurrentModificationException.class, "Index not valid."));
            Assertions.assertNull(Exceptions.searchCause(e, ConcurrentModificationException.class, "I am valid index."));
        }
    }

    @Test
    public void searchCauseWithCauseClassAndCauseMessageAndMaxDepth() {
        Exception level4Exception = new ArrayIndexOutOfBoundsException("Index not valid.");
        Exception level3Exception = new ConcurrentModificationException(level4Exception);
        Exception level2Exception = new IllegalStateException(level3Exception);
        Exception level1Exception = new MBeanException(level2Exception);

        try {
            Callable<String> stringCallable = () -> {
                throw level1Exception;
            };
            stringCallable.call();
        } catch (Exception e) {
            Assertions.assertNull(Exceptions.searchCause(e, ArrayIndexOutOfBoundsException.class, "Index not valid.", 2));
            Assertions.assertNull(Exceptions.searchCause(e, ArrayIndexOutOfBoundsException.class, "Index valid.", 3));
            Assertions.assertEquals(level4Exception, Exceptions.searchCause(e, ArrayIndexOutOfBoundsException.class, "Index not valid.", 3));
        }
    }

    @Test
    public void isEmpty() {
        Assertions.assertTrue(Exceptions.isEmpty(""));
        Assertions.assertTrue(Exceptions.isEmpty(null));
        Assertions.assertTrue(Exceptions.isEmpty("  "));
    }

    @Test
    public void packageName() {
        Assertions.assertEquals("com.arthenica.smartexception", Exceptions.packageName("com.arthenica.smartexception.Exceptions"));
        Assertions.assertEquals("java.lang", Exceptions.packageName("java.lang.String"));
        Assertions.assertEquals("", Exceptions.packageName("String"));
    }

}
