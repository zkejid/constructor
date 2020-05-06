package com.zkejid.constructor.core.api.v1;

import java.util.Set;

/**
 * Abstraction of any part of an application. It defines set of interfaces necessary to operate and set of
 * interfaces that given part provides to the rest of the application. It defines methods to obtain instance of each
 * interface provided and to provide instances of necessary interfaces.
 * <p>
 * Lifecycle of every part is as follows. As a first step caller method links part with the rest of an application. It
 * uses methods {@link #getInterfacesNecessary()}, {@link #getInterfacesProvided()}, {@link #getImplementation(Class)},
 * {@link #putImplementation(Class, Object)}. As a second step caller method calls
 * {@link #verifyNecessaryInterfaces()} to mark that the part is fully initialized and implementation code should
 * check inner integrity.
 */
public interface ConstructorPart {

    /**
     * Returns the set of interfaces, which this part of an application depends on. Implementation of each interface
     * from this set should be explicitly provided before this part of an application could operate properly.
     * However, implementation of {@link ConstructorPart} could define fallback if there is no necessary interface.
     * <p>
     * Implementation of each interface would be provided by caller method with
     * {@link #putImplementation(Class, Object)}.
     * <p>
     * If this part doesn't depend on any interface, method returns empty set. There is no null result.
     * <p>
     * Subsequent calls should return the same set of objects until {@link #verifyNecessaryInterfaces()} method would
     * be called.
     */
    Set<Class<?>> getInterfacesNecessary();

    /**
     * Returns the set of interfaces provided by this part of an application. Implementation of each interface could
     * be obtained by caller method through {@link #getImplementation(Class)}.
     * <p>
     * If this part doesn't provide any interface to the rest of an application, method returns empty set. There is no
     * null result.
     * <p>
     * Subsequent calls should return the same set of objects until {@link #verifyNecessaryInterfaces()} method would
     * be called.
     */
    Set<Class<?>> getInterfacesProvided();

    /**
     * Returns an implementation of given interface. Framework guarantees that given interface would be one of
     * {@link #getInterfacesProvided}.
     *
     * @throws ConstructionException if {@code anInterface} is not provided by this part.
     */
    Object getImplementation(Class<?> anInterface) throws ConstructionException;

    /**
     * Puts implementations of interface, which this part depends on. Framework guarantees that
     * given interface would be one of {@link #getInterfacesNecessary()}, and each implementation
     * object would be an implementation of given interface.
     */
    void putImplementation(Class<?> interfaceNecessary, Object ... implementation);

    /**
     * Implement this method if your module would not work without any dependent interface. Implementation
     * should check which dependency is missed and clearly tell it in error message.
     *
     * Checks if the part is fully linked to the rest of an application. Framework guarantees at this point that each
     * part of an application is fully linked to other parts through interfaces provided. <b>Framework does NOT
     * guarantee that each implementation of each interface would be in proper state to operate</b>. It is the
     * responsibility of each part to maintain its state and properly communicate through interfaces.
     * <p>
     * <b>Important note for implementors:</b> caller method calls this method of each part of an application in a
     * single thread. It means that implementation should not block the execution. All parts of an application a called
     * in no given order, so implementation should not assume that this method is called or not called on any other
     * part object.
     *
     * @throws ConstructionException if module lacks required dependent interface.
     */
    void verifyNecessaryInterfaces() throws ConstructionException;
}
