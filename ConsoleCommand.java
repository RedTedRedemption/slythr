package slythr;


/**
 * The type Console command.
 */
public class ConsoleCommand {

    /**
     * The command entered into the console that would result in this Console Command being called.
     */
    public String call_command;
    private ConsoleOperation call_operation;

    /**
     * Instantiates a new Console command.
     *
     * @param call_text the call text
     * @param operation the operation
     */
    public ConsoleCommand(String call_text, ConsoleOperation operation) {
        call_command = call_text;
        call_operation = operation;
    }

    /**
     * Run this command's designated operation.
     *
     * @param console_line the console line
     */
    public void operate(String console_line){
        call_operation.operation(console_line.replace(call_command + " ", ""));
    }

}
