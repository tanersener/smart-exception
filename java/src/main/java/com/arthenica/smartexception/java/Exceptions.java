/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020-2021, Taner Sener
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

package com.arthenica.smartexception.java;

import com.arthenica.smartexception.ClassLoader;
import com.arthenica.smartexception.*;

import java.util.Set;

/**
 * <p>Utility class to handle Throwable objects and stack traces. This implementation is compatible with all Java and
 * Android versions.
 *
 * @author Taner Sener
 * @since 0.1.0
 */
public class Exceptions {

    static {
        packageLoader = new JavaPackageLoader();
        classLoader = new JavaClassLoader();

        AbstractExceptions.setStackTraceElementSerializer(new JavaStackTraceElementSerializer());
    }

    static PackageLoader packageLoader;

    static ClassLoader classLoader;

    /**
     * <p>Registers a new root package.
     *
     * @param packageString root package name to register
     */
    public static void registerRootPackage(final String packageString) {
        AbstractExceptions.registerRootPackage(packageString);
    }

    /**
     * <p>Clears previously registered root packages.
     */
    public static void clearRootPackages() {
        AbstractExceptions.clearRootPackages();
    }

    /**
     * <p>Registers a new group package.
     *
     * @param packageString group package name to register
     */
    public static void registerGroupPackage(final String packageString) {
        AbstractExceptions.registerGroupPackage(packageString);
    }

    /**
     * <p>Clears previously registered group packages.
     */
    public static void clearGroupPackages() {
        AbstractExceptions.clearGroupPackages();
    }

    /**
     * <p>Returns the global stack trace serializer implementation which is used to serialize {@link StackTraceElement}
     * objects in <code>getStackTraceString</code> methods.
     *
     * @return current stack trace serializer implementation
     */
    public static StackTraceElementSerializer getStackTraceElementSerializer() {
        return AbstractExceptions.getStackTraceElementSerializer();
    }

    /**
     * <p>Sets the global stack trace serializer implementation which is used to serialize {@link StackTraceElement}
     * objects in <code>getStackTraceString</code> methods.
     *
     * @param stackTraceElementSerializer new stack trace serializer implementation
     */
    public static void setStackTraceElementSerializer(final StackTraceElementSerializer stackTraceElementSerializer) {
        AbstractExceptions.setStackTraceElementSerializer(stackTraceElementSerializer);
    }

    /**
     * <p>Registers a new ignore package.
     *
     * @param packageString      ignore package name to register
     * @param ignoreCauseClasses ignore cause classes from this package too
     */
    public static void registerIgnorePackage(final String packageString, final boolean ignoreCauseClasses) {
        AbstractExceptions.registerIgnorePackage(packageString, ignoreCauseClasses);
    }

    /**
     * <p>Clears previously registered ignore packages.
     */
    public static void clearIgnorePackages() {
        AbstractExceptions.clearIgnorePackages();
    }

    /**
     * <p>Returns the value of ignore all causes option.
     *
     * @return the value of global ignore all causes option. If value is true then stack trace elements printed or
     * converted to string will not include causes. If value is false causes will be appended to the stack trace of
     * the main throwable.
     */
    public static boolean getIgnoreAllCauses() {
        return AbstractExceptions.getIgnoreAllCauses();
    }

    /**
     * <p>Sets the value of ignore all causes option.
     *
     * @param ignoreAllCauses new global ignore all causes option. If value is true then stack trace elements printed
     *                        or converted to string will not include causes. If value is false causes will be appended to the stack trace of
     *                        the main throwable.
     */
    public static void setIgnoreAllCauses(final boolean ignoreAllCauses) {
        AbstractExceptions.setIgnoreAllCauses(ignoreAllCauses);
    }

    /**
     * <p>Returns the value of print package information option.
     *
     * @return the value of global print package information option. When this option is true, stack trace elements
     * printed or converted to string will include the name of the jar file that includes the printed class and the version
     * of the jar. If it is false, none of this information is printed
     */
    public static boolean isPrintPackageInformation() {
        return AbstractExceptions.isPrintPackageInformation();
    }

