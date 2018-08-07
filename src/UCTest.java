import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.io.InputStream;

public class UCTest {

	public static void main(String[] args) {
		int c;
		String urlStr = "http://www.baidu.com";
		URL hp = null;
		try {
			hp = new URL(urlStr);
		} catch (MalformedURLException e) {
			System.err.println("Invalid format of URL:" + hp + "," + e.getMessage());
			return;
		}
		URLConnection hpCon = null;
		try {
			hpCon = hp.openConnection();
		} catch (IOException e) {
			System.err.println("Can't get connection from URL:" + e.getMessage());
		}
		System.out.println("Date :"+ new Date(hpCon.getDate()));
		System.out.println("Content-Type :"+ hpCon.getContentType());
		System.out.println("Expires :"+ hpCon.getExpiration());
		System.out.println("Last-Modified :"+ new Date(hpCon.getLastModified()));
		int len=hpCon.getContentLength();
		System.out.println("Content-length :"+len);
		
		if(len>0) {
			System.out.println("===Content===");
			
			try {
				InputStream input=hpCon.getInputStream();
				int i=len;
				while(((c=input.read())!=-1)&&(--i>0)) {
					System.out.print((char)c);
				}
				input.close();
			}catch(IOException e) {
				System.err.println("I/O failed :"+e.getMessage());
			}
		}else {
			System.out.println("No Content Available");
		}
	}

}
