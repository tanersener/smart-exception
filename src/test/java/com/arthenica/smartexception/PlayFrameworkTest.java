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

import com.arthenica.smartexception.Exceptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.management.MBeanException;
import java.util.ConcurrentModificationException;
import java.util.concurrent.Callable;

public class PlayFrameworkTest {

    @Test
    public void getStackTrace() {
    }

    public void getStackTrace2() {
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
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTrace(ExceptionsTest.java:54)\n" +
                    "Caused by: java.lang.IllegalStateException: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTrace(ExceptionsTest.java:53)\n" +
                    "Caused by: java.util.ConcurrentModificationException: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTrace(ExceptionsTest.java:52)\n" +
                    "Caused by: java.lang.ArrayIndexOutOfBoundsException: Index not valid.\n" +
                    "\tat com.arthenica.smartexception.ExceptionsTest.getStackTrace(ExceptionsTest.java:51)";

            Assertions.assertEquals(expectedStackTrace, Exceptions.getStackTraceString(e));
        } finally {
            Exceptions.clearRootPackages();
            Exceptions.clearIgnorePackages();
        }
    }

}
