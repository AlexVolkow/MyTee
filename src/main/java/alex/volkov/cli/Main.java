package alex.volkov.cli;

import alex.volkov.tee.ConfigParser;
import alex.volkov.tee.Configuration;
import alex.volkov.tee.Tee;
import org.apache.commons.cli.*;

import java.util.List;

public class Main {
    public static final String APPEND = "append";
    public static final String HELP = "help";

    public static void main(String[] args) {
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        String usage = "mytee [OPTION]... [FILE]...";
        String header = "Copy standard input to each FILE, and also to standard output.\n\n";

        Options options = new Options();
        options.addOption("a", APPEND, false, "append to the given FILEs, do not overwrite");
        options.addOption(null, HELP, false, "display this help and exit");

        int status;
        try {
            CommandLine line = parser.parse(options, args);

            if (line.hasOption(HELP)) {
                formatter.printHelp(usage, header, options, null);
                return;
            }

            List<String> arg = line.getArgList();
            boolean isAppend = line.hasOption(APPEND);

            Configuration config = ConfigParser.parse(isAppend, arg);

            status = Tee.work(System.in, config) ? 1 : 0;
        } catch (ParseException e) {
            status = 1;
            System.out.println("Incorrect startup options");
            formatter.printHelp(usage, header, options, null);
        }
        System.exit(status);
    }
}