    /**
     * <p>Sets the value of print package information option.
     *
     * <p>When this option is true, stack trace elements printed or converted to string will include the name of the
     * jar file that includes the printed class and the version of the jar.
     *
     * <p>Note that for some libraries extracting the jar file and the version may not be possible.
     *
     * @param printPackageInformation new print package information option.
     */
    public static void setPrintPackageInformation(final boolean printPackageInformation) {
        AbstractExceptions.setPrintPackageInformation(printPackageInformation);
    }

    /**
     * <p>Returns the value of print suppressed exceptions option.
     *
     * @return the value of global print suppressed exceptions option
     */
    public static boolean getPrintSuppressedExceptions() {
        return AbstractExceptions.getPrintSuppressedExceptions();
    }

    /**
     * <p>Sets the value of print suppressed exceptions option.
     *
     * @param printSuppressedExceptions new global print suppressed exceptions option
     */
    public static void setPrintSuppressedExceptions(final boolean printSuppressedExceptions) {
        AbstractExceptions.setPrintSuppressedExceptions(printSuppressedExceptions);
    }

    /**
     * <p>Returns the smart stack trace for the given <code>throwable</code>.
     *
     * <p>This method uses root packages registered by {@link #registerRootPackage(String)}, group packages registered
     * by {@link #registerGroupPackage(String)} and ignore packages registered by
     * {@link #registerIgnorePackage(String, boolean)} to build the smart stack trace.
     *
     * @param throwable parent throwable
     * @return a string containing the smart stack trace for the given <code>throwable</code>
     */
    public static String getStackTraceString(final Throwable throwable) {
        return AbstractExceptions.getStackTraceString(new ThrowableWrapper(throwable));
    }

    /**
     * <p>Returns the smart stack trace for the given <code>throwable</code>.
     *
     * <p>This method uses root packages registered by {@link #registerRootPackage(String)}, group packages registered
     * by {@link #registerGroupPackage(String)} and ignore packages registered by
     * {@link #registerIgnorePackage(String, boolean)} to build the smart stack trace.
     *
     * @param throwable       parent throwable
     * @param ignoreAllCauses ignore all causes in the exception chain
     * @return a string containing the smart stack trace for the given <code>throwable</code>
     */
    public static String getStackTraceString(final Throwable throwable, final boolean ignoreAllCauses) {
        return AbstractExceptions.getStackTraceString(new ThrowableWrapper(throwable), ignoreAllCauses);
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
        return AbstractExceptions.getStackTraceString(new ThrowableWrapper(throwable), rootPackageSet, groupPackageSet, ignorePackageSet);
    }

    /**
     * <p>Returns the smart stack trace for the given <code>throwable</code> using packages provided.
     *
     * @param throwable        parent throwable
     * @param rootPackageSet   root packages to use for building the stack trace
     * @param groupPackageSet  group packages to use for building the stack trace
     * @param ignorePackageSet ignore packages to use for building the stack trace
     * @param ignoreAllCauses  ignore all causes in the exception chain
     * @return a string containing the smart stack trace for the given <code>throwable</code>
     */
    public static String getStackTraceString(final Throwable throwable, final Set<String> rootPackageSet, final Set<String> groupPackageSet, final Set<String> ignorePackageSet, final boolean ignoreAllCauses) {
        return AbstractExceptions.getStackTraceString(new ThrowableWrapper(throwable), rootPackageSet, groupPackageSet, ignorePackageSet, ignoreAllCauses);
    }

    /**
     * <p>Returns the smart stack trace for the given <code>throwable</code> using packages provided.
     *
     * @param throwable               parent throwable
     * @param rootPackageSet          root packages to use for building the stack trace
     * @param groupPackageSet         group packages to use for building the stack trace
     * @param ignorePackageSet        ignore packages to use for building the stack trace
     * @param ignoreAllCauses         ignore all causes in the exception chain
     * @param printPackageInformation print package information
     * @return a string containing the smart stack trace for the given <code>throwable</code>
     */
    public static String getStackTraceString(final Throwable throwable, final Set<String> rootPackageSet, final Set<String> groupPackageSet, final Set<String> ignorePackageSet, final boolean ignoreAllCauses, final boolean printPackageInformation) {
        return AbstractExceptions.getStackTraceString(new ThrowableWrapper(throwable), rootPackageSet, groupPackageSet, ignorePackageSet, ignoreAllCauses, printPackageInformation);
    }

