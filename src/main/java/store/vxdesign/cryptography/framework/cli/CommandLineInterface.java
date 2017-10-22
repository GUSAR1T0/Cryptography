/*
 * Copyright 2017 Roman Mashenkin
 * Licensed under the Apache License, Version 2.0
 */
package store.vxdesign.cryptography.framework.cli;

import org.apache.commons.cli.*;

/**
 * @author Roman Mashenkin
 * @since 18.10.2017
 */
public class CommandLineInterface {

    private final CommandLineParser parser;
    private final HelpFormatter formatter;
    private final Options options;
    private final String[] args;

    public CommandLineInterface(String[] args) {
        this.parser = new DefaultParser();
        this.formatter = new HelpFormatter();
        this.options = createOptions();
        this.args = args;
    }

    private Options createOptions() {
        Options options = new Options();

        Option cipher = new Option("c", "cipher", true, "Encrypt or decrypt");
        cipher.setArgs(1);
        cipher.setValueSeparator('=');
        options.addOption(cipher);

        Option generateKey = new Option("k", "generate-key", false, "Generate one key for encryption of all files");
        generateKey.setOptionalArg(true);
        options.addOption(generateKey);

        Option paths = new Option("p", "path", true, "Paths to files");
        paths.setOptionalArg(true);
        paths.setValueSeparator('=');
        options.addOption(paths);

        return options;
    }

    public CommandLine getParsedArguments() {
        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Cryptography", options);
            System.exit(1);
            return null;
        }
    }
}
