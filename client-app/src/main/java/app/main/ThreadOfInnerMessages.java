package app.main;

public class ThreadOfInnerMessages implements Runnable {
    private APPChat chat;
    private Connection connection;

    ThreadOfInnerMessages(APPChat chat) {
        this.chat=chat;
        this.connection=chat.getConnection();
    }

    @Override
    public void run() {
        String report;
        while (!chat.isExited()&&!Thread.currentThread().isInterrupted()) {
            report = (String) connection.getMessage();
            Message message=MessageUtils.convertToMessage(report);
            service(message);
        }
    }
    private void service(Message message){
        switch(message.getStatus()){
            case "connect":
                chat.setIsleave(true);
                chat.setRecipient(message.getName());
                System.out.println();
                System.out.print("You connected to chat with "+message.getName().split("#")[1]);
                break;
            case "leave":
                chat.setIsleave(false);
                message.setText("");
                System.out.println();
                System.out.print("You disconnected to chat.");
                chat.setRecipient(null);
                break;
            case "reg":
                System.out.print(message.getText()+": ");
                System.out.println("Please register to press command /reg $type $name");
                message.setText("");
                break;
            case "reg_success":
                chat.setRegistered(true);
                chat.setNameKey(message.getText());
                chat.setName(message.getText().split("#")[1]);
                message.setText("");
                System.out.print("Registration success. Wait to connection.");
                break;
            case "reg_fail":
                System.out.print("Registration failed. ");
                break;
            case "server_down":
                message.setText("");
                chat.setExited(true);
                System.out.println();
                System.out.print("Server is down. ");
                System.out.println("Press any key to exit app");
                break;
            case "send":
                System.out.println();
                System.out.print(chat.getRecipient().split("#")[1]+": ");
                break;
        }
        System.out.println(message.getText());
    }

}
