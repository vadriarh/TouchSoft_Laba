package web.utils;

import web.interfaces.Converter;
import web.model.JsonConverter;
import web.model.Message;

public class MessageUtils {
    private static Converter converter;
    public static String createCloseReport(){
        Message closeSessionMessage=new Message("/exit");
        converter=new JsonConverter();
        String report= (String) converter.convertToReport(closeSessionMessage);
        return report;
    }
}
