package com.zkejid.constructor.core.impl;

import com.zkejid.constructor.core.api.v1.ConstructorPart;

import java.util.*;

/**
 * Main class is an entrance point to the application while starting it in a {@code java -jar} way.
 */
public class Main {

    public static void main(String[] args) {
        final PartProvider partProvider = new PartProvider();
        List<ConstructorPart> initializationList = partProvider.getParts();

        final Librarium librarium = new Librarium();
        librarium.makeCatalog(initializationList);

        for (ConstructorPart part : initializationList) {
            for (Class<?> interfaceNecessary : part.getInterfacesNecessary()) {
                final List<Object> record = librarium.getRecord(interfaceNecessary);
                for (Object implementation : record) {
                    part.putImplementation(interfaceNecessary, implementation);
                }
            }
        }

        for (ConstructorPart part : initializationList) {
            part.checkInterfacesListAndStart();
        }
    }

}
