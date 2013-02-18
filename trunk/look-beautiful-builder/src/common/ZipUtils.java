package common;


import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

public class ZipUtils {

	public static void compressApks(String pathName, String destName) {
		File srcdir = new File(pathName);
		if (!srcdir.exists())
			throw new RuntimeException(pathName + "不存在！");

		File destFile = new File(destName);
		if (destFile.exists())
			destFile.delete();

		Project p = new Project();

		Zip zip = new Zip();
		zip.setProject(p);
		zip.setDestFile(new File(destName));//目标文件名

		FileSet fileSet = new FileSet();//文件组  
		fileSet.setProject(p);//工作
		fileSet.setDir(srcdir);//源目录
		fileSet.setIncludes("*.apk");

		zip.addFileset(fileSet);//加入压缩文件  
		zip.execute();//执行
	}

	public static void main(String[] args) {
		compressApks("E:\\work\\AndroidWorkSpace2\\auto-builder\\dist", "E:\\work\\AndroidWorkSpace2\\auto-builder\\dist\\test.zip");
	}
}
