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

package com.arthenica.smartexception.java9;

import com.arthenica.smartexception.AbstractExceptions;
import com.arthenica.smartexception.StackTraceElementSerializer;

public class Java9StackTraceElementSerializer implements StackTraceElementSerializer {

    @Override
    public String toString(final StackTraceElement stackTraceElement, final boolean printModuleName, final boolean printPackageInformation) {
        final StringBuilder stringBuilder = new StringBuilder();

        if (printModuleName && !AbstractExceptions.isEmpty(stackTraceElement.getModuleName())) {
            stringBuilder.append(getModuleName(stackTraceElement));
        }

        stringBuilder.append(stackTraceElement.getClassName());
        stringBuilder.append(".");
        stringBuilder.append(stackTraceElement.getMethodName());

        if (stackTraceElement.isNativeMethod()) {
            stringBuilder.append(getNativeMethodDefinition());
        } else if (!AbstractExceptions.isEmpty(stackTraceElement.getFileName())) {
            stringBuilder.append("(");
            stringBuilder.append(stackTraceElement.getFileName());
            if (stackTraceElement.getLineNumber() >= 0) {
                stringBuilder.append(":");
                stringBuilder.append(stackTraceElement.getLineNumber());
            }
            stringBuilder.append(")");
        } else {
            stringBuilder.append(getUnknownSourceDefinition());
        }

        if (printPackageInformation) {
            stringBuilder.append(getPackageInformation(stackTraceElement));
        }

        return stringBuilder.toString();
    }

    @Override
    public String getPackageInformation(final StackTraceElement stackTraceElement) {
        final StringBuilder stringBuilder = new StringBuilder();

        String className = stackTraceElement.getClassName();
        Class<?> loadedClass = Exceptions.classLoader.loadClass(className);
        if (loadedClass != null) {
            final String libraryName = AbstractExceptions.libraryName(loadedClass);
            final String version = AbstractExceptions.version(Exceptions.packageLoader, loadedClass, AbstractExceptions.packageName(className));

            stringBuilder.append(AbstractExceptions.packageInformation(libraryName, version));
        }

        return stringBuilder.toString();
    }

    @Override
    public String getModuleName(StackTraceElement stackTraceElement) {
        return (stackTraceElement != null) ? !AbstractExceptions.isEmpty(stackTraceElement.getModuleName()) ? String.format("%s/", stackTraceElement.getModuleName()) : "" : "";
    }

    @Override
    public String getNativeMethodDefinition() {
        return "(Native Method)";
    }

    @Override
    public String getUnknownSourceDefinition() {
        return "(Unknown Source)";
    }

}
