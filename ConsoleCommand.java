package slythr;

/**
 * Created by teddy on 9/5/17.
 */
public class ConsoleCommand {

    public String call_command;
    private ConsoleOperation call_operation;

    public ConsoleCommand(String call_text, ConsoleOperation operation) {
        call_command = call_text;
        call_operation = operation;
    }

    public void operate(String console_line){
        call_operation.operation(console_line.replace(call_command + " ", ""));
    }

}
