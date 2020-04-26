package com.zkejid.constructor.core.impl;

import com.zkejid.constructor.core.api.v1.ConstructionException;
import com.zkejid.constructor.core.api.v1.ConstructorPart;
import com.zkejid.constructor.core.api.v1.CoreLogging;
import com.zkejid.constructor.core.api.v1.EntryPoint;

import java.util.*;
import java.util.function.Supplier;

/**
 * Entrance point to the application while starting it in a {@code java -jar} way.
 */
public class Constructor {

    private final Supplier<List<ConstructorPart>> partProvider;

    public Constructor(Supplier<List<ConstructorPart>> partProvider) {
        this.partProvider = partProvider;
    }

    /**
     * Call of this method causes building the whole application and calling {@code EntryPoint} if exists.
     *
     * @param args Arguments to business logic module. Method passes arguments as is.
     */
    public void main(String[] args) {
        List<ConstructorPart> initializationList = partProvider.get();

        final Librarium librarium = new Librarium();
        librarium.makeCatalog(initializationList);

        linkModules(initializationList, librarium);
        logCatalog(librarium);
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

    private void logCatalog(Librarium librarium) {
        final List<Object> loggers = librarium.getRecord(CoreLogging.class);
        if (loggers == null) {
            return;
        }
        if (loggers.size() > 1) {
            throw new ConstructionException("Core module supports only one logging interface");
        }
        CoreLogging logger = (CoreLogging) loggers.get(0);
        logger.log(librarium.toString());
    }

}
