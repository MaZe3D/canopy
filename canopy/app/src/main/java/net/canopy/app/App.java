package net.canopy.app;

import net.canopy.app.api.*;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Set;
import org.reflections.Reflections;
import java.util.ArrayList;
import java.util.HashSet;

public class App {

    private static Logger logger = new Logger(App.class.getName());

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException {
        ArgumentParser argsParser = new ArgumentParser(args);

        var filterCommands = argsParser.getArguments();
        if (filterCommands.size() == 0) {
            logger.log("no filter commands specified");
            return;
        }

        var filterChain = loadFilters(filterCommands);

        try {
            checkFilterOrder(filterChain);
        } catch (FilterOrderException e) {
            logger.log(e.getMessage());
            return;
        }

        runFilterChain(filterChain, argsParser.getArguments());
    }

    private static IFilter[] loadFilters(ArrayList<Argument> filterCommands) {
        // get all classes implementing IFilter with classpath starting with net.canopy.filters
        Set<Class<? extends IFilter>> filterClasses = new Reflections("net.canopy.filters").getSubTypesOf(IFilter.class);

        IFilter[] filterChain = new IFilter[filterCommands.size()];
        for (int i = 0; i < filterCommands.size(); i++) {
                var arg = filterCommands.get(i);

                Set<Class<? extends IFilter>> matchingFilterClasses = new HashSet<>();
                for (Class<? extends IFilter> clazz : filterClasses) {
                    if (clazz.getCanonicalName().endsWith(arg.getCommand())) {
                        matchingFilterClasses.add(clazz); // don't break to check for collisions
                    }
                }
                Class<? extends IFilter> filterClass = matchingFilterClasses.iterator().next();

                logger.log("Load filter: " + filterClass + " with arguments: " + arg.getParameter());

                try {
                    filterChain[i] = (IFilter)filterClass.getDeclaredConstructor().newInstance();
                } catch (Throwable e) { e.printStackTrace(); return null; }
        }

        return filterChain;
    }

    private static void checkFilterOrder(IFilter[] filterChain) throws FilterOrderException {
        // check if first node is IFilter.ILoadFilter
        if (!(filterChain[0] instanceof IFilter.ILoadFilter)) {
            throw new FilterOrderException("First filter in chain must be IFilter.ILoadFilter");
        }

        // check if every Store-Filter is preceeded by a Store-Filter
        for (int i = 0; i < filterChain.length - 1; i++) {
            if (!(filterChain[i] instanceof IFilter.IStoreFilter) && filterChain[i + 1] instanceof IFilter.ILoadFilter) {
                throw new FilterOrderException("Load filters must be preceded by a store filter (except at the beginning)");
            }
        }

        // check if last node is IFilter.IStoreFilter
        if (!(filterChain[filterChain.length - 1] instanceof IFilter.IStoreFilter)) {
            throw new FilterOrderException("Last filter in chain must be IFilter.IStoreFilter");
        }
    }

    private static void runFilterChain(IFilter[] filterChain, ArrayList<Argument> args) {
        JsonNode node = null;
        for (int i = 0; i < filterChain.length; i++) {
            try {
                node = filterChain[i].apply(node, args.get(i).getParameter());
            } catch (FilterException e) {
                logger.log("An error occured while running filter \"" + filterChain[i].getClass().getName() + "\": " + e.getMessage() + ". The filter chain will be aborted.");
                break;
            }
        }
    }
}
