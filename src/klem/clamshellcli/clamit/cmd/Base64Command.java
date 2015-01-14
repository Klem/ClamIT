package klem.clamshellcli.clamit.cmd;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;

import klem.clamshellcli.clamit.ClamITController;

import org.apache.commons.codec.binary.Base64;
import org.clamshellcli.api.Command;
import org.clamshellcli.api.Context;
import org.clamshellcli.api.IOConsole;
import org.clamshellcli.core.ShellException;

// TODO HANDLE CARRIAGE RETURN ON INPUT STRING
// TODO HANDLE ENCODING SPECIFICATION

public class Base64Command implements Command {
	private static final String NAMESPACE = ClamITController.CLAMIT_NAMESPACE;
	private static final String ACTION_NAME = "b64";
	private static final String ENCODE = "encode";
	private static final String DECODE = "decode";
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
		if (values == null || values.length < 2) {
			console.writeOutput(String.format("%s%n","This command has arguments : " + getDescriptor().getUsage()));
			return null;
		}

		String action = values[0];

		if (!ENCODE.equals(action) && !DECODE.equals(action)) {
			console.writeOutput(String.format("%n%s", "Unknown parameter : "+ action));
			console.writeOutput(String.format("%n%s", getDescriptor().getUsage()));

			return null;
		}

		String sourceString = "";
		StringBuilder sb = new StringBuilder();

		// SPACE SUPPORT FOR STRINGS
		// SO SENTENCES DO NOT COUNT AS n ARGUMENTS
		if (values.length > 2) {
			for (int i = 1; i < values.length; i++) {
				sb.append(values[i]);
				sb.append(" ");
			}

			sourceString = sb.toString();
			// SINGLE WORD
		} else {
			sourceString = values[1];
		}


		// IMPLEMENTATION
		Base64 b64 = new Base64();
		
		byte[] content;
		if (action.equals(ENCODE)) {
			try {
				content = sourceString.getBytes(ENCODING);
			} catch (UnsupportedEncodingException e) {
				throw new ShellException(String.format("%n%s", "Unsupport encoding "+ENCODING));
			}
			String encoded = b64.encodeToString(content);
			console.writeOutput(String.format("%n%s", encoded));
			return null;
		}

		if (action.equals(DECODE)) {
			try {
				content = sourceString.getBytes(ENCODING);
			} catch (UnsupportedEncodingException e) {
				throw new ShellException(String.format("%n%s", "Unsupport encoding "+ENCODING));
			}
			String decoded = new String(b64.decode(content));
			console.writeOutput(String.format("%n%s", decoded));
			return null;

		}

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
				return "Decode / Encode a base64 string. No check performed on input validity.Process whatever is passed with no sense of context. Implemented in UTF8";

			}

			@Override
			public String getUsage() {
				return "Type 'b64' [encode || decode] string";

			}

			@Override
			public Map<String, String> getArguments() {
				return Collections.emptyMap();

			}

		};

	}

}
