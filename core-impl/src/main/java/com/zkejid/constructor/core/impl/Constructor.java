package com.zkejid.constructor.core.impl;

import com.zkejid.constructor.core.api.v1.ConstructionException;
import com.zkejid.constructor.core.api.v1.ConstructorPart;
import com.zkejid.constructor.core.api.v1.EntryPoint;

import java.util.*;

/**
 * Entrance point to the application while starting it in a {@code java -jar} way.
 */
public class Constructor {

    private PartProvider partProvider = new PartProvider();

    public Constructor() {
    }

    public Constructor(PartProvider partProvider) {
        this.partProvider = partProvider;
    }

    /**
     * Call of this method causes building the whole application and calling {@code EntryPoint} if exists.
     *
     * @param args Arguments to business logic module. Method passes arguments as is.
     */
    public void main(String[] args) {
        List<ConstructorPart> initializationList = partProvider.getParts();

        final Librarium librarium = new Librarium();
        librarium.makeCatalog(initializationList);

        linkModules(initializationList, librarium);
        verifyApplicationBuild(initializationList);
        callEntryPoint(args, librarium);
    }

    private void linkModules(List<ConstructorPart> initializationList, Librarium librarium) {
        for (ConstructorPart part : initializationList) {
            for (Class<?> interfaceNecessary : part.getInterfacesNecessary()) {
                final List<Object> record = librarium.getRecord(interfaceNecessary);
                for (Object implementation : record) {
                    part.putImplementation(interfaceNecessary, implementation);
                }
            }
        }
    }

    private void verifyApplicationBuild(List<ConstructorPart> initializationList) {
        for (ConstructorPart part : initializationList) {
            part.verifyNecessaryInterfaces();
        }
    }

    private void callEntryPoint(String[] args, Librarium librarium) {
        final List<Object> entryPoints = librarium.getRecord(EntryPoint.class);
        if (entryPoints == null) {
            return;
        }
        if (entryPoints.size() > 1) {
            throw new ConstructionException("Core module supports only one entry point");
        }
        final EntryPoint entryPoint = (EntryPoint) entryPoints.get(0);
        entryPoint.main(args);
    }

}
