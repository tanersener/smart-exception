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

import com.arthenica.smartexception.spring.RestController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = RestController.class)
public class SpringTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getUserTest() {
        try {
            mvc.perform(MockMvcRequestBuilders.get("/user")).andExpect(status().isOk());
        } catch (Exception e) {
            String expectedStackTrace = "\n" +
                    "org.springframework.web.util.NestedServletException: Request processing failed; nested exception is java.lang.StringIndexOutOfBoundsException: Invalid index.\n" +
                    "\tat org.springframework ... 6 more\n" +
                    "\tat com.arthenica.smartexception.SpringTest.getUserTest(SpringTest.java:62)\n" +
                    "Caused by: java.lang.StringIndexOutOfBoundsException: Invalid index.\n" +
                    "\tat com.arthenica.smartexception.spring.RestController.getUsers(RestController.java:46)\n" +
                    "\tat org.springframework ... 14 more\n" +
                    "\tat com.arthenica.smartexception.SpringTest.getUserTest(SpringTest.java:62)";

            Assertions.assertEquals(expectedStackTrace, AbstractExceptions.getStackTraceString(e, Collections.singleton("com.arthenica"), Collections.singleton("org.springframework"), new HashSet<>(Arrays.asList("sun.net", "sun.security", "sun.reflect", "java.lang.reflect", "javax.servlet.http"))));
        }
    }

    @Test
    public void postUserTest() {
        try {
            mvc.perform(MockMvcRequestBuilders.get("/user", 12345)).andExpect(status().isOk());
        } catch (Exception e) {
            String expectedStackTrace = "\n" +
                    "org.springframework.web.util.NestedServletException: Request processing failed; nested exception is java.lang.StringIndexOutOfBoundsException: Invalid index.\n" +
                    "\tat org.springframework ... 6 more\n" +
                    "\tat com.arthenica.smartexception.SpringTest.postUserTest(SpringTest.java:80)\n" +
                    "Caused by: java.lang.StringIndexOutOfBoundsException: Invalid index.\n" +
                    "\tat com.arthenica.smartexception.spring.RestController.getUsers(RestController.java:46)\n" +
                    "\tat sun.reflect ... 2 more\n" +
                    "\tat java.lang.reflect.Method.invoke(Method.java:498)\n" +
                    "\tat org.springframework ... 14 more\n" +
                    "\tat com.arthenica.smartexception.SpringTest.postUserTest(SpringTest.java:80)";

            Assertions.assertEquals(expectedStackTrace, AbstractExceptions.getStackTraceString(e, Collections.singleton("com.arthenica"), new HashSet<>(Arrays.asList("org.springframework", "sun.net", "sun.security", "sun.reflect", "java.lang.reflect")), Collections.singleton("javax.servlet.http.HttpServlet")));
        }
    }

    @Test
    public void putUserTest() {
        try {
            mvc.perform(MockMvcRequestBuilders.post("/user").content("Body")).andExpect(status().isOk());
        } catch (Exception e) {
            String expectedStackTrace = "\n" +
                    "org.springframework.web.util.NestedServletException: Request processing failed; nested exception is java.lang.NoSuchMethodException: Method not found.\n" +
                    "\tat org.springframework ... 6 more\n" +
                    "\tat com.arthenica.smartexception.SpringTest.putUserTest(SpringTest.java:100)\n" +
                    "Caused by: java.lang.NoSuchMethodException: Method not found.\n" +
                    "\tat com.arthenica.smartexception.spring.RestController.update(RestController.java:56)\n" +
                    "\tat org.springframework ... 14 more\n" +
                    "\tat com.arthenica.smartexception.SpringTest.putUserTest(SpringTest.java:100)";

            Assertions.assertEquals(expectedStackTrace, AbstractExceptions.getStackTraceString(e, Collections.singleton("com.arthenica"), Collections.singleton("org.springframework"), new HashSet<>(Arrays.asList("sun.net", "sun.security", "sun.reflect", "java.lang.reflect", "javax.servlet.http"))));
        }
    }

}
