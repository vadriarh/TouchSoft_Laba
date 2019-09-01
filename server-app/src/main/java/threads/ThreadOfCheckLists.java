package threads;

import model.Agent;
import model.Client;
import model.User;

import java.util.ArrayList;


public class ThreadOfCheckLists implements Runnable {
    private static ArrayList<Agent> agentArrayList = new ArrayList<Agent>();
    private static ArrayList<Client> clientArrayList = new ArrayList<Client>();

    @Override
    public void run() {
        checkFreeUsers();
    }

    private void checkFreeUsers(){
        while(true){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!clientArrayList.isEmpty()&&!agentArrayList.isEmpty()) {
                Client client = getClientFromList();
                removeClientFromList(client);
                Agent agent=getAgentFromList();
                removeAgentFromList(agent);
                Thread thread = new Thread(new ThreadOfConnection(agent, client));
                thread.start();
            }
        }
    }

    public static void addListStore(User user){
        if(user.getClass()==Agent.class){
            addAgentList((Agent)user);
        }else if(user.getClass()==Client.class){
            addClientList((Client)user);
        }
    }

    private synchronized static void addAgentList(Agent agent){
        agentArrayList.add(agent);
    }
    private synchronized static void addClientList(Client client){
        clientArrayList.add(client);
    }

    private synchronized Client getClientFromList(){
        return clientArrayList.get(0);
    }
    private synchronized Agent getAgentFromList(){
        return agentArrayList.get(0);
    }

    private synchronized void removeAgentFromList(Agent agent){
        agentArrayList.remove(agent);
    }
    private synchronized void removeClientFromList(Client client){
        clientArrayList.remove(client);
    }

}
