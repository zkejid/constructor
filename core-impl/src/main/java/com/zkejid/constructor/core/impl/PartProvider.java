package com.zkejid.constructor.core.impl;

import com.zkejid.constructor.core.api.v1.ConstructorPart;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * Class searches all parts of application available. Given implementation makes search in the classpath with
 * {@link ServiceLoader}.
 */
public class PartProvider implements Supplier<List<ConstructorPart>> {

    @Override
    public List<ConstructorPart> get() {
        final ServiceLoader<ConstructorPart> loader = ServiceLoader.load(ConstructorPart.class);
        final List<ConstructorPart> initializationList = new ArrayList<>();
        for (ConstructorPart part: loader) {
            initializationList.add(part);
        }
        return initializationList;
    }

}
