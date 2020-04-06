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

import java.util.*;

/**
 * <p>Utility class to handle Throwable objects and stack traces.
 *
 * @author Taner Sener
 */
public class Exceptions {

    /**
     * <p>Default max depth used in methods which do not have a maxDepth argument.
     */
    public static final int DEFAULT_MAX_DEPTH = 10;

    /**
     * <p>Stores global root package names.
     */
    protected static final Set<String> rootPackageSet = Collections.synchronizedSet(new HashSet<>());

    /**
     * <p>Stores global group package names.
     */
    protected static final Set<String> groupPackageSet = Collections.synchronizedSet(new HashSet<>());

    /**
     * <p>Stores global ignore package names.
     */
    protected static final Set<String> ignorePackageSet = Collections.synchronizedSet(new HashSet<>());

    /**
     * <p>Stores global ignore cause package names.
     */
    protected static final Set<String> ignoreCausePackageSet = Collections.synchronizedSet(new HashSet<>());

    /**
     * <p>Registers a new root package.
     *
     * @param packageString root package name to register
     */
    public static void registerRootPackage(final String packageString) {
        rootPackageSet.add(packageString);
    }

    /**
     * <p>Registers a new group package.
     *
     * @param packageString group package name to register
     */
    public static void registerGroupPackage(final String packageString) {
        groupPackageSet.add(packageString);
    }

    /**
     * <p>Registers a new ignore package.
     *
     * @param packageString      ignore package name to register
     * @param ignoreCauseClasses ignore cause classes from this package too
     */
    public static void registerIgnorePackage(final String packageString, final boolean ignoreCauseClasses) {
        ignorePackageSet.add(packageString);
        if (ignoreCauseClasses) {
            ignoreCausePackageSet.add(packageString);
        }
    }

    /**
     * <p>Clears previously registered root packages.
     */
    public static void clearRootPackages() {
        rootPackageSet.clear();
    }

    /**
     * <p>Clears previously registered group packages.
     */
    public static void clearGroupPackages() {
        groupPackageSet.clear();
    }

    /**
     * <p>Clears previously registered ignore packages.
     */
    public static void clearIgnorePackages() {
        ignorePackageSet.clear();
        ignoreCausePackageSet.clear();
    }

    /**
     * <p>Returns the smart stack trace for the given <code>throwable</code>.
     *
     * <p>This method uses root packages registered by {@link #registerRootPackage(String)} and ignore packages
     * registered by {@link #registerIgnorePackage(String, boolean)} to build the smart stack trace.
     *
     * @param throwable parent throwable
     * @return a string containing the smart stack trace for the given <code>throwable</code>
     */
    public static String getStackTraceString(final Throwable throwable) {
        return getStackTraceString(throwable, false, rootPackageSet, groupPackageSet, ignorePackageSet, 0);
    }

    /**
     * <p>Returns the smart stack trace for the given <code>throwable</code> using packages provided.
     *
     * @param throwable        parent throwable
     * @param rootPackageSet   root packages to use for building the stack trace
     * @param groupPackageSet  group packages to use for building the stack trace
     * @param ignorePackageSet ignore packages to use for building the stack trace
     * @return a string containing the smart stack trace for the given <code>throwable</code>
     */
    public static String getStackTraceString(final Throwable throwable, final Set<String> rootPackageSet, final Set<String> groupPackageSet, final Set<String> ignorePackageSet) {
        return getStackTraceString(throwable, false, rootPackageSet, groupPackageSet, ignorePackageSet, 0);
    }

    /**
     * <p>Returns the smart stack trace for the given <code>throwable</code> using root package provided.
     *
     * @param throwable   parent throwable
     * @param rootPackage root package to use for building the stack trace
     * @return a string containing the smart stack trace for the given <code>throwable</code>
     */
    public static String getStackTraceString(final Throwable throwable, final String rootPackage) {
        return getStackTraceString(throwable, false, Collections.singleton(rootPackage), Collections.emptySet(), Collections.emptySet(), 0);
    }

