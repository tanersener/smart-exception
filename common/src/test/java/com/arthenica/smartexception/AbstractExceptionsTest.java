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

import org.junit.Assert;
import org.junit.Test;

public class AbstractExceptionsTest {

    @Test
    public void isEmpty() {
        Assert.assertTrue(AbstractExceptions.isEmpty(""));
        Assert.assertTrue(AbstractExceptions.isEmpty(null));
        Assert.assertTrue(AbstractExceptions.isEmpty("  "));
    }

    @Test
    public void packageName() {
        Assert.assertEquals("com.arthenica.smartexception", AbstractExceptions.packageName("com.arthenica.smartexception.Exceptions"));
        Assert.assertEquals("java.lang", AbstractExceptions.packageName("java.lang.String"));
        Assert.assertEquals("", AbstractExceptions.packageName("String"));
    }

    @Test
    public void packageInformation() {
        assertPackageInformation(null, null, "");
        assertPackageInformation(null, "1.2.3", " [1.2.3]");
        assertPackageInformation("mylib.jar", null, " [mylib.jar]");
        assertPackageInformation("mylib-1.2.3.jar", null, " [mylib-1.2.3.jar]");
        assertPackageInformation("mylib.jar", "1.2.3", " [mylib.jar:1.2.3]");
        assertPackageInformation("mylib-1.2.3.jar", "1.2.3", " [mylib-1.2.3.jar]");
    }

    private void assertPackageInformation(final String libraryName, final String version, final String expectedPackageInformation) {
        String packageInformation = AbstractExceptions.packageInformation(libraryName, version);
        Assert.assertEquals(expectedPackageInformation, packageInformation);
    }

}
