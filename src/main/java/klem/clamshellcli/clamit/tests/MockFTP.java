package klem.clamshellcli.clamit.tests;

import klem.clamshellcli.clamit.utils.Utils;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.WindowsFakeFileSystem;

import java.io.File;

public class MockFTP {
	private static String path = "d:\\development\\clamshellcli-0.5.2\\ftp";
	public static void main(String[] args) {
		DirectoryEntry baseDir =  new DirectoryEntry(path);
		File root = new File(path);
		
		FakeFtpServer fakeFtpServer = new FakeFtpServer();
		fakeFtpServer.addUserAccount(new UserAccount("user", "password", path));

		FileSystem fileSystem = new WindowsFakeFileSystem();
		fileSystem.add(baseDir);
		

			for (File file : root.listFiles()) {
				System.out.println(file.getAbsolutePath());
				fileSystem.add(new FileEntry(file.getAbsolutePath(), Utils.getFileContentAsString(file)));
			}
		
		
		System.out.println(fileSystem.getEntry(path));
		
		fakeFtpServer.setFileSystem(fileSystem);

		fakeFtpServer.start();
	}

}
