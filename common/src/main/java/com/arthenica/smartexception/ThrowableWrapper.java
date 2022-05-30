/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2022, Taner Sener
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

import java.util.*;

public class ThrowableWrapper {
    private final String message;
    private final ThrowableWrapper cause;
    private final String className;
    private final ThrowableWrapper[] suppressed;
    private final StackTraceElementWrapper[] stackTrace;

    public ThrowableWrapper(final Throwable throwable) {
        this(throwable, Collections.newSetFromMap(new IdentityHashMap<Throwable, Boolean>()));
    }

    public ThrowableWrapper(final Throwable throwable, final Set<Throwable> alreadyWrapped) {
        alreadyWrapped.add(throwable);

        message = throwable.getMessage();
        if (throwable.getCause() != null && !alreadyWrapped.contains(throwable.getCause())) {
            cause = new ThrowableWrapper(throwable.getCause(), alreadyWrapped);
        } else {
            cause = null;
        }
        className = throwable.getClass().getName();

        final Throwable[] suppressedThrowableArray = throwable.getSuppressed();
        final List<ThrowableWrapper> tmpList = new LinkedList<>();
        for (int i = 0, throwableSuppressedLength = suppressedThrowableArray.length; i < throwableSuppressedLength; i++) {
            if (!alreadyWrapped.contains(suppressedThrowableArray[i])) {
                tmpList.add(new ThrowableWrapper(suppressedThrowableArray[i], alreadyWrapped));
            }
        }
        suppressed = tmpList.toArray(new ThrowableWrapper[0]);

        final StackTraceElement[] stackTraceElements = throwable.getStackTrace();
        stackTrace = new StackTraceElementWrapper[stackTraceElements.length];
        for (int i = 0, stackTraceElementsLength = stackTraceElements.length; i < stackTraceElementsLength; i++) {
            stackTrace[i] = new StackTraceElementWrapper(stackTraceElements[i]);
        }
    }

    public ThrowableWrapper(final String message, final ThrowableWrapper cause, final String className,
                            final ThrowableWrapper[] suppressed, final StackTraceElementWrapper[] stackTrace) {
        this.message = message;
        this.cause = cause;
        this.className = className;
        this.suppressed = suppressed;
        this.stackTrace = stackTrace;
    }

    public String getMessage() {
        return message;
    }

    public ThrowableWrapper getCause() {
        return cause;
    }

    public String getClassName() {
        return className;
    }

    public ThrowableWrapper[] getSuppressed() {
        return suppressed;
    }

    public StackTraceElementWrapper[] getStackTrace() {
        return stackTrace;
    }
}
