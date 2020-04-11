# SmartException ![GitHub release](https://img.shields.io/badge/release-v0.1.0-blue.svg) ![Bintray](https://img.shields.io/badge/bintray-v0.1.0-blue.svg) [![Build Status](https://travis-ci.org/tanersener/smart-exception.svg?branch=master)](https://travis-ci.org/tanersener/smart-exception)
Utilities to handle Throwable objects and stack trace elements

<img src="https://github.com/tanersener/smart-exception/blob/master/docs/assets/smart-exception-logo-v2.png" width="300">

### 1. Features
- Build shorter stack traces 
    - Start exception chain from a root package
    - Group stack trace elements from the same package
    - Ignore stack trace elements
    - Ignore causes
- Customise how a stack trace element will be printed
- Do not include module name while printing a stack trace element
- Define how many stack trace elements will be printed
- Define how native methods and closed source stack trace elements will be printed
- Search for a cause in an exception chain
- Get the cause of a throwable

### 2. Using

#### 2.1 Java 9 or later
1. Add SmartException dependency to your `build.gradle`
    ```
    dependencies {
        implementation 'com.arthenica:smart-exception:0.1.0'
    }
    ```

#### 2.2 Java 7/8 or Android
1. Add SmartException dependency to your `build.gradle`
    ```
    dependencies {
        implementation 'com.arthenica:smart-exception-compat:0.1.0'
    }
    ```

### 3. Versions

### 4. Building
#### 4.1 Prerequisites

- Java 9 or later

#### 4.2 Prerequisites

### 5. License

This project is licensed under `BSD 3-Clause License`

### 6. Contributing

If you have any recommendations or ideas to improve it, please feel free to submit issues or pull requests. 
