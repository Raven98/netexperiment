package client;

import java.net.*;
import java.io.*;

/**
 * HTTPClient: HTTP�ͻ���
 * ����HTTP/1.1Э�飬����ʵ�ֶ�������վ��GET��POST����
 * �ṩ�򵥵Ľ������ܡ�
 * ֧��HTML�����飬��HTML�����а������ļ�һ�����ء�
 * @author Raven
 *
 */
public class HTTPClient {
	static Socket s = null;// ����վ����ͨѶ��socket
	static String response = null;// ��վ���ص�����
	static String filename = null;// ����ʵ��ļ���
	static PrintStream writer = null;// �����
	static DataInputStream reader = null;// ������
	static int i = 0;//������
	static String savelocation = "./database";//�����ļ��ֿ�

	public static void main(String[] args) throws IOException {
		/* ���ӷ����� */
		s = new Socket(InetAddress.getByName("www.lib.neu.edu.cn"), 80);

		System.out.println("������������");

		/* ��������ͷ */
		filename = "index.html";
		writer = new PrintStream(s.getOutputStream());
		writer.println("GET /" + filename + " HTTP/1.1");
		writer.println("Host:localhost");
		writer.println("connection:keep-alive");
		writer.println();
		writer.flush();

		/* ������Ӧ���� */
		reader = new DataInputStream(s.getInputStream());
		
		String first = reader.readLine();// "HTTP/1.1 200 OK"
		System.out.println(first);
		String second = reader.readLine();// "Content-Type:"
		System.out.println(second);
		String third = reader.readLine();// "Content-length:"
		System.out.println(third);
		String forth = reader.readLine();// blank line
		System.out.println(forth);
		
		while((response = reader.readLine())!=null) {
			System.out.println(response);
			if(response.equals(""))
				break;
		}
		
		/* ��ȡ��Ӧ���ݣ������ļ� */
		//success
		if (first.endsWith("OK")) {
			downLoad();
		}

		else {
			StringBuffer result = new StringBuffer();
			String line = null;
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
			System.out.print(result);//���������Ϣ
		}
	}
	
	/* �����ƶ��ļ�������Ŀ¼*/
	private static void downLoad() throws IOException {
		byte[] b = new byte[1024];
		System.out.println("��������");
		FileOutputStream out = new FileOutputStream(savelocation + "/" + filename, true);// ����������ļ�д������
		//int len = in.read(b);
		int len = reader.read(b);
		/* д���ļ� */
		while (len != -1) {
			out.write(b, 0, len);
			len = reader.read(b);
		}
		System.out.println("���ݴ������");
		reader.close();
		out.close();
	}
	
	/* src�ж� */
	private static void parse() {
		
	}
}
