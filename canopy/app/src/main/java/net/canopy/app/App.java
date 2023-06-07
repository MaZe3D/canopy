package net.canopy.app;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Set;
import org.reflections.Reflections;
import java.lang.reflect.Method;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException {
        ArgumentParser argsParser = new ArgumentParser(args);

        var filterChain = loadFilters(argsParser);

        try {
            integrityCheck(filterChain);
        } catch (FilterChainIntegrityException e) {
            System.err.println(e.getMessage());
            return;
        }

        runFilterChain(filterChain, argsParser.getArguments());

    }

    public static IFilter[] loadFilters(ArgumentParser argsParser) {
        IFilter[] filterChain = new IFilter[argsParser.getArguments().size()];

        for (int i = 0; i < argsParser.getArguments().size(); i++) {
            try {
                var arg = argsParser.getArguments().get(i);
                Class<?> filterClass = Class.forName("net.canopy.filters." + arg.getCommand());
                if (!IFilter.class.isAssignableFrom(filterClass)) {
                    System.err.println("class does not implement IFilter interface: " + filterClass.getClass() + "\naborting...");
                    break;
                }

                System.err.println("Load filter: " + filterClass + "with arguments: " + arg.getParameter());

                filterChain[i] = (IFilter)filterClass.getDeclaredConstructor().newInstance();
            }
            catch (Throwable e) {
                e.printStackTrace();
                break;
            }
        }

        return filterChain;
    }

    public static void integrityCheck(IFilter[] filterChain) throws FilterChainIntegrityException {
        //Check if first node is IFilter.ILoadFilter
        if (!(filterChain[0] instanceof IFilter.ILoadFilter)) {
            throw new FilterChainIntegrityException("First filter in chain must be IFilter.ILoadFilter");
        }

        //Check if all Loads have a store as next filter
        for (int i = 0; i < filterChain.length - 1; i++) {
            if (filterChain[i] instanceof IFilter.ILoadFilter && !(filterChain[i + 1] instanceof IFilter.IStoreFilter)) {
                throw new FilterChainIntegrityException("Load filters must be followed by a store filter");
            }
        }

        //Check if last node is IFilter.IStoreFilter
        if (!(filterChain[filterChain.length - 1] instanceof IFilter.IStoreFilter)) {
            throw new FilterChainIntegrityException("Last filter in chain must be IFilter.IStoreFilter");
        }
    }

    //Apply filter chain to JsonNode
    public static void runFilterChain(IFilter[] filterChain, ArrayList<Argument> args)
    {
        JsonNode node = null;

        for (int i = 0; i < filterChain.length; i++) {
            node = filterChain[i].apply(node, args.get(i).getParameter());
        }
    }
}
