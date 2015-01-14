package klem.clamshellcli.clamit;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.clamshellcli.api.Command;
import org.clamshellcli.api.Context;
import org.clamshellcli.core.AnInputController;

public class ClamITController extends AnInputController{
	public static String CLAMIT_NAMESPACE = "clamit";
	public final static String KEY_ARGS_URL = "url";
    private Map<String,Command> commands;

    /**
     * Handles incoming command-line input.  CmdController first splits the
     * input and uses token[0] as the action name mapped to the Command.
     * @param ctx the shell context.
     */
    @Override
    public boolean handle(Context ctx) {
        String cmdLine = (String)ctx.getValue(Context.KEY_COMMAND_LINE_INPUT);
        boolean handled = false;
        // handle command line entry.  NOTE: value can be null
        if(cmdLine != null && !cmdLine.trim().isEmpty()){
            String[] tokens = cmdLine.trim().split("\\s+");
            if(!commands.isEmpty()){
                Command cmd = commands.get(tokens[0]);
                if(cmd != null){
                    if(tokens.length > 1){
                        String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
                        ctx.putValue(Context.KEY_COMMAND_LINE_ARGS, args);
                    }
                    
                    cmd.execute(ctx);
                    ctx.getIoConsole().writeOutput("\n");
                    
                    handled = true;
                }else{
                    ctx.getIoConsole().writeOutput(String.format("%nCommand [%s] is unknown. "
                            + "Type help for a list of installed commands.%n%n", tokens[0]));
                }
            }
        }
        
        return handled;
    }

    /**
     * Entry point for the plugin.  This method loads the Command plugins found
     * on the classpath and maps them to their action name.
     * @param plug 
     */
    @Override
    public void plug(Context plug) {
        super.plug(plug);
        List<Command> sysCommands = plug.getCommandsByNamespace(CLAMIT_NAMESPACE);
        if(sysCommands.size() > 0){
            commands = plug.mapCommands(sysCommands);
            Set<String> cmdHints = new TreeSet<String>();
            // plug each Command instance and collect input hints
            for(Command cmd : sysCommands){
                cmd.plug(plug);
                cmdHints.addAll(collectInputHints(cmd));
            }

            // save expected command input hints
            setExpectedInputs(cmdHints.toArray(new String[0]));
            
        }else{
            plug.getIoConsole().writeOutput(
                String.format("%nNo commands were found for input controller"
                    + " [%s].%nn", this.getClass().getName()));
        }
    }
}