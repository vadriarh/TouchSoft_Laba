package app.utils;

public class ServerStatus {
    private static boolean exit;
    private static boolean init;
    private static boolean alive;


    public static boolean isExit() {
        return exit;
    }

    public static void setIsExit(boolean isExit) {
        ServerStatus.exit = isExit;
    }

    public static boolean isInit() {
        return init;
    }

    public static void setIsInit(boolean isInit) {
        ServerStatus.init = isInit;
    }

    public static boolean isAlive() {
        return alive;
    }

    public static void setIsAlive(boolean isAlive) {
        ServerStatus.alive = isAlive;
    }
}
