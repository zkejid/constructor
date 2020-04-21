package com.zkejid.constructor.core.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
        librarium.makeCatalog(new ArrayList<>());
    }

    @Test
    void getRecord() {
    }
}