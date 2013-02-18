package common;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropetiesUtils {
	
	public static Properties getPropeties(String filePath){
		Properties props = null;
		InputStream in = null;
		try{
			in = new BufferedInputStream(new FileInputStream(filePath));
			props = new Properties();
			props.load(in);
		}catch(Throwable e){
			e.printStackTrace();
		}finally{
			if(in !=null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		return props;
	}
	
	
	public static void main(String[] args) throws IOException {
		String filePath = "auto-builder.properties";
		Properties props = getPropeties(filePath);
		System.out.println(props.getProperty("work_location"));
		System.out.println(props.getProperty("dist_location"));
	}
	
	
}
