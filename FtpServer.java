
/*ʵ����ȫ������*/

import java.io.*;
import java.net.*;
import java.rmi.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.StringTokenizer;

public class FtpServer {

	static File dir = null;// ��������ʼ����Ŀ¼
	static ServerSocket ss = null;// ����˿�server socket
	static Socket s = null;// ����˿�socket
	static BufferedReader reader = null;
	static PrintWriter writer = null;
	static ServerSocket dss = null;// ���ݶ˿�server socket
	static Socket ds = null;// ���ݶ˿�socket
	static BufferedReader dr = null;// ����������
	static PrintWriter dw = null;// ���������

	static String msg = null;// �ͻ��˷��ص�������Ϣ
	static StringTokenizer command = null;// �ͻ��˷��ص�ָ���
	static String clientIp = null;// �û�IP
	static int logstatus = 0;// �û���¼״̬
	static InetAddress user = null;// �û�IP

	static String name;// �ļ���
	static String address;// �ļ���ַ
	static long offset = 0;// ƫ����
	public static String list = null;

	public static void main(String[] args) throws IOException {

		dir = new File("E:\\01GPA\\01Course\\00History\\02Major\\Experiment\\����������γ����[ʵ���]\\2017-����������ʵ��");
		/* ����ServerSocket���񣬼���9513�˿� */
		ss = new ServerSocket(9513);
		System.out.println("FTP Server Starts.");

		s = ss.accept();// ����ʽ�������ȴ�socket���ӳɹ�
		user = s.getInetAddress();// �õ���ǰ���ӵ��û���ַ

		reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
		writer = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));

		writer.println("220---------- Welcome to Flash FTP server ----------");// ���ӳɹ�����ͻ��������û���
		writer.println("220-You are user number 4 of 150 allowed.");
		writer.println("220-Local time is now 10:39. Server port: 21.");
		writer.println("220-This is a private system - No anonymous login");
		writer.println("220-IPv6 connections are also welcome on this server.");
		writer.println("220 You will be disconnected after 30 minutes of inactivity.");
		writer.flush();

		while (true) {
			msg = reader.readLine();
			System.out.println(msg);// �û�������Ϣ
			command = new StringTokenizer(msg, " ");// �ָ��ָ��
			System.out.println(command.toString());// �û�����ָ��

			// ��Բ�ͬ�����д��ͬ����
			switch (command.nextToken()) {
			case ("USER"):
				USERtask();
				break;

			case ("PASS"):
				PASStask();
				System.out.println("******");
				break;

			case ("PASV"):
				PASVtask();
				break;

			case ("LIST"):
				LISTtask();
				break;

			case ("RETR"):
				RETRtask();
				break;

			case ("STOR"):
				STORtask();
				break;

			case ("SIZE"):
				SIZEtask();
				break;

			case ("MKD"):
				MKDtask();
				break;

			case ("CWD"):
				CWDtask();
				break;
			}
		}
	}

	private static void USERtask() {
		String user = command.nextToken();
		System.out.println("�û���" + user);
		if (user.equals("dlpuser@dlptest.com")) {
			writer.println("331 User " + user + " OK. Password required");
			writer.flush();
		}
	}

	private static void PASStask() {
		if (command.nextToken().equals("hZ3Xr8alJPl8TtE")) {
			writer.println("230 OK. Current restricted directory is " + dir);
			writer.flush();
		}
	}

	private static void PASVtask() throws IOException {
		// ������ɶ˿ں�
		System.out.println("***");
		Random generator = new Random();
		int port_high, port_low;
		while (true) {
			// ��ȡ���������ж˿�
			port_high = 1 + generator.nextInt(20);
			port_low = 100 + generator.nextInt(1000);
			try {
				// �������󶨶˿�
				dss = new ServerSocket(port_high * 256 + port_low);
				break;
			} catch (IOException e) {
				continue;
			}
		}

		writer.println("227 Entering Passive Mode (" + user.getHostAddress().replace(".", ",") + "," + port_high + ","
				+ port_low + ")");
		writer.flush();

		ds = dss.accept();
		dr = new BufferedReader(new InputStreamReader(ds.getInputStream()));
		dw = new PrintWriter(new OutputStreamWriter(ds.getOutputStream()));
	}

	private static void LISTtask() throws IOException {
		writer.println("150 Accepted data connection");
		writer.println("226-Options: -a -l ");
		writer.println("226 4 matches total");
		writer.flush();

		StringBuffer fileInfo = new StringBuffer();
		if (dir.isDirectory())// �ж�file�Ƿ���Ŀ¼
		{
			File[] lists = dir.listFiles();
			for (int i = 0; i < lists.length; i++) {
				fileInfo.append("File Name: " + lists[i].getName() + "\nFile Size: " + lists[i].length() + "B"
						+ "\nFile Time; "
						+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(lists[i].lastModified())) + "\n");
			}
		}
		System.out.println(fileInfo);
		dw.println(fileInfo);
		dw.flush();
		ds.close();
	}

	// ����
	private static void RETRtask() throws IOException {
		System.out.println(msg);
		name = msg.substring(5, msg.length());
		address = dir + "\\" + name;

		writer.println("150 Accepted data connection");
		writer.println("150 8.3 kbytes to download");
		writer.println("226-File successfully transferred");
		writer.println("226 0.000 seconds (measured here), 43.55 Mbytes per second");
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

		dos.close();
		ds.close();
	}

	// �ϴ�
	private static void STORtask() throws IOException {

		byte[] bufferbyte = new byte[1024];
		int len = 0;
		System.out.println(msg);
		name = msg.substring(5, msg.length());
		address = dir + "\\" + name;

		System.out.println(address);
		FileOutputStream in = new FileOutputStream(address, true);
		System.out.println("^^^");
		DataInputStream dis = new DataInputStream(ds.getInputStream());
		while ((len = dis.read(bufferbyte)) != -1) {
			in.write(bufferbyte, 0, len);
		}
		writer.println("150 Accepted data connection");
		writer.println("226-File successfully transferred");
		writer.println("226 0.016 seconds (measured here), 508.39 Kbytes per second");
		writer.flush();
		in.close();
		ds.close();
	}

	private static void SIZEtask() throws IOException {

		String name = msg.substring(5, msg.length());
		File[] files = dir.listFiles();
		int status = 0;
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().equals(name)) {
				writer.println(files[i].length());
				writer.flush();
				status = 1;
				break;
			}
		}

		if (status == 0) {
			writer.println("808 No file.");
			writer.flush();
		}
	}

	private static void MKDtask() {
		System.out.println(msg);
		String folder = msg.substring(4, msg.length());
		System.out.println(folder);
		address = dir + "\\" + folder;
		System.out.println(address);
		File l = new File(address);
		l.mkdir();
		
		writer.println("808 File Folder "+folder+" has been created.");
		writer.flush();
	}

	private static void CWDtask() {
		System.out.println(msg);
		String folder = msg.substring(4, msg.length());
		System.out.println(folder);
		address = dir + "\\" + folder;
		dir = new File(address);
		
		writer.println("808 File Folder "+folder+" has been opened.");
		writer.flush();
	}

	// Java File�಻�ṩ��ȡ�ļ�����ʱ��ķ��������ǵ���Windowsϵͳ��API��ͨ����CMD����ʵ��
	private static String getCreatedTime(File file) {
		try {
			Process p = Runtime.getRuntime().exec("cmd /C dir " + dir + "\\" + file.getName() + " /tc");
			InputStream is = p.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String str;
			int i = 0;
			return br.toString().substring(22, 30);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
