package net.canopy.app;

/**
 * Class that represents one command line argument from the user.
 * The argument is split into the name of the filter class and an argument to that filter.
 */
public class FilterCommand {
    /**
     * String that should be the end of the classname to a valid filter class, that is a class that implements {@link net.canopy.app.api.IFilter}.
     * @see net.canopy.app.api.IFilter
     */
    public final String filterClassName;

    /**
     * Arbitrary string from the user that will be passed through to the filter without modification.
     */
    public final String filterArgument;

    /**
     * Parse the given command line argument into filter classname and filter argument.
     * This is done by splitting the input string at the first colon.
     * <p>
     * Example: "LoadJson:/path/to/json.txt" => classname: "LoadJson", filter argument: "/path/to/json.txt"
     * <p>
     * Example: "ters.builtin.Encrypt:s3cur3_:p4$$word!" => classname: "ters.builtin.Encrypt", filter argument: "s3cur3_:p4$$word!"
     *
     * @param cliString One command line argument.
     */
    public FilterCommand(String cliString) {
        String[] cmd = cliString.split(":", 2);
        this.filterClassName = cmd.length > 0 ? cmd[0] : "";
        this.filterArgument = cmd.length > 1 ? cmd[0] : "";
    }
}
