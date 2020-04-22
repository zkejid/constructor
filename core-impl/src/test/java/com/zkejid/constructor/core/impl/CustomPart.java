package com.zkejid.constructor.core.impl;

import com.zkejid.constructor.core.api.v1.ConstructionException;
import com.zkejid.constructor.core.api.v1.ConstructorPart;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of {@link ConstructorPart} interface with fully modifiable behaviour:
 * <ul>
 *     <li>Set interfaces necessary.
 *     <li>Get the set of interfaces and implementations provided to this part during construction
 *     <li>Set interfaces provided.
 *     <li>Set implementations provided.
 * </ul>
 */
class CustomPart implements ConstructorPart {

    private final Set<Class<?>> interfacesNecessary;
    private final Set<Class<?>> interfacesProvided;
    private final Map<Class<?>, Object> implementationsNecessary;
    private final Map<Class<?>, Object> implementationsProvided;

    public CustomPart(
            Set<Class<?>> interfacesNecessary,
            Set<Class<?>> interfacesProvided,
            Map<Class<?>, Object> implementationsProvided) {
        this.interfacesNecessary = interfacesNecessary;
        this.interfacesProvided = interfacesProvided;
        this.implementationsNecessary = new HashMap<>();
        this.implementationsProvided = implementationsProvided;
    }

    @Override
    public Set<Class<?>> getInterfacesNecessary() {
        return interfacesNecessary;
    }

    @Override
    public Set<Class<?>> getInterfacesProvided() {
        return interfacesProvided;
    }

    @Override
    public Object getImplementation(Class<?> anInterface) {
        return implementationsProvided.get(anInterface);
    }

    @Override
    public void putImplementation(Class<?> interfaceNecessary, Object implementation) {
        implementationsNecessary.put(interfaceNecessary, implementation);
    }

    @Override
    public void verifyNecessaryInterfaces() throws ConstructionException {}

    public Map<Class<?>, Object> getImplementationsNecessary() {
        return implementationsNecessary;
    }
}
