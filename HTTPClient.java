package client;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

/**
 * HTTPClient: HTTP�ͻ��� ����HTTP/1.1Э�飬����ʵ�ֶ�������վ��GET��POST���� �ṩ�򵥵Ľ������ܡ�
 * ֧��HTML�����飬��HTML�����а������ļ�һ�����ء�
 * 
 * @author Raven
 *
 */
public class HTTPClient {
	static Socket s = null;// ����վ����ͨѶ��socket
	static String response = null;// ��վ���ص�����
	static String filename = null;// ����ʵ��ļ���
	static String fileName = null;// ���ļ���
	static String addr = null;// �ļ�·��
	static PrintStream writer = null;// �����
	static DataInputStream reader = null;// ������
	static int i = 0;// ������
	static String savelocation = "./clientdb";// �����ļ��ֿ�
	static String address = null;// Ŀ¼
	static Scanner in = null;
	static String url = null;
	static int status = 0;

	public static void main(String[] args) throws IOException {
		/* ���ӷ����� */
		/*
		 * System.out.println("����������ʵ���վ����"); in = new Scanner(System.in); url =
		 * in.toString(); System.out.println(url);
		 */
		
		
		get("index.html");
		s.close();
	}

	private static void get(String root) throws IOException {
		//s = new Socket(InetAddress.getLocalHost(), 8080);
		s = new Socket(InetAddress.getByName("www.lib.neu.edu.cn"), 80);
		System.out.println("������������");
		/* ��������ͷ */
		filename = root;
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

		while ((response = reader.readLine()) != null) {
			System.out.println(response);
			if (response.equals(""))
				break;
		}

		/* ��ȡ��Ӧ���ݣ������ļ� */
		// success
		if (first.endsWith("OK")) {
			downLoad();
		}

		else {
			StringBuffer result = new StringBuffer();
			String line = null;
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
			System.out.print(result);// ���������Ϣ
		}
	}
	
	/* ����ָ���ļ�������Ŀ¼ */
	private static void downLoad() throws IOException {
		address = savelocation + "/" + filename.substring(0, filename.lastIndexOf("."));//address:�ļ������ַ
		//System.out.println(address);

		/* �ж��ļ����ͣ������html�����½��ļ��� */
		if (filename.substring(filename.length() - 4, filename.length()).equals("html")) {
			File l = new File(address);
			l.mkdirs();
			savelocation = address;
		}
		if(status == 1) {
			
		}
		byte[] b = new byte[1024];
		System.out.println("��������");
		try{
			FileOutputStream out = new FileOutputStream(savelocation + "/" + filename, true);// ����������ļ�д������
			int len = reader.read(b);
			/* д���ļ� */
			while (len != -1) {
				out.write(b, 0, len);
				len = reader.read(b);
			}
			reader.close();
			writer.close();
			
			/* �ж��ļ����ͣ������html��������жϲ��� */
			if (filename.substring(filename.length() - 4, filename.length()).equals("html")) {
				System.out.println("��ʼ����");
				/*�����ļ����*/
				parse();
			}
			
			System.out.println("���ݴ���������ļ��Ѵ��뱾��");
			savelocation = "./database";
			out.close();
			s.close();
		}catch(FileNotFoundException e) {
			String temp = null;
			File l = new File(savelocation + "/" + addr);
			//System.out.println("savelocation:"+savelocation+"  filename"+filename);
			//System.out.println("savelocation:"+savelocation+"  addr"+addr);
			System.out.println(addr);
			File m=new File(l.getPath());
			m.mkdirs();
			System.out.println(filename);
			System.out.println(savelocation);
			get(filename);
			filename = fileName;
			savelocation = savelocation+addr;
			downLoad();
		}
	}

	/* src�ж� */
	private static void parse() throws IOException {
		FileInputStream in = new FileInputStream(savelocation + "/" + filename);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		String html = null;//��ȡ����html����
		byte[] b = new byte[1024];
		System.out.println("��ȡhtml��");
		int length = -1;
		while ((length = in.read(b)) != -1) {
			bos.write(b, 0, length);
		}

		bos.close();
		in.close();
		html = bos.toString();
		//System.out.println(html);

		List pics = getImgSrc(html);
		System.out.println(pics.toString());
		String add = pics.toString();//����src�ļ�Ŀ¼
		add=add.replaceAll("\\[", "");
		add=add.replaceAll("\\]", "");
		System.out.println(add);
		StringTokenizer temp = new StringTokenizer(add, " ");// �ָ�Ա��ȡȫ��src
		String[] lists = new String[90];
		String uuu = temp.toString();
		i = 0;
		while (true) {
			try {
				lists[i] = uuu;
				uuu = temp.nextToken();
				System.out.println(uuu);
				i++;
				if (uuu.equals(null))
					break;
			} catch (NoSuchElementException e) {
				break;
			}
		}
		//System.out.println("&"+i);
		int j = 0;
		for (j = 0; j < i; j++) {
			filename = lists[i].replaceAll(",", "");
			status = 1;
			File tempFile =new File( filename .trim());  
		    fileName = tempFile.getName(); //filename:��ҳ�ļ����������ļ��У�
		    addr = filename.replaceAll(fileName, "");
		    System.out.println(filename.substring(1, filename.length()));
		    //.out.println("*addr: "+addr);
		    //System.out.println("fileName"+fileName);
			get(filename.substring(1, filename.length()));//filename:��ҳ�ļ��������ļ��У�
		}

	}

	private static List<String> getImgSrc(String htmlStr) {
		String img = "";
		Pattern p_image;
		Matcher m_image;
		List<String> pics = new ArrayList<String>();
		// String regEx_img = "<img.*src=(.*?)[^>]*?>"; //ͼƬ���ӵ�ַ
		String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
		p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
		m_image = p_image.matcher(htmlStr);

		while (m_image.find()) {
			img = img + "," + m_image.group();
			// Matcher m =
			// Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(img); //ƥ��src
			Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
			while (m.find()) {
				pics.add(m.group(1));
			}
		}
		return pics;
	}
}
