package app.connections;

import app.converters.Converter;
import app.converters.JsonConverter;
import app.messages.InternalMessage;

import java.io.*;
import java.net.Socket;

public class SocketConnection implements Connection<InternalMessage> {
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private Converter<InternalMessage, String> converter;

    public SocketConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.converter = new JsonConverter();
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.printWriter = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())), true);
    }

    @Override
    public void sendContext(InternalMessage contextToSend) {
        String externalContext = converter.convertToExternalContext(contextToSend);
        printWriter.println(externalContext);
    }

    @Override
    public InternalMessage getContext() throws IOException {
        String externalContext = null;
        if (bufferedReader.ready()) {
            externalContext = bufferedReader.readLine();
        }
        if (externalContext == null) {
            return null;
        }
        return converter.convertToInternalContext(externalContext);
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
