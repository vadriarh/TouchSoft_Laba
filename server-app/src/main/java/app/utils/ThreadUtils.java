package app.utils;

import app.threads.ThreadOfCreateChats;
import app.threads.ThreadOfInnerReports;
import app.threads.ThreadOfServer;

public class ThreadUtils {
    private static Thread threadOfServer;
    private static Thread threadOfCreateChats;
    private static Thread threadOfInnerReports;


    private static boolean startThreadServer(){
        if(threadOfServer==null){
            threadOfServer = new Thread(new ThreadOfServer());
            threadOfServer.setDaemon(true);
            threadOfServer.setName("Server thread");
            threadOfServer.start();
            return true;
        }
        System.out.println("Server thread been started.");
        return false;
    }

    private static boolean stopThreadOfServer(){
        if(threadOfServer.isAlive()){
            threadOfServer.interrupt();
            threadOfServer=null;
            return true;
        }
        System.out.println("Server thread not started.");
        return false;
    }

    private static boolean startThreadOfCreateChats(){
        if(threadOfCreateChats ==null){
            threadOfCreateChats = new Thread(new ThreadOfCreateChats());
            threadOfCreateChats.setDaemon(true);
            threadOfCreateChats.setName("Registration thread");
            threadOfCreateChats.start();
            return true;
        }
        System.out.println("Error to start registration thread");
        return false;
    }

    private static boolean stopThreadOfCreateChats(){
        if(threadOfCreateChats.isAlive()){
            threadOfCreateChats.interrupt();
            threadOfCreateChats =null;
            return true;
        }
        System.out.println("Registration process not run");
        return false;
    }

    private static boolean startThreadOfInnerReports(){
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

    private static boolean stopThreadOfInnerReports(){
        if(threadOfInnerReports.isAlive()){
            threadOfInnerReports.interrupt();
            threadOfInnerReports=null;
            return true;
        }
        System.out.println("Connection process not run");
        return false;
    }

    public static void startThreads(){
        ThreadUtils.startThreadServer();
        ThreadUtils.startThreadOfCreateChats();
        ThreadUtils.startThreadOfInnerReports();
    }

    public static void stopThreads(){
        ThreadUtils.stopThreadOfServer();
        ThreadUtils.stopThreadOfInnerReports();
        ThreadUtils.stopThreadOfCreateChats();
    }
}
