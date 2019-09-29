package web.model;


import web.interfaces.Connection;
import web.interfaces.Converter;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class SocketConnection implements Connection<String> {
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private Converter converter;

    public SocketConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));;
        this.printWriter = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())), true);
    }

    @Override
    public void sendMessage(String message) {
        printWriter.println(message);
    }

    @Override
    public String getMessage() throws IOException {
        String report = null;
        if (bufferedReader.ready()) {
            report = bufferedReader.readLine();
        }
        return report;
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
    public String toString() {
        return "SocketConnection{" +
                "socket=" + socket +
                ", bufferedReader=" + bufferedReader +
                ", printWriter=" + printWriter +
                ", converter=" + converter +
                '}';
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
