package main;

public class Argument {
    String command;
    String parameter;

    public Argument(String command, String parameter) {
        this.command = command;
        this.parameter = parameter;
    }

    public String getCommand() {
        return command;
    }

    public String getParameter() {
        return parameter;
    }
}
