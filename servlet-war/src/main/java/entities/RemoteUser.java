package entities;

import interfaces.Converter;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RemoteUser {
    private List<String> messages;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private Converter converter;

    public RemoteUser() throws IOException {
        Socket socket=new Socket("localhost",40404);
        this.messages=new ArrayList<>();
        this.converter=new JsonConverter();
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));;
        this.printWriter = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())), true);
    }

    public void sendMessage(String text){
        printWriter.println(converter.convertToReport(text));
    }

    public Message getMessage() {
        String report = null;
        try {
            if (bufferedReader.ready()) {
                report = bufferedReader.readLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return (Message) converter.convertToMessage(report);
    }


}
