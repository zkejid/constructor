package com.zkejid.constructor.core.impl;

import com.zkejid.constructor.core.api.v1.ConstructorPart;

import java.util.*;

/**
 * Service class to manage the catalog of application parts and their dependencies.
 */
public class Librarium {

    private Map<String, List<Object>> catalog = new HashMap<>();

    /**
     * Reads all dependencies of given parts list.
     */
    void makeCatalog(List<ConstructorPart> initializationList) {
        final Map<String, List<Object>> implementationCatalog = new HashMap<>();
        for (ConstructorPart part: initializationList) {
            for (Class<?> anInterface : part.getInterfacesProvided()) {
                final String interfaceName = anInterface.getName();
                Object implementation = part.getImplementation(anInterface);
                checkInterfaceIsAssignableFromImplementation(anInterface, implementation);

                if (!implementationCatalog.containsKey(interfaceName)) {
                    implementationCatalog.put(interfaceName, new ArrayList<>());
                }
                final List<Object> record = implementationCatalog.get(interfaceName);
                record.add(implementation);
            }
        }
        catalog = implementationCatalog;
    }

    /**
     * Returns all implementations available for given interface.
     */
    List<Object> getRecord(Class<?> interfaceNecessary) {
        final String interfaceName = interfaceNecessary.getName();
        final List<Object> record = catalog.get(interfaceName);
        if (record == null) {
            throw new LibrariumException("Implementation not registered for " + interfaceName);
        }
        for (Object implementation : record) {
            if (interfaceNecessary.isAssignableFrom(implementation.getClass())) {
                continue;
            }
            final Class<?> implementationClass = implementation.getClass();
            final Class<?> actualInterface;
            try {
                actualInterface = implementationClass.getClassLoader().loadClass(interfaceName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(
                        "Could not find an interface " + interfaceName + " in classloader of " + implementationClass
                );
            }
            throw new RuntimeException(
                    "Classloader mismatch for " + interfaceNecessary +
                            " got class from " + actualInterface.getClassLoader() +
                            " but expect class from " + interfaceNecessary.getClassLoader()
            );
        }
        return Collections.unmodifiableList(record);
    }

    private void checkInterfaceIsAssignableFromImplementation(Class<?> anInterface, Object implementation) {
        final Class<?> implementationClass = implementation.getClass();
        if (!anInterface.isAssignableFrom(implementationClass)) {
            final Class<?>[] interfaces = getInterfaces(implementationClass);

            throw new LibrariumException(
                    implementationClass + " doesn't match to API " + anInterface + ": " +
                            "implementation class loader " + implementationClass.getClassLoader() + ", " +
                            "interface class loader " + anInterface.getClassLoader()
            );
        }
    }

    private Class<?>[] getInterfaces(Class<?> implementationClass) {
        final Set<Class<?>> interfaces = new HashSet<>();
        final Deque<Class<?>> stack = new ArrayDeque<>();
        stack.addLast(implementationClass);
        while (!stack.isEmpty()) {
            final Class<?> lastItem = stack.pollLast();
            final Class<?>[] currentInterfaces = lastItem.getInterfaces();
            for (Class<?> currentInterface : currentInterfaces) {
                final boolean interfaceIsNew = interfaces.add(currentInterface);
                if (interfaceIsNew) {
                    stack.addLast(currentInterface);
                }
            }
        }
        return interfaces.toArray(new Class<?>[0]);
    }

}
