package web.converters;

import com.google.gson.Gson;
import web.messages.InternalMessage;

public class JsonConverter implements Converter<InternalMessage, String> {
    private Gson gson = new Gson();

    @Override
    public String convertToExternalContext(InternalMessage internal) {
        return gson.toJson(internal);
    }

    @Override
    public InternalMessage convertToInternalContext(String external) {
        return gson.fromJson(external, InternalMessage.class);
    }
}
