/**
 * 
 */
package robot.arm.utils;

/**
 * @author li.li
 *
 * Apr 12, 2012
 *
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ZipUtil {

	// 压缩
	public static String compress(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(str.getBytes());
		gzip.close();
		return out.toString("ISO-8859-1");
	}

	// 解压缩
	public static String uncompress(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
		GZIPInputStream gunzip = new GZIPInputStream(in);
		byte[] buffer = new byte[256];
		int n;
		while ((n = gunzip.read(buffer)) >= 0) {
			out.write(buffer, 0, n);
		}

		// toString()使用平台默认编码，也可以显式的指定如toString("GBK")
		return out.toString();
	}

	// 测试方法
	public static void main(String[] args) throws IOException {
		String c = "中国China中国China中国China中国China中国China中国China中国China中国China中国China中国China";

		System.out.println(c.getBytes().length + "|" + ZipUtil.compress(c).getBytes().length);

		System.out.println(ZipUtil.uncompress(ZipUtil.compress(c)));
	}

}
