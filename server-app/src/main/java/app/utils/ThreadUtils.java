package app.utils;

import app.main.Server;
import app.threads.ThreadOfInnerReports;
import app.threads.ThreadOfRegistration;
import app.threads.ThreadOfServer;

public class ThreadUtils {
    private static Thread threadOfServer;
    private static Thread threadOfRegistration;
    private static Thread threadOfInnerReports;


    public static boolean startThreadServer(Server server){
        if(threadOfServer==null){
            threadOfServer = new Thread(new ThreadOfServer(server));
            threadOfServer.setDaemon(true);
            threadOfServer.setName("Server thread");
            threadOfServer.start();
            return true;
        }
        System.out.println("Server thread been started.");
        return false;
    }

    public static boolean stopThreadOfServer(){
        if(threadOfServer.isAlive()){
            threadOfServer.interrupt();
            threadOfServer=null;
            return true;
        }
        System.out.println("Server thread not started.");
        return false;
    }

    public static boolean startThreadOfRegistration(){
        if(threadOfRegistration==null){
            threadOfRegistration = new Thread(new ThreadOfRegistration());
            threadOfRegistration.setDaemon(true);
            threadOfRegistration.setName("Registration thread");
            threadOfRegistration.start();
            return true;
        }
        System.out.println("Error to start registration thread");
        return false;
    }

    public static boolean stopThreadOfRegistration(){
        if(threadOfRegistration.isAlive()){
            threadOfRegistration.interrupt();
            threadOfRegistration=null;
            return true;
        }
        System.out.println("Registration process not run");
        return false;
    }

    public static boolean startThreadOfInnerReports(){
        if(threadOfInnerReports==null){
            threadOfInnerReports = new Thread(new ThreadOfInnerReports());
            threadOfInnerReports.setDaemon(true);
            threadOfInnerReports.setName("InnerReports thread");
            threadOfInnerReports.start();
            return true;
        }
        System.out.println("InnerReports thread been started");
        return false;
    }

    public static boolean stopThreadOfInnerReports(){
        if(threadOfInnerReports.isAlive()){
            threadOfInnerReports.interrupt();
            threadOfInnerReports=null;
            return true;
        }
        System.out.println("Connection process not run");
        return false;
    }

    public static boolean isIsThreadsAlive() {
        return threadOfInnerReports.isAlive()&&threadOfServer.isAlive()&&threadOfRegistration.isAlive();
    }
}