    /**
     * <p>Returns the smart stack trace for the given <code>throwable</code> using root package provided.
     *
     * @param throwable    parent throwable
     * @param rootPackage  root package to use for building the stack trace
     * @param groupPackage group package to use for building the stack trace
     * @return a string containing the smart stack trace for the given <code>throwable</code>
     */
    public static String getStackTraceString(final Throwable throwable, final String rootPackage, final String groupPackage) {
        return getStackTraceString(throwable, false, Collections.singleton(rootPackage), Collections.singleton(groupPackage), Collections.emptySet(), 0);
    }

    /**
     * <p>Returns the smart stack trace for the given <code>throwable</code> using elements found until the maxDepth.
     *
     * @param throwable parent throwable
     * @param maxDepth  max depth in exception chain that will be used
     * @return a string containing the smart stack trace for the given <code>throwable</code>
     */
    public static String getStackTraceString(final Throwable throwable, final int maxDepth) {
        return getStackTraceString(throwable, false, Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), maxDepth);
    }

    /**
     * <p>Returns the smart stack trace for the given <code>throwable</code> using packages and maxDepth provided.
     *
     * @param throwable        parent throwable
     * @param isCause          throwable is a cause or not
     * @param rootPackageSet   root packages to use for building the stack trace
     * @param groupPackageSet  group packages to use for building the stack trace
     * @param ignorePackageSet ignore packages to use for building the stack trace
     * @param maxDepth         max depth in exception chain that will be used
     * @return a string containing the smart stack trace for the given <code>throwable</code>
     */
    protected static String getStackTraceString(final Throwable throwable, final boolean isCause, final Set<String> rootPackageSet, final Set<String> groupPackageSet, final Set<String> ignorePackageSet, final int maxDepth) {
        final StringBuilder builder = new StringBuilder();

        if (throwable == null) {
            return "";
        }

        final String className = throwable.getClass().getName();

        final StackTraceElement[] stackTraceElements;
        if (maxDepth > 0) {
            stackTraceElements = getStackTrace(throwable, maxDepth);
        } else {
            stackTraceElements = getStackTrace(throwable, rootPackageSet, ignorePackageSet);
        }
        String message = throwable.getLocalizedMessage();
        if (isEmpty(message)) {
            message = throwable.getMessage();
        }

        /* PRINT MESSAGE */
        if (isCause) {
            builder.append(System.lineSeparator());
            builder.append("Caused by: ");
            builder.append(className);
            if (!isEmpty(message)) {
                builder.append(": ");
                builder.append(message);
            }
        } else {
            builder.append(System.lineSeparator());
            builder.append(className);
            if (!isEmpty(message)) {
                builder.append(": ");
                builder.append(message);
            }
        }

        /* PRINT SHORT STACK TRACE */
        String currentGroupPackage = null;
        StackTraceElement firstStackTraceElementInTheGroup = null;
        int currentGroupCount = 0;
        for (StackTraceElement traceElement : stackTraceElements) {
            String traceElementClassName = traceElement.getClassName();
            String groupPackageMatch = getContainingPackage(traceElementClassName, groupPackageSet);

            if (groupPackageMatch != null) {
                if (!groupPackageMatch.equals(currentGroupPackage)) {
                    appendStackTraceGroupElement(builder, currentGroupPackage, currentGroupCount, firstStackTraceElementInTheGroup);

                    builder.append(System.lineSeparator());
                    builder.append("\tat ");

                    currentGroupPackage = groupPackageMatch;
                    firstStackTraceElementInTheGroup = traceElement;
                    currentGroupCount = 1;
                } else {
                    currentGroupCount++;
                }
            } else {
                currentGroupCount = appendStackTraceGroupElement(builder, currentGroupPackage, currentGroupCount, firstStackTraceElementInTheGroup);

                builder.append(System.lineSeparator());
                builder.append("\tat ");
                builder.append(traceElement);
                currentGroupPackage = null;
            }
        }

        appendStackTraceGroupElement(builder, currentGroupPackage, currentGroupCount, firstStackTraceElementInTheGroup);

        /* WE ARE INTERESTED IN CAUSE TOO */
        final Throwable cause = throwable.getCause();
        if (cause != null && !containsPackage(className, ignoreCausePackageSet)) {
            builder.append(getStackTraceString(cause, true, rootPackageSet, groupPackageSet, ignorePackageSet, maxDepth));
        }

        return builder.toString();
    }

    /**
     * <p>Appends stack trace group information for the given parameters.
     *
     * @param stringBuilder                     string builder that group stack trace information will be appended
     * @param currentGroupPackage               package name of the current group
     * @param numberOfElementsInTheCurrentGroup number of elements in the current group
     * @param firstStackTraceElementInTheGroup  first stack trace element of this group
     * @return new value for the group element count
     */
    protected static int appendStackTraceGroupElement(final StringBuilder stringBuilder, final String currentGroupPackage, final int numberOfElementsInTheCurrentGroup, final StackTraceElement firstStackTraceElementInTheGroup) {
        if (numberOfElementsInTheCurrentGroup > 0) {
            stringBuilder.append((numberOfElementsInTheCurrentGroup == 1) ? firstStackTraceElementInTheGroup.toString() : String.format("%s ... %d more", currentGroupPackage, (numberOfElementsInTheCurrentGroup - 1)));
        }

        return 0;
    }

    /**
     * <p>Returns true if package of the given class name matches one of package names provided in
     * <code>packageSet</code>.
     *
     * @param fullClassName fully qualified class name
     * @param packageSet    set containing package names
     * @return true if package of the given class name matches one of package names provided in <code>packageSet</code>,
     * false otherwise
     */
    protected static boolean containsPackage(final String fullClassName, final Set<String> packageSet) {
        return getContainingPackage(fullClassName, packageSet) != null;
    }

    /**
     * <p>Checks if one of the package names in the <code>packageSet</code> matches the package of the given class name
     * and returns the matching package name.
     *
     * @param fullClassName fully qualified class name
     * @param packageSet    set containing package names
     * @return one of the package names in the <code>packageSet</code> that matches the package of the given class name
     * or null if no matching package name is found in the <code>packageSet</code>
     */
    protected static String getContainingPackage(final String fullClassName, final Set<String> packageSet) {
        for (final String parentExceptionPackage : packageSet) {
            if (fullClassName.startsWith(parentExceptionPackage)) {
                return parentExceptionPackage;
            }
        }

        return null;
    }

    /**
     * <p>Returns all messages found in the exception chain of the <code>throwable</code> as a single string.
     *
     * @param throwable parent throwable
     * @return a string containing all messages found in the exception chain
     */
    public static String getAllMessages(final Throwable throwable) {
        final StringBuilder messageBuilder = new StringBuilder();
        getAllMessages(throwable, messageBuilder);
        return messageBuilder.toString();
    }

    /**
     * <p>Adds all messages found in the exception chain of the <code>throwable</code> into the given message builder.
     *
     * @param throwable      parent throwable
     * @param messageBuilder message builder
     */
    protected static void getAllMessages(final Throwable throwable, final StringBuilder messageBuilder) {
        if (throwable != null) {
            final String message = throwable.getMessage();
            if (!isEmpty(message)) {
                if (messageBuilder.length() != 0) {
                    messageBuilder.append(System.lineSeparator());
                    messageBuilder.append(" - Caused by: ");
                }
                messageBuilder.append(message);
            }
            getAllMessages(throwable.getCause(), messageBuilder);
        }
    }

    /**
     * <p>Builds a smart stack trace for the given <code>throwable</code> using elements found until the maxDepth and
     * elements of it.
     *
     * @param throwable parent throwable
     * @param maxDepth  max depth in exception chain that will be returned
     * @return an array containing stack trace elements
     */
    protected static StackTraceElement[] getStackTrace(final Throwable throwable, final int maxDepth) {
        final ArrayList<StackTraceElement> list = new ArrayList<>();

        if (throwable != null) {
            StackTraceElement[] stackTrace = throwable.getStackTrace();
            for (int i = 0; (i < stackTrace.length) && (i < maxDepth); i++) {
                list.add(stackTrace[i]);
            }
        }

        return list.toArray(new StackTraceElement[0]);
    }

    /**
     * <p>Builds a smart stack trace for the given <code>throwable</code> using packages provided and returns elements
     * of it.
     *
     * @param throwable        parent throwable
     * @param rootPackageSet   root packages to use for building the stack trace
     * @param ignorePackageSet ignore packages to use for building the stack trace
     * @return an array containing stack trace elements
     */
    protected static StackTraceElement[] getStackTrace(final Throwable throwable, final Set<String> rootPackageSet, final Set<String> ignorePackageSet) {
        final ArrayList<StackTraceElement> list = new ArrayList<>();
        final ArrayList<StackTraceElement> partialList = new ArrayList<>();

        if (throwable != null) {
            Arrays.stream(throwable.getStackTrace()).forEachOrdered(
                    stackTraceElement -> {
                        final String className = stackTraceElement.getClassName();
                        if (!isEmpty(className)) {
                            if (containsPackage(className, rootPackageSet)) {
                                list.addAll(partialList);
                                list.add(stackTraceElement);
                            } else if (!containsPackage(className, ignorePackageSet)) {
                                partialList.add(stackTraceElement);
                            }
                        }
                    }
            );
        }

        if (list.isEmpty()) {
            list.addAll(partialList);
        }

        return list.toArray(new StackTraceElement[0]);
    }

    /**
     * <p>Returns true if the given cause class is found in the exception chain of the <code>throwable</code>.
     * <br/>
     * <br>
     * <p>Search starts from the <code>throwable</code> itself and goes through the exception chain up until
     * {@link #DEFAULT_MAX_DEPTH}.
     *
     * @param throwable  parent throwable
     * @param causeClass class to search for
     * @return true if the given cause class is found in the exception chain of the <code>throwable</code>, false
     * otherwise
     */
    public static boolean containsCause(final Throwable throwable, final Class<?> causeClass) {
        return containsCause(throwable, causeClass, null);
    }

    /**
     * <p>Returns true if the given cause class and cause message is found in the exception chain of the
     * <code>throwable</code>.
     * <br/>
     * <br>
     * <p>Search starts from the <code>throwable</code> itself and goes through the exception chain up until
     * {@link #DEFAULT_MAX_DEPTH}.
     *
     * @param throwable    parent throwable
     * @param causeClass   class to search for
     * @param causeMessage message to search for
     * @return true if the given cause class and cause message is found in the exception chain of the
     * <code>throwable</code>, false otherwise
     */
    public static boolean containsCause(final Throwable throwable, final Class<?> causeClass, final String causeMessage) {
        return (searchCause(throwable, causeClass, causeMessage, DEFAULT_MAX_DEPTH) != null);
    }

    /**
     * <p>Returns the cause of the <code>throwable</code> by walking through the exception chain.
     *
     * @param throwable parent throwable
     * @return the cause of the <code>throwable</code> found or null if <code>throwable</code> does not have a cause
     */
    public static Throwable getCause(final Throwable throwable) {
        return getCause(throwable, DEFAULT_MAX_DEPTH);
    }

    /**
     * <p>Returns the cause of the <code>throwable</code> by walking through the exception chain.
     * <br/>
     * <br>
     * <p>Note that this method walks through the exception chain up to given <code>maxDepth</code>. If exception chain
     * includes more items than <code>maxDepth</code>, cause found at <code>maxDepth</code> level is returned.
     *
     * @param throwable parent throwable
     * @param maxDepth  max depth in exception chain that will be searched
     * @return the cause of the <code>throwable</code> found until <code>maxDepth</code> is reached in the exception
     * chain or null if <code>throwable</code> does not have a cause
     */
    public static Throwable getCause(final Throwable throwable, final int maxDepth) {
        if (throwable == null) {
            return null;
        }

        if (maxDepth <= 0) {
            return throwable;
        }

        final Throwable cause = throwable.getCause();
        if (cause == null) {
            return throwable;
        } else {
            return getCause(cause, maxDepth - 1);
        }
    }

    /**
     * <p>Searches for the given cause class in the exception chain of the <code>throwable</code>.
     * <br/>
     * <br>
     * <p>Search starts from the <code>throwable</code> itself and goes through the exception chain up until
     * {@link #DEFAULT_MAX_DEPTH}.
     *
     * @param throwable  parent throwable
     * @param causeClass class to search for
     * @return the <code>throwable</code> found or null if no class in exception chain matches the given cause class
     */
    public static Throwable searchCause(final Throwable throwable, final Class<?> causeClass) {
        return searchCause(throwable, causeClass, null, DEFAULT_MAX_DEPTH);
    }

    /**
     * <p>Searches for the given cause class and cause message in the exception chain of the <code>throwable</code>.
     * <br/>
     * <br>
     * <p>Search starts from the <code>throwable</code> itself and goes through the exception chain up until
     * {@link #DEFAULT_MAX_DEPTH}.
     *
     * @param throwable    parent throwable
     * @param causeClass   class to search for
     * @param causeMessage message to search for
     * @return the <code>throwable</code> found or null if no class in exception chain matches the given cause class
     * and cause message
     */
    public static Throwable searchCause(final Throwable throwable, final Class<?> causeClass, final String causeMessage) {
        return searchCause(throwable, causeClass, causeMessage, DEFAULT_MAX_DEPTH);
    }

    /**
     * <p>Searches for the given cause class and cause message in the exception chain of the <code>throwable</code>.
     * <br/>
     * <br>
     * <p>Search starts from the <code>throwable</code> itself and goes through the exception chain up until the given
     * <code>maxDepth</code>.
     *
     * @param throwable    parent throwable
     * @param causeClass   class to search for
     * @param causeMessage message to search for
     * @param maxDepth     max depth in exception chain that will be searched
     * @return the <code>throwable</code> found or null if no class in exception chain matches the given cause class
     * and cause message
     */
    public static Throwable searchCause(final Throwable throwable, final Class<?> causeClass, final String causeMessage, final int maxDepth) {
        if (throwable == null) {
            return null;
        }

        if (isEmpty(causeMessage)) {
            if (throwable.getClass().equals(causeClass)) {
                return throwable;
            }
        } else {
            if (throwable.getClass().equals(causeClass) && getAllMessages(throwable).toLowerCase().contains(causeMessage.toLowerCase())) {
                return throwable;
            }
        }

        if (maxDepth <= 0) {
            return null;
        }

        final Throwable cause = throwable.getCause();
        if (cause == null) {
            return null;
        } else {
            return searchCause(cause, causeClass, causeMessage, maxDepth - 1);
        }
    }

    /**
     * <p>Searches for the given cause class in the exception chain of the <code>throwable</code>.
     * <br/>
     * <br>
     * <p>Search starts from the <code>throwable</code> itself and goes through the exception chain up until the given
     * <code>maxDepth</code>.
     *
     * @param throwable  parent throwable
     * @param causeClass class to search for
     * @param maxDepth   max depth in exception chain that will be searched
     * @return the <code>throwable</code> found or null if no class in exception chain matches the given cause class
     */
    public static Throwable searchCause(final Throwable throwable, final Class<?> causeClass, final int maxDepth) {
        if (throwable == null) {
            return null;
        }

        if (throwable.getClass().equals(causeClass)) {
            return throwable;
        }

        if (maxDepth <= 0) {
            return null;
        }

        final Throwable cause = throwable.getCause();
        if (cause == null) {
            return null;
        } else {
            return searchCause(cause, causeClass, maxDepth - 1);
        }
    }

    /**
     * <p>Checks if given string is empty or not.
     *
     * @param value string to check
     * @return true if given string is null or has zero length after trim(), false otherwise
     */
    protected static boolean isEmpty(final String value) {
        if (value == null) {
            return true;
        }

        return value.trim().length() == 0;
    }

    /**
     * <p>Returns the package name of this class.
     *
     * @param className fully qualified class name
     * @return name of the package
     */
    protected static String packageName(final String className) {
        if (className == null) {
            return "";
        }

        int index = className.lastIndexOf(".");
        if (index >= 0) {
            return className.substring(0, index);
        } else {
            return "";
        }
    }

}
