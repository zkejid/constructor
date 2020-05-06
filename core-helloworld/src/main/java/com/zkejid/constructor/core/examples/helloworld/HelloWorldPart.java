package com.zkejid.constructor.core.examples.helloworld;

import com.zkejid.constructor.core.api.v1.ConstructionException;
import com.zkejid.constructor.core.api.v1.ConstructorPart;
import com.zkejid.constructor.core.api.v1.CoreLogging;
import com.zkejid.constructor.core.api.v1.EntryPoint;
import com.zkejid.constructor.javasdk.system.api.v1.System;

import java.util.Collections;
import java.util.Set;

public class HelloWorldPart implements ConstructorPart, EntryPoint, CoreLogging {

    private System system;

    @Override
    public Set<Class<?>> getInterfacesNecessary() {
        return Collections.singleton(System.class);
    }

    @Override
    public Set<Class<?>> getInterfacesProvided() {
        return Set.of(EntryPoint.class, CoreLogging.class);
    }

    @Override
    public Object getImplementation(Class<?> anInterface) {
        if (EntryPoint.class.equals(anInterface) || CoreLogging.class.equals(anInterface)) {
            return this;
        } else {
            throw new ConstructionException("Part does not provide " + anInterface);
        }
    }

    @Override
    public void putImplementation(Class<?> interfaceNecessary, Object ... implementations) {
        if (implementations.length != 1) {
            throw new ConstructionException(
                "Expect exactly one implementation of " + interfaceNecessary
                    + ". Got " + implementations.length
            );
        }
        if (System.class.equals(interfaceNecessary)) {
            system = (System) implementations[0];
        }
    }

    @Override
    public void verifyNecessaryInterfaces() throws ConstructionException {
        if (system == null) {
            throw new ConstructionException("System interface is not provided");
        }
    }

    @Override
    public void main(String[] args) {
        system.out().println("Hello world!");
    }

    @Override
    public void log(String message) {
        system.out().println(message);
    }
}
