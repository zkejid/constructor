package com.zkejid.constructor.core.impl;

import com.zkejid.constructor.core.api.v1.ConstructionException;
import com.zkejid.constructor.core.api.v1.ConstructorPart;
import com.zkejid.constructor.core.api.v1.EntryPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

class LibrariumTest {

    private Librarium librarium;

    @BeforeEach
    void setUp() {
        librarium = new Librarium();
    }

    @Test
    void makeCatalog_nullInitializationList_npe() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> librarium.makeCatalog(null)
        );
    }

    @Test
    void makeCatalog_emptyInitializationList_success() {
        librarium.makeCatalog(new ArrayList<>());
    }

    @Test
    void makeCatalog_singleElementInitializationList_success() {
        ConstructorPart testPart = new TestPart();
        librarium.makeCatalog(Collections.singletonList(testPart));
    }

    @Test
    void makeCatalog_twoElementsOfSameClassInitializationList_success() {
        ConstructorPart testPart1 = new TestPart();
        ConstructorPart testPart2 = new TestPart();

        librarium.makeCatalog(Arrays.asList(testPart1, testPart2));
    }

    @Test
    void getRecord_noInterfaces_npe() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> librarium.getRecord(null)
        );
    }

    @Test
    void getRecord_notRegisteredInterface_exception() {
        Assertions.assertThrows(
                LibrariumException.class,
                () -> librarium.getRecord(Map.class),
                "No exception provided on the request of the record of unknown type"
        );
    }

    @Test
    void getRecord_notRegisteredSystemInterface_null() {
        final List<Object> record = librarium.getRecord(EntryPoint.class);

        Assertions.assertNull(record, "Got not null result on request of not registered system interface");
    }

    @Test
    void getRecord_oneProvidedInterface_success() {
        final HashMap<Object, Object> expectedObject = new HashMap<>();
        final CustomPart customPart1 = new CustomPart(
                Collections.singleton(Map.class),
                Collections.emptySet(),
                Collections.emptyMap()
        );
        final CustomPart customPart2 = new CustomPart(
                Collections.emptySet(),
                Collections.singleton(Map.class),
                Collections.singletonMap(Map.class, expectedObject)
        );
        librarium.makeCatalog(Arrays.asList(customPart1, customPart2));

        final List<Object> record = librarium.getRecord(Map.class);

        Assertions.assertEquals(1, record.size(), "Expects exactly one object in record");
        Assertions.assertEquals(expectedObject, record.get(0), "Record contains not the expected object");
    }

    @Test
    void getRecord_cycleDependenciesOnTwoParts_success() {
        final HashSet<Object> expectedObject1 = new HashSet<>();
        final HashMap<Object, Object> expectedObject2 = new HashMap<>();
        final CustomPart customPart1 = new CustomPart(
                Collections.singleton(Map.class),
                Collections.singleton(Set.class),
                Collections.singletonMap(Set.class, expectedObject1)
        );
        final CustomPart customPart2 = new CustomPart(
                Collections.singleton(Set.class),
                Collections.singleton(Map.class),
                Collections.singletonMap(Map.class, expectedObject2)
        );
        librarium.makeCatalog(Arrays.asList(customPart1, customPart2));

        final List<Object> recordSet = librarium.getRecord(Set.class);
        final List<Object> recordMap = librarium.getRecord(Map.class);

        Assertions.assertEquals(1, recordSet.size(), "Expects exactly one object in record");
        Assertions.assertEquals(expectedObject1, recordSet.get(0), "Record contains not the expected object");
        Assertions.assertEquals(1, recordMap.size(), "Expects exactly one object in record");
        Assertions.assertEquals(expectedObject2, recordMap.get(0), "Record contains not the expected object");
    }

    @Test
    void getRecord_dependencyOnTwoImplementations_success() {
        final Set<Object> expectedObject1 = Collections.singleton("a");
        final Set<Object> expectedObject2 = Collections.singleton("b");
        final CustomPart customPartProvider1 = new CustomPart(
                Collections.emptySet(),
                Collections.singleton(Set.class),
                Collections.singletonMap(Set.class, expectedObject1)
        );
        final CustomPart customPartProvider2 = new CustomPart(
                Collections.emptySet(),
                Collections.singleton(Set.class),
                Collections.singletonMap(Set.class, expectedObject2)
        );
        final CustomPart customPartReceiver = new CustomPart(
                Collections.singleton(Set.class),
                Collections.emptySet(),
                Collections.emptyMap()
        );
        librarium.makeCatalog(Arrays.asList(customPartProvider1, customPartProvider2, customPartReceiver));

        final List<Object> recordSet = librarium.getRecord(Set.class);

        Assertions.assertEquals(2, recordSet.size(), "Expects exactly two objects in record");
        Assertions.assertNotEquals(recordSet.get(0), recordSet.get(1), "Objects contained should be different");
        Assertions.assertTrue(recordSet.contains(expectedObject1), "Record should contain expected object");
        Assertions.assertTrue(recordSet.contains(expectedObject2), "Record should contain expected object");
    }

    static class TestPart implements ConstructorPart {

        @Override
        public Set<Class<?>> getInterfacesNecessary() {
            return new HashSet<>();
        }

        @Override
        public Set<Class<?>> getInterfacesProvided() {
            return new HashSet<>();
        }

        @Override
        public Object getImplementation(Class<?> anInterface) {
            return null;
        }

        @Override
        public void putImplementation(Class<?> interfaceNecessary, Object implementation) {}

        @Override
        public void verifyNecessaryInterfaces() throws ConstructionException {}
    }

}