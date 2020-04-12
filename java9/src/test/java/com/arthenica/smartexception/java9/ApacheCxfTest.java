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

package com.arthenica.smartexception.java9;

import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class ApacheCxfTest {

    @Test
    public void access() {
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:12345/rs");
        target = target.path("service").queryParam("param1", "value1");

        Invocation.Builder builder = target.request();
        try {
            builder.get();
        } catch (Exception e) {
            String expectedStackTrace = "\n" +
                    "javax.ws.rs.ProcessingException: java.net.ConnectException: ConnectException invoking http://localhost:12345/rs/service?param1=value1: Connection refused (Connection refused)\n" +
                    "\tat org.apache.cxf ... 10 more\n" +
                    "\tat com.arthenica.smartexception.java9.ApacheCxfTest.access(ApacheCxfTest.java:56)\n" +
                    "Caused by: java.net.ConnectException: ConnectException invoking http://localhost:12345/rs/service?param1=value1: Connection refused (Connection refused)\n" +
                    "\tat java.base/jdk.internal.reflect ... 2 more\n" +
                    "\tat java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:488)\n" +
                    "\tat org.apache.cxf ... 15 more\n" +
                    "\tat com.arthenica.smartexception.java9.ApacheCxfTest.access(ApacheCxfTest.java:56)\n" +
                    "Caused by: java.net.ConnectException: Connection refused (Connection refused)\n" +
                    "\tat java.base/java.net ... 4 more\n" +
                    "\tat java.base/sun.net ... 11 more\n" +
                    "\tat java.base/java.net.HttpURLConnection.getResponseCode(HttpURLConnection.java:527)\n" +
                    "\tat org.apache.cxf ... 1 more\n" +
                    "\tat java.base/java.security.AccessController.doPrivileged(Native Method)\n" +
                    "\tat org.apache.cxf ... 18 more\n" +
                    "\tat com.arthenica.smartexception.java9.ApacheCxfTest.access(ApacheCxfTest.java:56)";

            Assert.assertEquals(ExceptionsTest.trimDynamicParts(expectedStackTrace), ExceptionsTest.trimDynamicParts(Exceptions.getStackTraceString(e, Collections.singleton("com.arthenica"), new HashSet<>(Arrays.asList("org.apache.cxf", "java.net", "java.security", "sun.net", "jdk.internal.reflect")), Collections.emptySet())));
        }
    }

}
