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

/**
 * <p>Utility class to handle Throwable objects and stack traces. This implementation uses some language features that
 * are available on Java 9 and later. Please refer to {@link com.arthenica.smartexception.compat.Exceptions} if you are
 * using an older Java version or Android.
 *
 * @author Taner Sener
 */
public class Exceptions extends AbstractExceptions {

    /**
     * <p>Default value for ignoring module name when stack trace elements are printed or converted to string.
     */
    public static final boolean DEFAULT_IGNORE_MODULE_NAME = false;

    /**
     * <p>Stores the value of global ignore module name option.
     */
    protected static boolean ignoreModuleName = DEFAULT_IGNORE_MODULE_NAME;

    static {
        AbstractExceptions.setStackTraceElementSerializer(new StackTraceElementSerializer() {

            public String toString(StackTraceElement stackTraceElement) {
                final StringBuilder stringBuilder = new StringBuilder();

                if (!ignoreModuleName) {
                    stringBuilder.append(stackTraceElement.getModuleName());
                }

                stringBuilder.append(stackTraceElement.getClassName());
                stringBuilder.append(".");
                stringBuilder.append(stackTraceElement.getMethodName());

                if (stackTraceElement.isNativeMethod()) {
                    stringBuilder.append("(Native Method)");
                } else if (!AbstractExceptions.isEmpty(stackTraceElement.getFileName())) {
                    stringBuilder.append("(");
                    stringBuilder.append(stackTraceElement.getFileName());
                    if (stackTraceElement.getLineNumber() >= 0) {
                        stringBuilder.append(":");
                        stringBuilder.append(stackTraceElement.getLineNumber());
                    }
                    stringBuilder.append(")");
                } else {
                    stringBuilder.append("(Native Method)");
                }

                return stringBuilder.toString();
            }
        });
    }

    /**
     * <p>Returns the value of ignore module name option.
     *
     * @return the value of global ignore module name option. If value is true then stack trace elements printed or
     * converted to string will include module name at the beginning of the line. Otherwise module name will be
     * ignored.
     */
    public static boolean getIgnoreModuleName() {
        return ignoreModuleName;
    }

    /**
     * <p>Sets the value of ignore module name option.
     *
     * @param ignoreModuleName new global ignore module name option. If value is true then stack trace elements printed
     *                         or converted to string will include module name at the beginning of the line. Otherwise
     *                         module name will be ignored.
     */
    public static void setIgnoreModuleName(final boolean ignoreModuleName) {
        Exceptions.ignoreModuleName = ignoreModuleName;
    }

}
