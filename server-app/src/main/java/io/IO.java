package io;

import java.io.*;
import java.net.Socket;

public class IO {
    public static BufferedReader getBufferedReaderOfSocket(Socket socket) {
        InputStreamReader inputStreamReader = null;
        try {
            InputStream inputStream = socket.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BufferedReader(inputStreamReader);
    }

    public static PrintWriter getPrintWriterOfSocket(Socket socket) {
        OutputStreamWriter outputStreamWriter = null;
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new PrintWriter(outputStreamWriter,true);
    }

    public static BufferedReader getBufferedReaderOfSystem() {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(System.in, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new BufferedReader(inputStreamReader);
    }
}
