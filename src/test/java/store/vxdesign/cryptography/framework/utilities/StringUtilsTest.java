/*
 * Copyright 2017 Roman Mashenkin
 * Licensed under the Apache License, Version 2.0
 */
package store.vxdesign.cryptography.framework.utilities;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

/**
 * String util unit tests class.
 *
 * @author Roman Mashenkin
 * @since 22.10.2017
 */
@RunWith(Parameterized.class)
public class StringUtilsTest {

    @Parameterized.Parameter
    public String input;

    @Parameterized.Parameter(1)
    public String output;

    @Parameterized.Parameter(2)
    public int blockSize;

    @Parameterized.Parameter(3)
    public String pattern;

    @Parameterized.Parameter(4)
    public Map.Entry entry;

    @Parameterized.Parameter(5)
    public String line;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        List<String> inputs = Arrays.asList("test1", "TEST2", "3test", "4TEST", "tEsT5");
        List<String> outputs = Arrays.asList("Test1", "Test2", "3test", "4test", "Test5");
        List<Integer> blockSizes = Arrays.asList(8, 16, 32, 64, 128);
        List<String> patterns = Arrays.asList("(?<=\\G.{8})", "(?<=\\G.{16})", "(?<=\\G.{32})", "(?<=\\G.{64})",
                "(?<=\\G.{128})");
        Map<String, String> map = new LinkedHashMap<>();
        map.put("key1", "value1");
        map.put("key2test", "value2");
        map.put("test3key", "value3");
        map.put("testkey4", "value4");
        map.put("keytest5", "value5");
        List<String> lines = Arrays.asList("           key1: value1", "       key2test: value2",
                "       test3key: value3", "       testkey4: value4", "       keytest5: value5");

        Object[][] data = new Object[5][6];

        for (int i = 0; i < 5; i++) {
            data[i][0] = inputs.get(i);
            data[i][1] = outputs.get(i);
            data[i][2] = blockSizes.get(i);
            data[i][3] = patterns.get(i);
            int finalI = i + 1;
            data[i][4] = map.entrySet()
                    .stream()
                    .filter(entry1 -> entry1.getValue().contains(Integer.toString(finalI)))
                    .findFirst()
                    .orElse(null);
            data[i][5] = lines.get(i);
        }

        return Arrays.asList(data);
    }

    @Test
    public void testCapitalize() throws Exception {
        Assert.assertTrue("Another result is expected", output.equals(StringUtils.capitalize(input)));
    }

    @Test
    public void testDivideOnBlocksPattern() throws Exception {
        Assert.assertTrue("Another result is expected", pattern.equals(StringUtils.divideOnBlocksPattern(blockSize)));
    }

    @Test
    public void testCreateLineFromEntry() throws Exception {
        Assert.assertTrue("Another result is expected", StringUtils.createLineFromEntry(entry).contains(line));
    }
}
