# Constructor Core module
## Overview
Constructor is a framework for development of modular applications. The idea behind it is simple: split the whole application on separate modules linked through well documented and properly tested interfaces. 

Each module of an application defines two sets of interfaces. One set consists of all interface classes given module depends on. Other set consists of interface classes given module provides. Core module responsible fo detection of modules, their instantiation, providing dependencies, calling entry point code and executing some other infrastructural tasks.

Recommended way to organize code in project is as follows. Each module should be provided as a separate set of artifacts. Module project should contain: artifact containing API provided, implementation artifact, unit tests over implementation, test application, integration tests over API.

## Benefits
* Constructor Framework supports and powers up test driven development in the following way: each interaction does through an interface, which means you can set any implementation you need.
* Framework forces developer to use modular way of thoughts. Each module has clear and well described purpose. 
* The set of infrastructural classes in Java SDK has their own interfaces. Usage of these interfaces allows developer to make more clear tests on interaction with infrastructure: file system, JMX, System class, System Environment variables, and so on.

## Similar frameworks
* Any Dependency Injection (DI) framework: Spring, Guice, Dagger, etc.  Constructor Framework implements [Dependency injection](https://en.wikipedia.org/wiki/Dependency_injection) on the level of modules.
* Plugin frameworks like PF4J. Core module does not support the dynamic plugin loadig, but has similar behavior on static loading searching modules in classpath.

## Versioning
Artifact versions of the Core Module follow the [Semantic Versioning 2.0.0](https://semver.org/spec/v2.0.0.html) specification.

## Logging
Core classes use framework's own built-in interface for logging. Application should define an implementation of this interface in order to achieve logging for core classes. Implementation should be made in the form of Constructor Framework module providing implementation of ```CoreLogging.java``` interface. 