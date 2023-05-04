package main;

import java.util.ArrayList;

public class ArgumentParser {

    public ArgumentParser(String[] args) {
        for (String arg : args) {
            String[] cmdParam = arg.split(":", 2);
            String command = cmdParam[0];
            String parameter = "";
            if (cmdParam.length == 2) {
                parameter = cmdParam[1];
            }
            arguments.add(new Argument(command, parameter));
        }
    }

    private ArrayList<Argument> arguments = new ArrayList<Argument>();

    public ArrayList<Argument> getArguments() {
        return arguments;
    }
}