    /**
     * <p>Returns the smart stack trace for the given <code>throwable</code> using packages provided.
     *
     * @param throwable                 parent throwable
     * @param rootPackageSet            root packages to use for building the stack trace
     * @param groupPackageSet           group packages to use for building the stack trace
     * @param ignorePackageSet          ignore packages to use for building the stack trace
     * @param ignoreAllCauses           ignore all causes in the exception chain
     * @param printPackageInformation   print package information
     * @param printSuppressedExceptions print suppressed exceptions
     * @return a string containing the smart stack trace for the given <code>throwable</code>
     */
    public static String getStackTraceString(final Throwable throwable, final Set<String> rootPackageSet, final Set<String> groupPackageSet, final Set<String> ignorePackageSet, final boolean ignoreAllCauses, final boolean printPackageInformation, final boolean printSuppressedExceptions) {
        return AbstractExceptions.getStackTraceString(new ThrowableWrapper(throwable), rootPackageSet, groupPackageSet, ignorePackageSet, ignoreAllCauses, printPackageInformation, printSuppressedExceptions);
    }

    /**
     * <p>Returns the smart stack trace for the given <code>throwable</code> using root package provided.
     *
     * @param throwable   parent throwable
     * @param rootPackage root package to use for building the stack trace
     * @return a string containing the smart stack trace for the given <code>throwable</code>
     */
    public static String getStackTraceString(final Throwable throwable, final String rootPackage) {
        return AbstractExceptions.getStackTraceString(new ThrowableWrapper(throwable), rootPackage);
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
        return AbstractExceptions.getStackTraceString(new ThrowableWrapper(throwable), rootPackage, groupPackage);
    }

    /**
     * <p>Returns the smart stack trace for the given <code>throwable</code> using elements found until the maxDepth.
     *
     * @param throwable parent throwable
     * @param maxDepth  max depth in exception chain that will be used
     * @return a string containing the smart stack trace for the given <code>throwable</code>
     */
    public static String getStackTraceString(final Throwable throwable, final int maxDepth) {
        return AbstractExceptions.getStackTraceString(new ThrowableWrapper(throwable), maxDepth);
    }

    /**
     * <p>Returns the smart stack trace for the given <code>throwable</code> using elements found until the maxDepth.
     *
     * @param throwable       parent throwable
     * @param maxDepth        max depth in exception chain that will be used
     * @param ignoreAllCauses ignore all causes in the exception chain
     * @return a string containing the smart stack trace for the given <code>throwable</code>
     */
    public static String getStackTraceString(final Throwable throwable, final int maxDepth, final boolean ignoreAllCauses) {
        return AbstractExceptions.getStackTraceString(new ThrowableWrapper(throwable), maxDepth, ignoreAllCauses);
    }

    /**
     * <p>Returns the smart stack trace for the given <code>throwable</code> using elements found until the maxDepth.
     *
     * @param throwable               parent throwable
     * @param maxDepth                max depth in exception chain that will be used
     * @param ignoreAllCauses         ignore all causes in the exception chain
     * @param printPackageInformation print package information
     * @return a string containing the smart stack trace for the given <code>throwable</code>
     */
    public static String getStackTraceString(final Throwable throwable, final int maxDepth, final boolean ignoreAllCauses, final boolean printPackageInformation) {
        return AbstractExceptions.getStackTraceString(new ThrowableWrapper(throwable), maxDepth, ignoreAllCauses, printPackageInformation);
    }

    /**
     * <p>Returns the smart stack trace for the given <code>throwable</code> using elements found until the maxDepth.
     *
     * @param throwable                 parent throwable
     * @param maxDepth                  max depth in exception chain that will be used
     * @param ignoreAllCauses           ignore all causes in the exception chain
     * @param printPackageInformation   print package information
     * @param printSuppressedExceptions print suppressed exceptions
     * @return a string containing the smart stack trace for the given <code>throwable</code>
     */
    public static String getStackTraceString(final Throwable throwable, final int maxDepth, final boolean ignoreAllCauses, final boolean printPackageInformation, final boolean printSuppressedExceptions) {
        return AbstractExceptions.getStackTraceString(new ThrowableWrapper(throwable), maxDepth, ignoreAllCauses, printPackageInformation, printSuppressedExceptions);
    }

