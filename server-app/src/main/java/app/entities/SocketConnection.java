package app.entities;

import app.interfaces.Connection;
import app.interfaces.Converter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class SocketConnection implements Connection<Message> {
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private Converter converter;
    private static Logger LOGGER = LogManager.getLogger(SocketConnection.class);

    public SocketConnection(Socket socket, Converter converter) {
        this.socket = socket;
        this.converter = converter;
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));;
            this.printWriter = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())), true);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
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
            if (bufferedReader.ready()) {
                report = bufferedReader.readLine();
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return (Message) converter.convertToMessage(report);
    }

    @Override
    public boolean isActive() {
        if(socket.isClosed()) return false;
        return true;
    }

    @Override
    public void close() throws IOException {
        socket.close();
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


}
