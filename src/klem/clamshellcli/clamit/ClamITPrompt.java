package klem.clamshellcli.clamit;

import org.clamshellcli.api.Context;
import org.clamshellcli.api.Prompt;

public class ClamITPrompt implements Prompt{
    private static final String PROMPT = "clam-it>";
    
	@Override
    public String getValue(Context ctx) {
        return PROMPT;
    }
	@Override
    public void plug(Context plug) {
        // nothing to do
    }
}
