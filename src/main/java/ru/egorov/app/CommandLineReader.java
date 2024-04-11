package ru.egorov.app;

import java.util.HashMap;
import java.util.Map;

public class CommandLineReader {

    public final static String COUNT_THREADS_KEY_NAME = "nThreads";
    public final static String COUNT_ACCOUNT_KEY_NAME = "nAccounts";
    public static Map<String, Integer> parseCommandLine(String[] args) {
        Map<String, Integer> result = new HashMap<>();

        if (args.length >= 1 && args[0] != null) {
            result.put(COUNT_THREADS_KEY_NAME, Integer.valueOf(args[0]));
        }

        if (args.length >= 2 && args[1] != null) {
            result.put(COUNT_ACCOUNT_KEY_NAME, Integer.valueOf(args[1]));
        }
        return result;
    }
}