    /**
     * <p>Returns all messages found in the exception chain of the <code>throwable</code> as a single string.
     *
     * @param throwable parent throwable
     * @return a string containing all messages found in the exception chain
     */
    public static String getAllMessages(final Throwable throwable) {
        return AbstractExceptions.getAllMessages(throwable);
    }

    /**
     * <p>Returns true if the given cause class is found in the exception chain of the <code>throwable</code>.
     * <br>
     * <br>
     * <p>Search starts from the <code>throwable</code> itself and goes through the exception chain up until
     * {@link AbstractExceptions#DEFAULT_MAX_DEPTH}.
     *
     * @param throwable  parent throwable
     * @param causeClass class to search for
     * @return true if the given cause class is found in the exception chain of the <code>throwable</code>, false
     * otherwise
     */
    public static boolean containsCause(final Throwable throwable, final Class<?> causeClass) {
        return AbstractExceptions.containsCause(throwable, causeClass);
    }

    /**
     * <p>Returns true if the given cause class and cause message is found in the exception chain of the
     * <code>throwable</code>.
     * <br>
     * <br>
     * <p>Search starts from the <code>throwable</code> itself and goes through the exception chain up until
     * {@link AbstractExceptions#DEFAULT_MAX_DEPTH}.
     *
     * @param throwable    parent throwable
     * @param causeClass   class to search for
     * @param causeMessage message to search for
     * @return true if the given cause class and cause message is found in the exception chain of the
     * <code>throwable</code>, false otherwise
     */
    public static boolean containsCause(final Throwable throwable, final Class<?> causeClass, final String causeMessage) {
        return AbstractExceptions.containsCause(throwable, causeClass, causeMessage);
    }

    /**
     * <p>Returns the cause of the <code>throwable</code> by walking through the exception chain.
     *
     * @param throwable parent throwable
     * @return the cause of the <code>throwable</code> found or null if <code>throwable</code> does not have a cause
     */
    public static Throwable getCause(final Throwable throwable) {
        return AbstractExceptions.getCause(throwable);
    }

    /**
     * <p>Returns the cause of the <code>throwable</code> by walking through the exception chain.
     * <br>
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
        return AbstractExceptions.getCause(throwable, maxDepth);
    }

    /**
     * <p>Searches for the given cause class in the exception chain of the <code>throwable</code>.
     * <br>
     * <br>
     * <p>Search starts from the <code>throwable</code> itself and goes through the exception chain up until
     * {@link AbstractExceptions#DEFAULT_MAX_DEPTH}.
     *
     * @param throwable  parent throwable
     * @param causeClass class to search for
     * @return the <code>throwable</code> found or null if no class in exception chain matches the given cause class
     */
    public static Throwable searchCause(final Throwable throwable, final Class<?> causeClass) {
        return AbstractExceptions.searchCause(throwable, causeClass);
    }

    /**
     * <p>Searches for the given cause class and cause message in the exception chain of the <code>throwable</code>.
     * <br>
     * <br>
     * <p>Search starts from the <code>throwable</code> itself and goes through the exception chain up until
     * {@link AbstractExceptions#DEFAULT_MAX_DEPTH}.
     *
     * @param throwable    parent throwable
     * @param causeClass   class to search for
     * @param causeMessage message to search for
     * @return the <code>throwable</code> found or null if no class in exception chain matches the given cause class
     * and cause message
     */
    public static Throwable searchCause(final Throwable throwable, final Class<?> causeClass, final String causeMessage) {
        return AbstractExceptions.searchCause(throwable, causeClass, causeMessage);
    }

    /**
     * <p>Searches for the given cause class and cause message in the exception chain of the <code>throwable</code>.
     * <br>
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
        return AbstractExceptions.searchCause(throwable, causeClass, causeMessage, maxDepth);
    }

    /**
     * <p>Searches for the given cause class in the exception chain of the <code>throwable</code>.
     * <br>
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
        return AbstractExceptions.searchCause(throwable, causeClass, maxDepth);
    }

}
