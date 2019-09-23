package app.entities;

import app.interfaces.Converter;
import com.google.gson.Gson;

public class JsonConverter implements Converter<Message,String> {
    private Gson gson=new Gson();
    @Override
    public String convertToReport(Message message){
        return gson.toJson(message);
    }
    @Override
    public Message convertToMessage(String report){
        return gson.fromJson(report,Message.class);
    }
}
