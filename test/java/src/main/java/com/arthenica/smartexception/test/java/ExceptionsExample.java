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

package com.arthenica.smartexception.test.java;

import com.arthenica.smartexception.java.Exceptions;

import javax.management.MBeanException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.DigestException;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.concurrent.Callable;

public class ExceptionsExample {

    public static void main(String[] args) {
        ExceptionsExample exceptionsTest = new ExceptionsExample();
        exceptionsTest.getStackTrace();
        exceptionsTest.getStackTraceWithPackages();
        exceptionsTest.getStackTraceWithRootPackage();
        exceptionsTest.getStackTraceWithRootPackageAndGroupPackage();
        exceptionsTest.getStackTraceWithMaxDepth();
        exceptionsTest.getAllMessages();
        exceptionsTest.containsCauseWithException();
        exceptionsTest.containsCauseWithExceptionAndMessage();
        exceptionsTest.getCause();
        exceptionsTest.getCauseWithMaxDepth();
        exceptionsTest.searchCause();
        exceptionsTest.searchCauseWithCauseClass();
        exceptionsTest.searchCauseWithCauseClassAndCauseMessageAndMaxDepth();
    }

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
            System.out.println(Exceptions.getStackTraceString(e));
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
            System.out.println(Exceptions.getStackTraceString(e));
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
            System.out.println(Exceptions.getStackTraceString(e, true));
        } finally {
            Exceptions.clearRootPackages();
            Exceptions.clearIgnorePackages();
        }
    }

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
            System.out.println(Exceptions.getStackTraceString(e, Collections.singleton("com.arthenica"), new HashSet<String>(), new HashSet<String>()));
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
            System.out.println(Exceptions.getStackTraceString(e, Collections.singleton("com.arthenica"), new HashSet<String>(), new HashSet<String>()));
        }
    }

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
            System.out.println(Exceptions.getStackTraceString(e, "com.arthenica"));
        }
    }

    public void getStackTraceWithRootPackageAndGroupPackage() {
        try {
            Integer.parseInt("ABC");
        } catch (NumberFormatException e) {
            System.out.println(Exceptions.getStackTraceString(e, "com.arthenica", "org.gradle"));
        }

        try {
            Exceptions.registerGroupPackage("org.junit");
            Exceptions.registerGroupPackage("sun.reflect");
            Exceptions.registerGroupPackage("org.gradle");

            Integer.parseInt("ABC");
        } catch (NumberFormatException e) {
            System.out.println(Exceptions.getStackTraceString(e, new HashSet<String>(), new HashSet<String>(), new HashSet<String>()));
        } finally {
            Exceptions.clearGroupPackages();
        }
    }

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
            System.out.println(Exceptions.getStackTraceString(e, 2));
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
            System.out.println(Exceptions.getStackTraceString(e, 2));
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
            System.out.println(Exceptions.getStackTraceString(e, 2, true));
        }
    }

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
            System.out.println(Exceptions.getAllMessages(e));
        }
    }

    public void containsCauseWithException() {
        String randomFileNumber = String.format("random-file-%d.log", System.currentTimeMillis());

        try {
            new FileInputStream(new File(randomFileNumber));
        } catch (FileNotFoundException e) {
            if (Exceptions.containsCause(e, FileNotFoundException.class)) {
                System.out.println("This is file not found error.");
            }
        }
    }

    public void containsCauseWithExceptionAndMessage() {
        try {
            Integer.parseInt("ABC");
        } catch (NumberFormatException e) {
            if (Exceptions.containsCause(e, NumberFormatException.class, "For input string: \"ABC\"")) {
                System.out.println("This is number format error.");
            }
        }
    }

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
            System.out.println(Exceptions.getStackTraceString(Exceptions.getCause(e)));
        }
    }

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
            System.out.println(Exceptions.getStackTraceString(Exceptions.getCause(e, 2)));
        }
    }

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
            if (Exceptions.searchCause(e, ArrayIndexOutOfBoundsException.class, 2) == null) {
                System.out.println("ArrayIndexOutOfBoundsException is not among the first two exceptions.");
            }
            if (Exceptions.searchCause(e, ConcurrentModificationException.class, 1) == null) {
                System.out.println("ConcurrentModificationException is not among the first two exceptions.");
            }
            if (Exceptions.searchCause(e, IllegalArgumentException.class, 10) == null) {
                System.out.println("IllegalArgumentException is not among the first two exceptions.");
            }
        }
    }

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
            if (Exceptions.searchCause(e, ConcurrentModificationException.class) != null) {
                System.out.println("ConcurrentModificationException found.");
            }
            if (Exceptions.searchCause(e, ConcurrentModificationException.class, "Index not valid.") != null) {
                System.out.println("ConcurrentModificationException found.");
            }
            if (Exceptions.searchCause(e, ConcurrentModificationException.class, "I am valid index.") == null) {
                System.out.println("ConcurrentModificationException not found.");
            }
        }
    }

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
            if (Exceptions.searchCause(e, ArrayIndexOutOfBoundsException.class, "Index not valid.", 2) != null) {
                System.out.println("ArrayIndexOutOfBoundsException found.");
            }
            if (Exceptions.searchCause(e, ArrayIndexOutOfBoundsException.class, "Index valid.", 3) != null) {
                System.out.println("ArrayIndexOutOfBoundsException found.");
            }
            if (Exceptions.searchCause(e, ArrayIndexOutOfBoundsException.class, "Index not valid.", 3) == null) {
                System.out.println("ArrayIndexOutOfBoundsException not found.");
            }
        }
    }

}
