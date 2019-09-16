package app.model;

import app.messages.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import app.utils.MessageUtils;

import java.io.*;
import java.net.Socket;

public class SocketStringConnection implements Connection<Message> {
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private static Logger LOGGER = LogManager.getLogger(SocketStringConnection.class);

    SocketStringConnection(Socket socket) {
        try {
            this.socket=socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));;
            this.printWriter = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())), true);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void sendMessage(Message message) {
        String report= MessageUtils.convertToReport(message);
        printWriter.println(report);
    }

    @Override
    public Message getMessage() {
        String report = getReport();
        Message message=MessageUtils.convertToMessage(report);
        return message;
    }

    @Override
    public String getReport(){
        String report= null;
        try {
            if (bufferedReader.ready()) {
                report = bufferedReader.readLine();
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return report;
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
