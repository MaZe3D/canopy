package net.canopy.app;

import net.canopy.app.api.IFilter;
import net.canopy.app.api.Logger;
import net.canopy.app.api.CanopyException;
import net.canopy.app.api.FilterException;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Set;
import org.reflections.Reflections;

/**
 * Main class of Canopy. Contains the entry point of the application.
 */
public class App {

    private static Logger logger = new Logger(App.class.getName());

    /**
     * Entry point of Canopy.
     *
     * @param args Command line arguments passed to Canopy.
     *        Every argument should specify (the end of) a classpath to a valid filter class, as well as an optional string argument for that filter.
     *
     * @see IFilter
     * @see FilterCommand
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            logger.log("no filter commands specified");
            return;
        }

        // split arguments into filter classname + filter argument
        FilterCommand[] filterCommands = new FilterCommand[args.length];
        for (int i = 0; i < args.length; i++) {
            filterCommands[i] = new FilterCommand(args[i]);
        }

        // get all classes implementing IFilter with classpath starting with net.canopy.filters
        // this will be searched for matching classnames
        Set<Class<? extends IFilter>> filterClasses = new Reflections("net.canopy.filters").getSubTypesOf(IFilter.class);

        try {
            // create filter instances
            for (FilterCommand command : filterCommands) {
                command.createFilterInstance(filterClasses);
            }

            checkFilterOrder(filterCommands);

            // run filter chain
            JsonNode node = null;
            for (FilterCommand command : filterCommands) {
                try {
                    node = command.applyFilter(node);
                } catch (FilterException e) {
                    logger.log("An error occured while running filter \"" + command.getFilterInstance().getClass().getName() + "\": " + e.getMessage() + ". The filter chain will be aborted.");
                    break;
                }
            }

        } catch (CanopyException e) {
            logger.log(e.getMessage());
            return;
        }
    }

    private static void checkFilterOrder(FilterCommand[] filterChain) throws CanopyException {
        // check if first node is IFilter.ILoadFilter
        if (!(filterChain[0].getFilterInstance() instanceof IFilter.ILoadFilter)) {
            throw new CanopyException("App.checkFilterOrder()", "First filter in chain must be IFilter.ILoadFilter");
        }

        // check if every Store-Filter is preceeded by a Store-Filter
        for (int i = 0; i < filterChain.length - 1; i++) {
            if (!(filterChain[i].getFilterInstance() instanceof IFilter.IStoreFilter) && filterChain[i + 1].getFilterInstance() instanceof IFilter.ILoadFilter) {
                throw new CanopyException("App.checkFilterOrder()", "Load filters must be preceded by a store filter (except at the beginning)");
            }
        }

        // check if last node is IFilter.IStoreFilter
        if (!(filterChain[filterChain.length - 1].getFilterInstance() instanceof IFilter.IStoreFilter)) {
            throw new CanopyException("App.checkFilterOrder()", "Last filter in chain must be IFilter.IStoreFilter");
        }
    }

}
