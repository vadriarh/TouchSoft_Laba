package app.main;

import java.io.*;

class MessageUtils {

    static String getConsoleMessage() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String message = null;
        try {
            message = reader.readLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return message;
    }

    public static Message convertToMessage(String report){
        Message message=new Message();
        String[]parseReport=report.split("#@");
        message.setStatus(parseReport[0]);
        message.setName(parseReport[1]);
        if(parseReport.length==3){
            message.setText(parseReport[2]);
        }else{
            message.setText("");
        }
        return message;
    }

}
