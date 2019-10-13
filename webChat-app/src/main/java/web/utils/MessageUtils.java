package web.utils;

import web.messages.InternalMessage;
import web.messages.SendMessage;
import web.messages.ServiceMessage;

public class MessageUtils {


    public static InternalMessage parseAndCreateContext(String message) {
        if(message.startsWith("/")){
            return new ServiceMessage(message);
        }else{
            return new SendMessage(message);
        }
    }
}
