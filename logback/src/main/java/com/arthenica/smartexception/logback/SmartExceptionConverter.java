/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2021-2022, Taner Sener
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

package com.arthenica.smartexception.logback;

import ch.qos.logback.classic.pattern.ThrowableHandlingConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.CoreConstants;
import com.arthenica.smartexception.AbstractExceptions;
import com.arthenica.smartexception.java9.Java9StackTraceElementSerializer;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>Converts throwables into strings using smart-exception-java9.
 *
 * @author Taner Sener
 * @since 0.2
 */
public class SmartExceptionConverter extends ThrowableHandlingConverter {

    public static final String OPTION_VALUE_ROOT_PACKAGE = "rootPackage";

    public static final String OPTION_VALUE_GROUP_PACKAGE = "groupPackage";

    public static final String OPTION_VALUE_IGNORE_PACKAGE = "ignorePackage";

    public static final String OPTION_VALUE_IGNORE_CAUSES = "ignoreCauses";

    public static final String OPTION_VALUE_PRINT_MODULE_NAME = "printModuleName";

    public static final String OPTION_VALUE_MAX_DEPTH = "maxDepth";

    public static final String OPTION_VALUE_PRINT_PACKAGE_INFORMATION = "printPackageInformation";

    private Set<String> rootPackages = new LinkedHashSet<>();

    private Set<String> groupPackages = new LinkedHashSet<>();

    private Set<String> ignorePackages = new LinkedHashSet<>();

    private boolean ignoreCauses = AbstractExceptions.DEFAULT_IGNORE_ALL_CAUSES;

    private boolean printModuleName = AbstractExceptions.DEFAULT_PRINT_MODULE_NAME;

    private int maxDepth = 0;

    private boolean printPackageInformation = AbstractExceptions.DEFAULT_PRINT_PACKAGE_INFORMATION;

    static {
        AbstractExceptions.setStackTraceElementSerializer(new Java9StackTraceElementSerializer());
    }

    public void start() {
        parseOptions();
        super.start();
    }

    private void parseOptions() {
        List<String> optionList = getOptionList();
        if (optionList == null) {
            return;
        }
        for (String option : optionList) {
            parseOption(option);
        }
    }

    private void parseOption(final String option) {
        String[] split = option.split("=");
        if (split.length == 2) {
            try {
                switch (split[0]) {
                    case OPTION_VALUE_ROOT_PACKAGE: {
                        rootPackages.add(split[1]);
                    }
                    break;
                    case OPTION_VALUE_GROUP_PACKAGE: {
                        groupPackages.add(split[1]);
                    }
                    break;
                    case OPTION_VALUE_IGNORE_PACKAGE: {
                        ignorePackages.add(split[1]);
                    }
                    break;
                    case OPTION_VALUE_IGNORE_CAUSES: {
                        ignoreCauses = parseBooleanOption(split[1]);
                    }
                    break;
                    case OPTION_VALUE_PRINT_MODULE_NAME: {
                        printModuleName = parseBooleanOption(split[1]);
                    }
                    break;
                    case OPTION_VALUE_MAX_DEPTH: {
                        maxDepth = parseIntegerOption(split[1]);
                    }
                    break;
                    case OPTION_VALUE_PRINT_PACKAGE_INFORMATION: {
                        printPackageInformation = parseBooleanOption(split[1]);
                    }
                    break;
                    default: {
                        System.out.printf("Unsupported SmartExceptionConverter option: %s%n", option);
                    }
                }
            } catch (final NumberFormatException e) {
                System.out.printf("Failed to set SmartExceptionConverter option: %s%n", option);
                e.printStackTrace();
            }
        } else {
            System.out.printf("Invalid SmartExceptionConverter option: %s%n", option);
        }
    }

    private int parseIntegerOption(final String optionValue) {
        return Integer.parseInt(optionValue);
    }

    private boolean parseBooleanOption(final String optionValue) {
        return Boolean.parseBoolean(optionValue);
    }

    public void addRootPackage(final String rootPackage) {
        this.rootPackages.add(rootPackage);
    }

    public Set<String> getRootPackages() {
        return rootPackages;
    }

    public void setRootPackages(final Set<String> rootPackages) {
        this.rootPackages = rootPackages;
    }

    public void addGroupPackage(final String groupPackage) {
        this.groupPackages.add(groupPackage);
    }

    public Set<String> getGroupPackages() {
        return groupPackages;
    }

    public void setGroupPackages(final Set<String> groupPackages) {
        this.groupPackages = groupPackages;
    }

    public void addIgnorePackage(final String ignorePackage) {
        this.ignorePackages.add(ignorePackage);
    }

    public Set<String> getIgnorePackages() {
        return ignorePackages;
    }

    public void setIgnorePackages(final Set<String> ignorePackages) {
        this.ignorePackages = ignorePackages;
    }

    public boolean isIgnoreCauses() {
        return ignoreCauses;
    }

    public void setIgnoreCauses(boolean ignoreCauses) {
        this.ignoreCauses = ignoreCauses;
    }

    public boolean isPrintModuleName() {
        return printModuleName;
    }

    public void setPrintModuleName(boolean printModuleName) {
        this.printModuleName = printModuleName;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public boolean isPrintPackageInformation() {
        return printPackageInformation;
    }

    public void setPrintPackageInformation(boolean printPackageInformation) {
        this.printPackageInformation = printPackageInformation;
    }

    @Override
    public String convert(ILoggingEvent event) {
        final IThrowableProxy throwableProxy = event.getThrowableProxy();
        if (throwableProxy == null) {
            return CoreConstants.EMPTY_STRING;
        }

        return AbstractExceptions.getStackTraceString(ThrowableWrapperConverter.toThrowableWrapper(throwableProxy), false, rootPackages, groupPackages, ignorePackages, printModuleName, maxDepth, ignoreCauses, printPackageInformation);
    }

}
