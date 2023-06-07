package net.canopy.app;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Set;
import org.reflections.Reflections;
import java.lang.reflect.Method;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException {
        ArgumentParser argsParser = new ArgumentParser(args);

        JsonNode node = null;
        for (Argument arg : argsParser.getArguments()) {
            try {
                Class<?> filterClass = Class.forName("net.canopy.filters." + arg.getCommand());
                if (!IFilter.class.isAssignableFrom(filterClass)) {
                    System.out.println("class does not implement IFilter interface: " + filterClass.getClass() + "\naborting...");
                    break;
                }

                System.out.println("applying filter : " + filterClass);
                System.out.println("filter arguments: " + arg.getParameter());

                IFilter filterInstance = (IFilter)filterClass.getDeclaredConstructor().newInstance();
                node = filterInstance.apply(node, arg.getParameter());
            }
            catch (Throwable e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
