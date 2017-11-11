/*
 * Copyright 2017 Roman Mashenkin
 * Licensed under the Apache License, Version 2.0
 */
package store.vxdesign.apps.cryptography.framework.utilities;

import org.junit.Assert;
import org.junit.Test;

/**
 * Enumeration util unit tests class.
 *
 * @author Roman Mashenkin
 * @since 22.10.2017
 */
public class EnumUtilsTest {

    @Test
    public void testValueMethodSuccessfully() throws Exception {
        Assert.assertTrue("Another result is expected", Enum.E1.equals(EnumUtils.value("E1", Enum.values())));
    }

    @Test
    public void testValueMethodUnsuccessfully() throws Exception {
        Assert.assertFalse("Another result is expected", Enum.E2.equals(EnumUtils.value("e2", Enum.values())));
    }

    @Test
    public void testValueMethodWithReturnedNonNull() throws Exception {
        Assert.assertNotNull("Another result is expected", EnumUtils.value("E3", Enum.values()));
    }

    @Test
    public void testValueMethodWithReturnedNull() throws Exception {
        Assert.assertNull("Another result is expected", EnumUtils.value("E4", Enum.values()));
    }

    private enum Enum {

        E1, E2, E3
    }
}
