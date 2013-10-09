package com.xs.cn.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import android.util.Log;

import com.eastedge.readnovel.beans.BFBook;
import com.eastedge.readnovel.common.Constants;
import com.readnovel.base.util.LogUtils;


public class Down {
//	private long starttime;

	private long ptime = 0;

//	private long usetime;
	public BFBook bookd;
	private String filename;// 文件名
	public String bookname;

//	protected JProgressBar progressBar;// 进度条

//	private String location;// 文件路径

	private String time; // 开始时间

	private String urllocation; // 文件全路径名

	private String filelocation;// 文件本地存储的路径名

	private URL url; // 下载文件

	private boolean flag = false;// 下载是否完成

	private boolean isstop = false;

	private int tnum;// 线程数

	long dsize;// 下载长度

	long m = 0;// 控制暂停时候下载的长度

	private Vector<DownThread> v = new Vector<DownThread>();

	private long fsize;// 文件大小

	long[] eachThreadstart;// 存放每个线程的开始

	long[] eachThreadbeg;// 存放每个线程的开始

	long[] eachThreadend;// 存放每个线程的长度

	public Down() {

	}

	public Down(URL url, int tnum,BFBook bookd1) {
		this.url = url;
		this.tnum = tnum;
		bookd=bookd1;
		bookname=bookd1.getTitle();
		this.filename = "book_text_"+bookd1.getArticleid()+".txt";
		filelocation = Constants.READNOVEL_BOOK  + File.separatorChar + filename;
		File f=new File(filelocation);
		f.getParentFile().mkdirs();
		if(f.exists()){
			f.delete();
		}
		try {
			f.createNewFile();
			urllocation = url.toString();
			URLConnection conn = url.openConnection();
			conn.connect();
			fsize = conn.getContentLength();
			LogUtils.info("文件长度   ------   "+fsize);
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}

	}

	class DownThread implements Runnable {
		long start;

		private URL u;

		long end;

		long size;

		long begin;

		long x;

		public DownThread(long start, long begin, long end) {
			this.start = start;
			this.begin = begin;
			this.end = end;
			LogUtils.info("启动下载   end："+end);
			try {
				u = new URL(urllocation);
			} catch (MalformedURLException e) {
				LogUtils.error(e.getMessage(), e);
			}
		}

		public void run() {
			LogUtils.info("启动下载2");
			byte[] bt = new byte[1024];
			int l = -1;
			InputStream in = null;
			RandomAccessFile r = null;
			size = begin - start;
			LogUtils.info(begin+"   开始下载       "+end);
			if (begin < end) {
				try {
					HttpURLConnection hcon = (HttpURLConnection) u
							.openConnection();
					hcon.setReadTimeout(0);
					hcon.setRequestProperty("RANGE", "bytes=" + begin + "-");
					in = hcon.getInputStream();
					r = new RandomAccessFile(filelocation, "rw");
					r.seek(begin);
					x = begin;
					while ((l = in.read(bt)) > 0 && x < end && !isstop) {
						if (l + x > end)
							l = (int) (end - x);
						r.write(bt, 0, l);
						x += l;
						size = x - start;
					}
					r.close();
					if (in != null)
						in.close();
					Thread.interrupted();
				} catch (Exception e) {
					LogUtils.error(e.getMessage(), e);
				}
			}
		}

	}

	private void startT(DownThread d) {
		new Thread(d).start();
	}

	public void start() {
		long tsize = fsize / tnum;
		eachThreadstart = new long[tnum];
		eachThreadend = new long[tnum];
		for (int i = 0; i < tnum; i++) {
			eachThreadstart[i] = i * tsize;
			eachThreadend[i] = tsize + i * tsize;
			if (i == tnum - 1)
				eachThreadend[i] = fsize;
		}
		if (eachThreadbeg == null) {
			eachThreadbeg = eachThreadstart;
		}

		for (int i = 0; i < tnum; i++) {
			DownThread dthread = new DownThread(eachThreadstart[i],
					eachThreadbeg[i], eachThreadend[i]);
			v.addElement(dthread);
			startT(dthread);
		}
	}

	public long downsize() {
		if (!isstop && !flag) {
			dsize = 0;
			StringBuffer buff = new StringBuffer();
			for (DownThread d : v) {
				if (d.x == 0)
					d.x = d.start;
				buff.append(d.x + ":" + d.end + " ");
				dsize += d.size;
			}

			try {
				PrintWriter w = new PrintWriter(filelocation + ".tmp");
				w.println(buff.toString());
				w.flush();
				w.close();
			} catch (FileNotFoundException e) {
				LogUtils.error(e.getMessage(), e);
			}

		}
		if (dsize == fsize) {
			flag = true;
		}
		return dsize;
	}

	public void stop() {
		isstop = true;
	}

	public void finish() {
		File f = new File(filelocation + ".tmp");
		f.delete();
//		JOptionPane.showMessageDialog(null, filename + " 下载完成,用了 " + usetime
//				+ " 秒", "完成", JOptionPane.INFORMATION_MESSAGE);

	}

	public String getFilename() {
		return filename;
	}

	public void setFilelocation(String filelocation) {
		this.filelocation = filelocation;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTime() {
		return time;
	}

	public long getFsize() {
		return fsize;
	}

	public String getUrllocation() {
		return urllocation;
	}

//	public JProgressBar getProgressBar() {
//		progressBar = new JProgressBar(0, (int) fsize);// 进度条,从0到文件大小
//		progressBar.setBackground(Color.RED);
//		progressBar.setForeground(Color.GREEN);
//		progressBar.setStringPainted(true);// 在进度条上显示百分比
//		progressBar.setValue((int) downsize());
//		progressBar.setPreferredSize(new Dimension(400, 15));
//		return progressBar;
//	}

	public boolean isFlag() {
		return flag;
	}

//	public long getUsetime() {
//		if (!flag && !isstop) {
//			long finishtime = System.currentTimeMillis();
//			usetime = (finishtime - starttime) / 1000;
//			usetime += ptime;
//		}
//		return usetime;
//	}

//	public long getStarttime() {
//		return starttime;
//	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setFsize(long fsize) {
		this.fsize = fsize;
	}

	public void setUrllocation(String urllocation) {
		this.urllocation = urllocation;
	}

	public String getFilelocation() {
		return filelocation;
	}

//	public void setStarttime(long starttime) {
//		this.starttime = starttime;
//	}

//	public String toString() {
//
//		return getFilename() + " " + downsize() + " " + getFilelocation() + " "
//				+ getUsetime() + " " + getTime() + " " + getUrllocation() + " "
//				+ getFsize() + "\n";
//	}

	public void setEachThread(long[][] a) {
		if (a != null) {
			this.eachThreadbeg = a[0];
			this.eachThreadend = a[1];
			tnum = a[1].length;
		}

	}

	public boolean Isstop() {
		return isstop;
	}

//	public void setUsetime(long usetime) {
//		this.usetime = usetime;
//	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void setIsstop(boolean isstop) {
		this.isstop = isstop;
	}

	public void setDsize(long dsize) {
		this.dsize = dsize;
	}

	public long getPtime() {
		return ptime;
	}

	public void setPtime(long ptime) {
		this.ptime = ptime;
	}

}
