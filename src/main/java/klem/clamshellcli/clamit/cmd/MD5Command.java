package klem.clamshellcli.clamit.cmd;

import klem.clamshellcli.clamit.ClamITController;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.clamshellcli.api.Command;
import org.clamshellcli.api.Context;
import org.clamshellcli.api.IOConsole;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.Map;

// TODO HANDLE CARRIAGE RETURN ON INPUT STRING
// TODO HANDLE ENCODING SPECIFICATION

public class MD5Command implements Command {
	private static final String NAMESPACE = ClamITController.CLAMIT_NAMESPACE;
	private static final String ACTION_NAME = "md5";

	private static final String ENCODING = "UTF-8";
	private IOConsole console;

	@Override
	public void plug(Context ctx) {
		console = ctx.getIoConsole();
	}

	@Override
	public Object execute(Context ctx) {

		String[] values = (String[]) ctx.getValue(Context.KEY_COMMAND_LINE_ARGS);

		// INPUT VERIFICATION
		if (values == null || values.length < 1) {
			console.writeOutput(String.format("%s%n","This command has arguments : " + getDescriptor().getUsage()));
			return null;
		}


		String sourceString;
		
		StringBuilder sb = new StringBuilder();
		
		// SPACE SUPPORT FOR STRINGS
		// SO SENTENCES DO NOT COUNT AS n ARGUMENTS
		if (values.length > 1) {
			for (int i = 1; i < values.length; i++) {
				sb.append(values[i]);
				sb.append(" ");
			}

			sourceString = sb.toString();
			// SINGLE WORD
		} else {
			sourceString = values[0];
		}


		// IMPLEMENTATION
		MessageDigest md = DigestUtils.getMd5Digest();
		
		byte[] input = null;
		try {
			input = sourceString.getBytes(ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        
        
        byte byteData[] = md.digest(input);
 
        String hash = Hex.encodeHexString(byteData);
        console.writeOutput(String.format("%-10s :: %s%n", "Hash", hash));
        console.writeOutput(String.format("%-10s :: %s%n", "Algorithm", md.getAlgorithm()));
        console.writeOutput(String.format("%-10s :: %s%n", "Length", md.getDigestLength()));
        console.writeOutput(String.format("%-10s :: %s%n", "Encoding", ENCODING));
       
        
		return null;
	}

	@Override
	public Descriptor getDescriptor() {
		return new Command.Descriptor() {
			@Override
			public String getNamespace() {
				return NAMESPACE;
			}

			@Override
			public String getName() {
				return ACTION_NAME;

			}

			@Override
			public String getDescription() {
				return "Generate a MD5 from a String. Implemented in UTF8";

			}

			@Override
			public String getUsage() {
				return "Type 'md5' string";

			}

			@Override
			public Map<String, String> getArguments() {
				return Collections.emptyMap();

			}

		};

	}

}
