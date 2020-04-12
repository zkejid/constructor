package com.zkejid.constructor.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Class searches all parts of application available. Given implementation makes search in the classpath with
 * {@link ServiceLoader}.
 */
public class PartProvider {

    List<ConstructorPart> getParts() {
        final ServiceLoader<ConstructorPart> loader = ServiceLoader.load(ConstructorPart.class);
        final List<ConstructorPart> initializationList = new ArrayList<>();
        for (ConstructorPart part: loader) {
            initializationList.add(part);
        }
        return initializationList;
    }

}
