
//1521
//ftp.dlptest.com
//mkdil

/*ʵ����ȫ������*/

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class FtpClient {
	static Socket s = null;
	static Socket ds = null;
	static BufferedReader dr = null;
	static PrintWriter dw = null;
	static String response = null;
	static String data = null;
	static BufferedReader reader = null;
	static PrintWriter writer = null;
	static Scanner input = null;
	static String cmd = null;
	static String name = null;
	static String address = null;
	static StringTokenizer temps = null;
	static File dir = null;// Ĭ�ϵ�ַ
	static String loca = null;
	static int i = 0;

	public static void main(String[] args) throws IOException {

		dir = new File("E:\\");
		/* �ͻ��˺�FTP����������Socket���� */
		s = new Socket(InetAddress.getLocalHost(), 9513);
		reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
		writer = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));

		i = 0;
		while ((response = reader.readLine()) != null) {
			System.out.println(response);
			i++;
			if (i == 6) {
				break;
			}
		}
		cmd = "USER dlpuser@dlptest.com";
		user();
		cmd = "PASS hZ3Xr8alJPl8TtE";
		pass();
		cmd = "LIST";
		list();
		cmd = "STOR xieyi2.txt";
		stor();
		cmd = "RETR haoyun.txt";
		retr();
		/*cmd = "STOR test";
		stor();*/
		/*cmd = "RETR ";
		retr();*/
		/*
		 * cmd="RETR xieyi2.txt"; retr();
		 */
		// ����ѭ�����û����в���
		/*
		 * while (true) { input = new Scanner(System.in); cmd = input.nextLine();
		 * 
		 * switch (cmd.substring(0, 4)) { case ("USER"): user(); break;
		 * 
		 * case ("PASS"): pass(); break; case ("PASV"): getSocket(); break;
		 * 
		 * case ("LIST"): list(); break;
		 * 
		 * case ("STOR"): stor(); break;
		 * 
		 * case ("RETR"): retr(); break;
		 * 
		 * case ("SIZE"): size(); break;
		 * 
		 * case ("CWD "): cwd(); break;
		 * 
		 * case ("MKD "): mkd(); break;
		 * 
		 * case ("QUIT"): quit(); break; } }
		 */

	}

	private static void user() throws IOException {
		if (response.substring(0, 3).equals("220")) {
			writer.println(cmd);
			writer.flush();
			response = reader.readLine();
			System.out.println(response);
		}
	}

	private static void pass() throws IOException {
		/* ����û������ڣ�����Ҫ���������� */
		if (response.substring(0, 3).equals("331")) {
			writer.println(cmd);
			writer.flush();
			response = reader.readLine();
			System.out.println(response);
		}
	}

	private static void list() throws IOException {
		getSocket();// ��ȡ���ݶ˿�
		/* ʹ�� List �������ļ��б� */
		writer.println(cmd);
		writer.flush();
		i = 0;
		while ((response = reader.readLine()) != null) {
			System.out.println(response);
			i++;
			if (i == 3) {
				break;
			}
		}
		while ((data = dr.readLine()) != null) {
			System.out.println(data);
		}
		ds.close();
	}

	private static void stor() throws IOException {
		getSocket();// ��ȡ���ݶ˿�
		/* �ϴ��ļ� */
		address = dir + cmd.substring(5, cmd.length());
		name = cmd.substring(5, cmd.length());
		File tempFile = new File(address);
		/* �ϴ��ļ��� */
		if (tempFile.isDirectory()) {
			cmd = "MKD " + name;
			mkd();// �½��ļ���
			cmd = "CWD " + name;
			cwd();// ���ļ���
			File[] files = tempFile.listFiles();
			if (files != null) {
				System.out.println(files.length);
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						continue;
					}
					System.out.println(i);
					System.out.println(files[i]);
					name = files[i].getName();
					address=files[i].toString();
					System.out.println("**\n"+name+"\n"+address+"\n");
					upLoad(files[i]);
				}
			}
		}
		/* �ϴ��ļ� */
		else {
			upLoad(tempFile);
		}
	}

	private static void retr() throws IOException {
		getSocket();// ��ȡ���ݶ˿�
		/* �����ļ� */

		name = cmd.substring(5, cmd.length());
		System.out.println(name);
		address = dir + "\\" + name;
		File tempFile = new File(address);
		if (tempFile.isDirectory()) {
			cmd = "MKD " + name;
			mkd();// �½��ļ���
			cmd = "CWD " + name;
			cwd();// ���ļ���
			File[] files = tempFile.listFiles();
			if (files != null) {
				System.out.println(files.length);
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						continue;
					}
					System.out.println(i);
					System.out.println(files[i]);
					name = files[i].getName();
					address=files[i].toString();
					System.out.println("**\n"+name+"\n"+address+"\n");
					upLoad(files[i]);
				}
			}
		} else {
			downLoad(tempFile);
		}

	}

	private static void size() throws IOException {
		getSocket();// ��ȡ���ݶ˿�
		/* SIZE:�ӷ������Ϸ���ָ���ļ��Ĵ�С�� */
		writer.println(cmd);
		writer.flush();
		i = 0;
		while ((response = reader.readLine()) != null) {
			System.out.println(response);
			i++;
			if (i == 1) {
				break;
			}
		}
	}

	private static void cwd() throws IOException {
		/* CWD:�ı乤��Ŀ¼�� */
		writer.println(cmd);
		writer.flush();
		i = 0;
		while ((response = reader.readLine()) != null) {
			System.out.println(response);
			i++;
			if (i == 1)
				break;
		}
	}

	private static void mkd() throws IOException {
		writer.println(cmd);
		writer.flush();
		i = 0;
		while ((response = reader.readLine()) != null) {
			System.out.println(response);
			i++;
			if (i == 1)
				break;
		}
	}

	private static void quit() throws IOException {
		/* ��������Ϻ�Ͽ��������Ӳ����� QUIT �����˳��� */
		writer.println("cmd	");
		writer.flush();
		while ((response = reader.readLine()) != null) {
			System.out.println(response);
			i++;
			if (i == 1) {
				break;
			}
		}
		input.close();
		s.close();
	}

	private static void upLoad(File file) throws IOException {
		writer.println("SIZE " + name);
		writer.flush();
		response = reader.readLine();// �������ļ���С
		long local = file.length();
		System.out.println(name);
		System.out.println(address);
		System.out.println("**");
		System.out.println("@@"+response);
		if (response.substring(0, 3).equals("808")) {
			System.out.println("û���ļ��������ϴ���");
			cmd = "STOR "+name;
			writer.println(cmd);
			writer.flush();
			RandomAccessFile outFiles = null;
			DataOutputStream dos = new DataOutputStream(ds.getOutputStream());
			outFiles = new RandomAccessFile(address, "r");
			outFiles.writeChars("");
			byte[] bufferbyte = new byte[1024];
			int len = 0;
			try {
				while ((len = outFiles.read(bufferbyte)) != -1) {
					dos.write(bufferbyte, 0, len);
				}
				dos.flush();
				outFiles.close();
				dos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			i = 0;
			while ((response = reader.readLine()) != null) {
				System.out.println(response);
				i++;
				if (i == 3) {
					break;
				}
			}
		} else if (local <= Integer.parseInt(response)) {
			System.out.println("���������ļ���");
		} else {
			System.out.println("�ϵ�����~");
			int leng = Integer.parseInt(response);
			System.out.println(leng);

			writer.println(cmd);
			writer.flush();
			RandomAccessFile outFiles = null;
			DataOutputStream dos = new DataOutputStream(ds.getOutputStream());
			outFiles = new RandomAccessFile(address, "r");
			outFiles.skipBytes(leng);
			outFiles.writeChars("");
			byte[] bufferbyte = new byte[1024];
			int len = 0;
			try {
				while ((len = outFiles.read(bufferbyte)) != -1) {
					dos.write(bufferbyte, 0, len);
				}
				dos.flush();
				outFiles.close();
				dos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			i = 0;
			while ((response = reader.readLine()) != null) {
				System.out.println(response);
				i++;
				if (i == 3) {
					break;
				}
			}
		}
	}

	private static void downLoad(File file) throws IOException {
		writer.println("SIZE " + name);
		writer.flush();
		response = reader.readLine();// �������ļ���С
		long local = file.length();

		long response2 = getSize(file.getName());

		if (response2 == 808) {
			System.out.println("û���ļ����������أ�");

			writer.println(cmd);
			writer.flush();

			name = cmd.substring(5, cmd.length());
			System.out.println(name);
			i = 0;
			while ((response = reader.readLine()) != null) {
				System.out.println(response);
				i++;
				if (i == 4) {
					break;
				}
			}

			byte[] bufferbyte = new byte[1024];
			int len = 0;
			FileOutputStream in = new FileOutputStream(address);
			DataInputStream dis = new DataInputStream(ds.getInputStream());
			while ((len = dis.read(bufferbyte)) != -1) {
				in.write(bufferbyte, 0, len);
			}
			in.close();
			ds.close();
		} else if (local <= response2) {
			System.out.println("���������ļ��󣬶ϵ�����");
			writer.println(cmd);
			writer.flush();

			name = cmd.substring(5, cmd.length());
			System.out.println(name);
			i = 0;
			while ((response = reader.readLine()) != null) {
				System.out.println(response);
				i++;
				if (i == 4) {
					break;
				}
			}

			byte[] bufferbyte = new byte[1024];
			int len = 0;
			FileOutputStream in = new FileOutputStream(address, true);
			DataInputStream dis = new DataInputStream(ds.getInputStream());
			dis.skip(local);
			while ((len = dis.read(bufferbyte)) != -1) {
				in.write(bufferbyte, 0, len);
			}
			in.close();
			ds.close();
		} else {
			System.out.println("���������ļ�С���������ء�");
		}
	}

	private static long getSize(String name) {
		File[] files = dir.listFiles();
		int status = 0;
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().equals(name)) {
				status = 1;
				return files[i].length();
			}
		}

		if (status == 0) {
			return 808;
		}
		return 0;
	}

	private static void getSocket() throws IOException {
		/* ʹ��PASV����õ������������Ķ˿ںţ������������ӡ� */
		writer.println("PASV");
		writer.flush();
		response = reader.readLine();
		System.out.println(response);

		/* ʹ�� p1*256+p2 ��������ݶ˿ڣ��������ݶ˿ڣ�׼���������ݡ� */
		String temp = response.substring(27, response.length() - 1);
		String[] tem = temp.split(",");
		System.out.println(temp);
		int port = Integer.parseInt(tem[4].trim()) * 256 + Integer.parseInt(tem[5].trim());
		String ip = tem[0] + "." + tem[1] + "." + tem[2] + "." + tem[3];
		ds = new Socket(ip, port);

		/* �����ݶ˿��н������� */
		dr = new BufferedReader(new InputStreamReader(ds.getInputStream()));
		dw = new PrintWriter(new OutputStreamWriter(ds.getOutputStream()));
	}
}