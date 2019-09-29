package model;


import interfaces.Connection;
import interfaces.Converter;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class SocketConnection implements Connection<String> {
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private Converter converter;

    public SocketConnection(Socket socket) {
        this.socket = socket;
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));;
            this.printWriter = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())), true);
        } catch (IOException e) {

        }
    }

    @Override
    public void sendMessage(String message) {
        printWriter.println(message);
    }

    @Override
    public String getMessage() {
        String report = null;
        try {
            if (bufferedReader.ready()) {
                report = bufferedReader.readLine();
            }
        } catch (IOException e) {
        }
        return (String) report;
    }

    @Override
    public boolean isActive() {
        if(socket.isClosed()) return false;
        return true;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocketConnection that = (SocketConnection) o;
        return socket.equals(that.socket) &&
                bufferedReader.equals(that.bufferedReader) &&
                printWriter.equals(that.printWriter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket, bufferedReader, printWriter);
    }


    @Override
    public void close() throws IOException {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
