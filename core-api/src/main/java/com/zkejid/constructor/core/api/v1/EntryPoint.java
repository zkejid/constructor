package com.zkejid.constructor.core.api.v1;

/**
 * Entry point interface for module, containing business logic.
 * <p>
 * Core module would call implementation of this interface if found.
 * Implementor should declare {@code EntryPoint} as a provided API of
 * the module.
 */
public interface EntryPoint {

    /**
     * Entry point method.
     * @param args command line arguments of application call. If the
     *             context doesn't suppose command line call (code is
     *             running in a container) the empty array is
     *             provided.
     */
    void main(String[] args);

}
