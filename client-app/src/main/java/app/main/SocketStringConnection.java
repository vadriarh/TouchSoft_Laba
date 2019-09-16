package app.main;


import java.io.*;
import java.net.Socket;

public class SocketStringConnection implements Connection<String> {
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    SocketStringConnection(Socket socket) {
        try {
            this.socket=socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));;
            this.printWriter = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())), true);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void sendMessage(String message) {
        printWriter.println(message);
    }

    @Override
    public String getMessage() {
        String message = null;
        try {
            while(message==null){
                if(bufferedReader.ready()){
                    message = bufferedReader.readLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }


    @Override
    public void close() throws IOException {
        socket.close();
    }
}
