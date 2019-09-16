package app.utils;

import app.messages.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MessageUtils {

    public static String getConsoleMessage() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String message = null;
        try {
            message = reader.readLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return message;
    }

    public static String convertToReport(Message obj){
        String name=obj.getName()!=null?obj.getName():"";
        String status=obj.getStatus()!=null?obj.getStatus():"";
        String text=obj.getText()!=null?obj.getText():"";
        return String.format("%s#@%s#@%s", status, name, text);
    }

    public static Message convertToMessage(String report){
        Message message=new Message();
        if(report!=null){
            String[]parseReport=report.split("#@");
            message.setStatus(parseReport[0]);
            message.setName(parseReport[1]);
            if(parseReport.length==3){
                message.setText(parseReport[2]);
            }else{
                message.setText("");
            }
        }
        return message;
    }

}
