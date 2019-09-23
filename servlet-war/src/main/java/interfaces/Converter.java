package interfaces;


public interface Converter<MessageType,ReportType> {
    ReportType convertToReport(MessageType message);
    MessageType convertToMessage(ReportType report);

}
