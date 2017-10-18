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

        Option generateKey = new Option("k", "generate-key", true, "Generate one key for encryption of all files");
        generateKey.setOptionalArg(true);
        generateKey.setArgs(0);
        options.addOption(generateKey);

        Option paths = new Option("p", "path", true, "Paths to files");
        paths.setOptionalArg(true);
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
