/*
 * Copyright 2017 Roman Mashenkin
 * Licensed under the Apache License, Version 2.0
 */
package store.vxdesign.cryptography.framework.utilities;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Roman Mashenkin
 * @since 22.10.2017
 */
@RunWith(Parameterized.class)
public class ConverterUtilsTest {

    @Parameterized.Parameter
    public String input;

    @Parameterized.Parameter(1)
    public String binaryInput;

    @Parameterized.Parameter(2)
    public String[] binaryInputBlocks;

    @Parameterized.Parameter(3)
    public String hex;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        List<String> inputs = Arrays.asList("12345678", "sdfuvyy3", "734oh92bdfcagdse");
        List<String> outputs = Arrays.asList("0011000100110010001100110011010000110101001101100011011100111000",
                "0111001101100100011001100111010101110110011110010111100100110011",
                "0011011100110011001101000110111101101000001110010011001001100010" +
                        "0110010001100110011000110110000101100111011001000111001101100101");
        List<String> hexes = Arrays.asList("3132333435363738", "7364667576797933", "3733346F683932626466636167647365");

        Object[][] data = new Object[3][4];

        for (int i = 0; i < 3; i++) {
            data[i][0] = inputs.get(i);
            data[i][1] = outputs.get(i);
            data[i][2] = outputs.get(i).split(StringUtils.divideOnBlocksPattern(64));
            data[i][3] = hexes.get(i);
        }

        return Arrays.asList(data);
    }

    @Test
    public void testToBinaryString() throws Exception {
        Assert.assertTrue("Another result is expected", binaryInput.equals(ConverterUtils.toBinaryString(input)));
    }

    @Test
    public void testToBinaryStringBlocks() throws Exception {
        Assert.assertTrue("Another result is expected", Arrays.equals(binaryInputBlocks, ConverterUtils.toBinaryStringBlocks(input, 64)));
    }

    @Test
    public void testToTextString() throws Exception {
        Assert.assertTrue("Another result is expected", input.equals(ConverterUtils.toTextString(binaryInputBlocks)));
    }

    @Test
    public void testToHexString() throws Exception {
        Assert.assertTrue("Another result is expected", hex.equals(ConverterUtils.toHexString(binaryInputBlocks)));
    }
}
