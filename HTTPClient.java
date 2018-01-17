package client;

import java.net.*;
import java.io.*;

public class HTTPClient {
	static Socket s = null;// ����վ����ͨѶ��socket
	static String response = null;// ��վ���ص�����
	static String filename = null;// ����ʵ��ļ���
	static PrintStream writer = null;// �����
	static DataInputStream reader = null;// ������
	static int i = 0;//������
	static String savelocation = "E:";//�����ļ��ֿ�

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
		i = 0;
		while((response = reader.readLine())!=null) {
			i++;
			System.out.println(response);
			if(i == 7)
				break;
		}
		
		//��ȡ��Ӧ���ݣ������ļ�
		//success
		if (first.endsWith("OK")) {
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

		else {
			StringBuffer result = new StringBuffer();
			String line = null;
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
			System.out.print(result);//���������Ϣ
		}
	}
}
