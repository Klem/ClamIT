package klem.clamshellcli.clamit.cmd;

import klem.clamshellcli.clamit.ClamITController;
import org.clamshellcli.api.Command;
import org.clamshellcli.api.Context;
import org.clamshellcli.api.IOConsole;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

// TODO HANDLE CARRIAGE RETURN ON INPUT STRING
// TODO HANDLE ENCODING SPECIFICATION


public class UnMD5Command implements Command {
	private static final String NAMESPACE = ClamITController.CLAMIT_NAMESPACE;
	private static final String ACTION_NAME = "unmd5";

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

        String sourceString = values[0];
        Document doc;
		String token = "";


        try {

            doc = Jsoup.connect("http://www.stringfunction.com/md5-decrypter.html")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Host", "www.stringfunction.com")
                    .header("Origin", "http://www.stringfunction.com")
                    .header("Referer", "http://www.stringfunction.com/md5-decrypter.html")
                    .header("User-Agent", "Mozilla / 5.0 (Windows NT 6.1;WOW64)AppleWebKit / 537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36")
                    .data("string_md5", sourceString)
                    .data("submit", "Decrypt")
                    .post();

            Element input = doc.getElementById("textarea_md5_decrypter");
            token = input.val();

			token = token.isEmpty() ? "NOT FOUND" : token;

            System.out.println("Hash:: " + sourceString);
            System.out.println("Decrypted:: " + token);
            System.out.println("Length:: " + token.length());

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
		return null;
	}

	@Override
	public Descriptor getDescriptor() {
		return new Descriptor() {
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
				return "Unmd5 a md5 string. Queries remote server. Implemented in UTF8";

			}

			@Override
			public String getUsage() {
				return "Type 'unmd5' hash";

			}

			@Override
			public Map<String, String> getArguments() {
				return Collections.emptyMap();

			}

		};

	}

}
