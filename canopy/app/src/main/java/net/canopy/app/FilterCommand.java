package net.canopy.app;

import net.canopy.app.api.IFilter;
import net.canopy.app.api.CanopyException;
import net.canopy.app.api.FilterException;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashSet;
import java.util.Set;


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
     * Space for an instance of the filter class this command represents.
     * Filled by
     */
    private IFilter filterInstance = null;

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
        this.filterArgument = cmd.length > 1 ? cmd[1] : "";
    }

    public void createFilterInstance(Set<Class<? extends IFilter>> allFilterClasses) throws CanopyException {

        // find all classes that end with this.filterClassName
        Set<Class<? extends IFilter>> matchingFilterClasses = new HashSet<>();
        for (var clazz : allFilterClasses) {
            if (clazz.getCanonicalName().endsWith(this.filterClassName)) {
                matchingFilterClasses.add(clazz); // don't break, maybe there are more matches
            }
        }

        // error if there is none / there are more than 1
        if (matchingFilterClasses.isEmpty()) {
            throw new CanopyException(
                this.getClass().getName() + ".getMatchingFilterClass()",
                "Invalid Command. There is no matching filter class for: \"" + this.filterClassName + "\""
            );
        }
        if (matchingFilterClasses.size() > 1) {
            throw new CanopyException(
                this.getClass().getName() + ".getMatchingFilterClass()",
                "Ambiguous Command. There are multiple filter class that match: \"" + this.filterClassName + "\""
            );
        }

        // instantiate class and save instance
        var filterClass = matchingFilterClasses.iterator().next();
        try {
            this.filterInstance = (IFilter)filterClass.getDeclaredConstructor().newInstance();
        } catch (Throwable e) {
            throw new CanopyException(
                this.getClass().getName() + ".createFilterObject()",
                "Could not instantiate class: " + filterClass.getName() + " - " + e.getMessage()
            );
        }
    }

    public JsonNode applyFilter(JsonNode node) throws CanopyException, FilterException {
        if (this.filterInstance != null) {
            return this.filterInstance.apply(node, this.filterArgument);
        }
        throw new CanopyException(this.getClass().getName() + ".applyFilter", "Filter instance has not been created yet. Call createFilterInstance() first!");
    }

    public IFilter getFilterInstance() {
        return this.filterInstance;
    }
}
