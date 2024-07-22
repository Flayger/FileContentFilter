package org.flayger.util;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.ArrayList;
import java.util.List;

public class ExtendedParser extends DefaultParser {

    private final ArrayList<String> notParsedArgs = new ArrayList<>();

    public List<String> getNotParsedArgs() {
        return notParsedArgs;
    }

    @Override
    public CommandLine parse(Options options, String[] arguments) throws ParseException {

        List<String> knownArguments = new ArrayList<>();
        notParsedArgs.clear();
        boolean nextArgument = false;
        for (String arg : arguments) {
            if (options.hasOption(arg) || nextArgument) {
                knownArguments.add(arg);
            } else {
                notParsedArgs.add(arg);
            }

            nextArgument = options.hasOption(arg) && options.getOption(arg).hasArg();
        }
        return super.parse(options, knownArguments.toArray(new String[0]));
    }
}
