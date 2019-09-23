package app.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MessageUtils {
    public static String getConsoleMessage() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String message = null;
        try {
            message = reader.readLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return message;
    }
}
