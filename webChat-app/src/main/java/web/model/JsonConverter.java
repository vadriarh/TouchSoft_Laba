package web.model;

import web.interfaces.Converter;
import com.google.gson.Gson;

public class JsonConverter implements Converter<Message,String> {
    private Gson gson=new Gson();
    @Override
    public String convertToReport(Message message){
        return gson.toJson(message);
    }
    @Override
    public Message convertToMessage(String report){
        if(report==null) return new Message();
        return gson.fromJson(report,Message.class);
    }
}
