package app.entities;


import app.interfaces.Connection;
import app.interfaces.Converter;

import java.io.*;
import java.net.Socket;

public class SocketConnection implements Connection<Message> {
    private Socket socket;
    private Converter converter;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public SocketConnection(Socket socket, Converter converter) {
        this.socket = socket;
        this.converter = converter;
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ;
            this.printWriter = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())), true);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void sendMessage(Message message) {
        printWriter.println(converter.convertToReport(message));
    }

    @Override
    public Message getMessage() {
        String report = null;
        try {
            while (report == null) {
                if (bufferedReader.ready()) {
                    report = bufferedReader.readLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return (Message) converter.convertToMessage(report);
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
