package com.eastedge.readnovel.view;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.widget.SeekBar;
import android.widget.TextView;

import com.eastedge.readnovel.common.Constants;
import com.eastedge.readnovel.utils.BookPageUtils;
import com.eastedge.readnovel.utils.DisplayUtil;
import com.readnovel.base.cache.lru.BMemCache;
import com.readnovel.base.util.DateUtils;
import com.readnovel.base.util.LogUtils;
import com.readnovel.base.util.PhoneUtils;
import com.xs.cn.R;

/**
 * 翻页页面生成
 * @author li.li
 *
 * Dec 19, 2012
 */
public class BookPageFactory {
	public static final int DEF_FOND_SIZE_INDEX = 1;//默认字大小 

	private File book_file = null; // 需要阅读的文件
	private MappedByteBuffer m_mbBuf = null; // 快速读取
	public int m_mbBufLen = 0; // 总长度
	public int m_mbBufBegin = 0; // 当前长度
	public int m_mbBufEnd = 0; // 结束位置
	private String m_strCharsetName = "utf-8"; // 文件编码
	//	private Bitmap bg; // 背景图片
	private int mWidth; // 屏幕的宽
	private int mHeight; // 屏幕的高
	private String title; // 标题
	private Vector<String> m_lines = new Vector<String>(); // 记录当前页显示的所以行数对应的内容
	private int[] textSize = new int[] { DisplayUtil.getFont1(), DisplayUtil.getFont2(), DisplayUtil.getFont3(), DisplayUtil.getFont4(),
			DisplayUtil.getFont5() }; // 字体大小改变集合
	private int m_fontSize; // 字间距
	public ArrayList<Integer> textColorList = new ArrayList<Integer>(); // 正文字体颜色的集合
	public ArrayList<Integer> topTextColorList = new ArrayList<Integer>(); // 顶部底部字体的颜色的集合
	private int marginWidth = DisplayUtil.readTextLeftRightSize(); // 左右与边缘的距离
	private int marginHeight = DisplayUtil.readTextTopBottomSize(); // 上下与边缘的距离
	private float mVisibleHeight; // 绘制内容的高
	private float mVisibleWidth; // 绘制内容的宽
	private boolean m_isfirstPage, m_islastPage; // m_isfirstPage 是否为第一页
													// m_islastPage 是否为最后一页
	private Context context;
	private Paint mPaint, pt, zPaint; // mPaint 正文的画笔 pt顶部跟底部小字的画笔 zPaint章节名的画笔
										// ，章节为粗体
	private SeekBar readjpseek; // 进度跳转的进度条
	private TextView jpTex; // 当前进度的值
	private boolean ff; // 判断是否从订阅那显示的， 订阅那 简单信息特殊处理
	private DecimalFormat df = new DecimalFormat("00.0"); // 底部显示的进度的格式
	private int p;
	private int[] wh;

	/**
	 * @param context
	 * @param w
	 *            屏幕宽
	 * @param h
	 *            屏幕 高
	 * @param fontSize
	 *            字体大小
	 * @param readjpseek
	 *            跳转进度条
	 * @param jpTex
	 *            显示当前进度的文本
	 */
	public BookPageFactory(Context context, int w, int h, int fontSize, SeekBar readjpseek, TextView jpTex) {
		this(context, w, h, fontSize);
		this.readjpseek = readjpseek;
		this.jpTex = jpTex;
	}

	/**
	 * @param context
	 * @param w
	 *            屏幕宽
	 * @param h
	 *            屏幕 高
	 * @param fontSize
	 *            字体大小
	 */
	public BookPageFactory(Context context, int w, int h, int fontSize) {
		this.context = context;
		// 获取R.array.zwcolor中定义的颜色集合
		String[] str1 = context.getResources().getStringArray(R.array.zwcolor);
		String[] str2 = context.getResources().getStringArray(R.array.orcolor);
		for (int i = 0; i < str1.length; i++) {
			// 将 类似#FFFFFF转化为对应的 颜色值 并放入对应的list中
			textColorList.add(Color.parseColor(str1[i]));
			topTextColorList.add(Color.parseColor(str2[i]));
		}

		// 记录屏幕的宽高
		mWidth = w;
		mHeight = h;

		// 初始化正文画笔
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTextAlign(Align.LEFT);
		mPaint.setTextSize(textSize[DEF_FOND_SIZE_INDEX]);
		mPaint.setColor(textColorList.get(0));

		// 初始化画笔 （章节名画笔 粗体）
		zPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		zPaint.setTextAlign(Align.LEFT);
		zPaint.setTextSize(textSize[2]);
		zPaint.setColor(textColorList.get(0));
		zPaint.setFakeBoldText(true);

		FontMetrics fm = mPaint.getFontMetrics();
		// 行间距
		int a = DisplayUtil.readTextFontSpaceSize();
		// (fm.descent-fm.ascent) 为字的高度 再加上 a 为行间距
		m_fontSize = (int) (fm.descent - fm.ascent) + a;

		// 初始化底部小字，顶部小标题 画笔
		pt = new Paint(Paint.ANTI_ALIAS_FLAG);
		pt.setTextAlign(Align.LEFT);
		pt.setTextSize(DisplayUtil.readTextTitleFondSize());
		pt.setColor(topTextColorList.get(0));

		// 可现实的宽 高。 减去上下左右间距后的
		mVisibleWidth = mWidth - marginWidth * 2;
		mVisibleHeight = mHeight - marginHeight * 2;
		// mLineCount = (int) (mVisibleHeight /m_fontSize); // 可显示的行数

		wh = PhoneUtils.getScreenPix((Activity) context);

	}

	/**
	 * 读取数据
	 * 
	 * @param strFilePath
	 *            文件路径
	 * @throws IOException
	 */
	public void openbook(String strFilePath) throws IOException {
		book_file = new File(strFilePath);
		long lLen = book_file.length();
		m_mbBufLen = (int) lLen;
		m_mbBuf = new RandomAccessFile(book_file, "r").getChannel().map(FileChannel.MapMode.READ_ONLY, 0, lLen);
		m_mbBufBegin = m_mbBufEnd = 0;
		m_lines.clear();
		if (readjpseek != null) {
			readjpseek.setMax(m_mbBufLen);
		}
	}

	/**
	 * 读取数据
	 * 
	 * @param f
	 *            文件对象
	 * @throws IOException
	 */
	public void openbook(File f) throws IOException {
		book_file = f;
		long lLen = book_file.length();
		m_mbBufLen = (int) lLen;
		m_mbBuf = new RandomAccessFile(book_file, "r").getChannel().map(FileChannel.MapMode.READ_ONLY, 0, lLen);
		m_mbBufBegin = m_mbBufEnd = 0;
		m_lines.clear();
		if (readjpseek != null) {
			readjpseek.setMax(m_mbBufLen);
		}
	}

	/**
	 * 读取数据
	 * 
	 * @param f
	 *            文件对象
	 * @param beg
	 *            开始位置
	 * @throws IOException
	 */
	public void openbook(File f, int beg) throws IOException {
		openbook(f, beg, false);
	}

	/**
	 * 读取数据
	 * 
	 * @param f
	 *            文件对象
	 * @param beg
	 *            开始位置
	 * @param ff
	 *            是否是订阅页面
	 * @throws IOException
	 */
	public void openbook(File f, int beg, boolean ff) throws IOException {
		this.ff = ff;
		book_file = f;
		long lLen = book_file.length();
		m_mbBufLen = (int) lLen;
		m_mbBuf = new RandomAccessFile(book_file, "r").getChannel().map(FileChannel.MapMode.READ_ONLY, 0, lLen);
		m_mbBufBegin = m_mbBufEnd = beg;
		m_lines.clear();
		if (readjpseek != null) {
			readjpseek.setMax(m_mbBufLen);
		}
	}

	/**
	 * 读取数据
	 * 
	 * @param f
	 *            文件对象
	 * @param beg
	 *            开始位置 0-size
	 * @param start
	 *            文件中这个章节的起始位置
	 * @param size
	 *            这个章节的长度
	 * @throws IOException
	 */
	public void openbook(File f, int beg, int start, int size) throws IOException {
		LogUtils.info("打开数据文件|" + f.getAbsolutePath());

		book_file = f;
		m_mbBufLen = size;
		m_mbBuf = new RandomAccessFile(book_file, "r").getChannel().map(FileChannel.MapMode.READ_ONLY, start, size);
		m_mbBufBegin = m_mbBufEnd = beg;
		m_lines.clear();
		if (readjpseek != null) {
			readjpseek.setMax(m_mbBufLen);
		}
	}

	/**
	 * 获取当前页 前两行简介信息， 存书签的时候用
	 * 
	 * @return
	 */
	public String getJJ() {
		if (m_lines != null && m_lines.size() > 0) {
			StringBuffer buff = new StringBuffer();
			buff.append(m_lines.get(0));
			if (m_lines.size() > 1) {
				buff.append(m_lines.get(1));
			}
			return buff.toString();
		}
		return "";
	}

	/**
	 * 读取上一个段落
	 * 
	 * @param nFromPos
	 *            当前位置
	 * @return
	 */
	protected byte[] readParagraphBack(int nFromPos) {
		int nEnd = nFromPos;
		int i;
		byte b0;

		// i = nEnd - 1;
		i = Math.min(nEnd - 1, m_mbBufLen);// 限制不要超出总长度，有时文件下载有错误

		// 当遇到0x0a（换行） 或者没有内容的时候结束
		while (i > 0) {
			b0 = m_mbBuf.get(i);
			if (b0 == 0x0a && i != nEnd - 1) {
				i++;
				break;
			}
			i--;
		}

		if (i < 0)
			i = 0;
		int nParaSize = nEnd - i;
		int j;
		byte[] buf = new byte[nParaSize];
		for (j = 0; j < nParaSize; j++) {
			buf[j] = m_mbBuf.get(i + j);
		}
		return buf;
	}

	/**
	 * 读取下一段落
	 * 
	 * @param nFromPos
	 *            当前位置
	 * @return
	 */
	protected byte[] readParagraphForward(int nFromPos) {
		int nStart = nFromPos;
		int i = nStart;
		byte b0;

		// 遇到换行结束
		while (i < m_mbBufLen) {
			b0 = m_mbBuf.get(i++);
			if (b0 == 0x0a) {
				break;
			}
		}
		int nParaSize = i - nStart;
		byte[] buf = new byte[nParaSize];
		for (i = 0; i < nParaSize; i++) {
			buf[i] = m_mbBuf.get(nFromPos + i);
		}

		return buf;
	}

	/**
	 * 获取下一页内容
	 * 
	 * @return
	 */
	protected Vector<String> pageDown() {
		String strParagraph = "";
		Vector<String> lines = new Vector<String>();
		int curHeight = 0;
		int fir = 0;

		// 判断高度 和当前的位置
		while (curHeight < mVisibleHeight && m_mbBufEnd < m_mbBufLen) {
			// 读取一个段落
			byte[] paraBuf = readParagraphForward(m_mbBufEnd);

			m_mbBufEnd += paraBuf.length;

			// 一个段落前加上空白高度
			curHeight += BookPageUtils.BLANK_LINE_HEIGHT;

			try {
				strParagraph = new String(paraBuf, m_strCharsetName);
			} catch (UnsupportedEncodingException e) {
				LogUtils.error(e.getMessage(), e);
			}
			// 结尾号
			String strReturn = "";
			if (strParagraph.indexOf("\r\n") != -1) {
				// 记录结尾符号，并替换为空
				strReturn = "\r\n";
				strParagraph = strParagraph.replaceAll("\r\n", "");
			} else if (strParagraph.indexOf("\n") != -1) {
				// 记录结尾符号，并替换为空
				strReturn = "\n";
				strParagraph = strParagraph.replaceAll("\n", "");
			}

			// 如果替换后所有的内容为空 即 ""
			if (strParagraph.length() == 0) {
				// 判断是否为第一行
				if (fir == 0) {
					try {
						// 开始位置加上 结尾符的长度
						m_mbBufBegin += strReturn.getBytes(m_strCharsetName).length;
					} catch (UnsupportedEncodingException e) {
						LogUtils.error(e.getMessage(), e);
					}
				} else {
					// 添加到显示行
					lines.add(strParagraph);
					// 空行高度加20；
					curHeight += 20;
				}
			}

			// 如果替换后的内容不为空
			while (strParagraph.length() > 0) {
				// 强制换行
				int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth, null);
				// 计算行高度
				curHeight += m_fontSize;
				if (curHeight >= mVisibleHeight) {
					break;
				}
				// 添加到显示行
				lines.add(strParagraph.substring(0, nSize));
				// 剩下的字符继续换行
				strParagraph = strParagraph.substring(nSize);
			}

			if (strParagraph.length() != 0) {
				try {
					// 除去结尾符的长度
					m_mbBufEnd -= (strParagraph + strReturn).getBytes(m_strCharsetName).length;
				} catch (UnsupportedEncodingException e) {
					LogUtils.error(e.getMessage(), e);
				}
			}
			fir++;
		}

		return lines;
	}

	/**
	 * 计算 前一页的开始位置 m_mbBufEnd
	 */
	protected void pageUp() {
		if (m_mbBufBegin < 0) {
			m_mbBufBegin = 0;
		}
		Vector<String> lines = new Vector<String>();
		int curHeight = 0;
		String strParagraph = "";

		// 判断高度跟 开始位置
		while (curHeight < mVisibleHeight && m_mbBufBegin > 0) {
			// 当前段的 显示行集合
			Vector<String> paraLines = new Vector<String>();
			byte[] paraBuf = readParagraphBack(m_mbBufBegin);
			m_mbBufBegin -= paraBuf.length;

			// 一个段落前加上空白高度
			curHeight += BookPageUtils.BLANK_LINE_HEIGHT;

			try {
				strParagraph = new String(paraBuf, m_strCharsetName);
			} catch (UnsupportedEncodingException e) {
				LogUtils.error(e.getMessage(), e);
			}

			// 替换所有的换行符
			strParagraph = strParagraph.replaceAll("\r\n", "");
			strParagraph = strParagraph.replaceAll("\n", "");

			// 内容为空时
			if (strParagraph.length() == 0) {
				paraLines.add(strParagraph);
				// 添加空白行高
				curHeight += 20;
			}

			// 内容不为空
			while (strParagraph.length() > 0) {
				// 分行
				int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth, null);
				paraLines.add(strParagraph.substring(0, nSize));
				curHeight += m_fontSize;
				// 获取分行剩下的 继续分
				strParagraph = strParagraph.substring(nSize);
			}
			lines.addAll(0, paraLines);
		}

		// 如果高溢出 则行有可能多了 要删除行
		while (curHeight > mVisibleHeight) {
			try {
				// 删除一行
				String n = lines.remove(0);
				m_mbBufBegin += n.getBytes(m_strCharsetName).length;
				// 计算删除后的高度
				if (n.length() == 0) {
					curHeight -= 20;
				} else {
					curHeight -= m_fontSize;
				}
			} catch (UnsupportedEncodingException e) {
				LogUtils.error(e.getMessage(), e);
			}
		}
		m_mbBufEnd = m_mbBufBegin;
		return;
	}

	/**
	 * 向前翻页
	 * 
	 * @throws IOException
	 */
	public void prePage() throws IOException {
		LogUtils.info("prePage");
		if (m_mbBufBegin <= 0) {
			m_mbBufBegin = 0;
			m_isfirstPage = true;
			return;
		} else {
			m_isfirstPage = false;
		}
		// 清空缓存行
		m_lines.clear();
		// 计算开始位置
		pageUp();
		// 获取显示内容
		m_lines = pageDown();
	}

	/**
	 * 向后翻页
	 * 
	 * @throws IOException
	 */
	public void nextPage() throws IOException {
		LogUtils.info("nextPage");
		// 判断是否为最后一页
		if (m_mbBufEnd >= m_mbBufLen) {
			m_islastPage = true;
			return;
		} else {
			m_islastPage = false;
		}
		// 清空缓存行
		m_lines.clear();
		m_mbBufBegin = m_mbBufEnd;
		// 获取显示内容
		m_lines = pageDown();
	}

	public void onDraw(final Canvas canvas) {
		LogUtils.info("onDraw");

		if (canvas == null)
			return;

		mPaint.setColor(textColorList.get(p));
		zPaint.setColor(textColorList.get(p));
		pt.setColor(topTextColorList.get(p));

		// 创建屏幕大小的背景
		String key = String.format(Constants.IMG_CACHE_KEY_PAGEWIDGET_BG, p);

		Bitmap cacheBM = BMemCache.getInstance().get(key);
		if (cacheBM == null || cacheBM.isRecycled()) {//缓存为空
			Bitmap bg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(((Activity) context).getResources(), Constants.READ_BG_LIST.get(p)),
					mWidth, wh[1], true);

			canvas.drawBitmap(bg, 0, 0, null);

			BMemCache.getInstance().put(key, bg);//放入缓存

		} else {//缓存不为空
			canvas.drawBitmap(cacheBM, 0, 0, null);
		}

		// 如果内容为空 则获取下一页内容， 这个为刚进入的时候还没翻页 获得第1页的内容
		if (m_lines.size() == 0) {
			m_lines = pageDown();
		}

		// 设置时候为最后一页的标识
		if (m_mbBufEnd >= m_mbBufLen) {
			m_islastPage = true;
		} else {
			m_islastPage = false;
		}

		if (m_lines.size() > 0) {

			int y = 18;
			int i = 0;
			for (String strLine : m_lines) {

				// 判断新段落时插入空白行
				if (BookPageUtils.checkIsParaEnd(strLine))
					y += BookPageUtils.BLANK_LINE_HEIGHT;// 增加一个空白高度

				// 解决乱码
				for (char cr : strLine.toCharArray()) {
					if (cr == BookPageUtils.IGNORE_CHAR) {
						LogUtils.info("onDraw|替换乱码");

						strLine = strLine.replace(cr, BookPageUtils.EMPTY_CHAR);
					}
				}

				if ("".equals(strLine)) {
					// 空行加 20边距
					y += 20;
					canvas.drawText(strLine, marginWidth, y, mPaint);
				} else if (m_mbBufBegin == 0 && ff) {
					// 订阅书本的时候第一行粗体
					y += m_fontSize;
					canvas.drawText(strLine, marginWidth, y, zPaint);
					ff = false;
				} else if (m_mbBufBegin == 0 && i == 0) {
					// 章节开始的时候 章节名粗体
					y += m_fontSize;
					canvas.drawText(strLine, marginWidth, y, zPaint);
				} else {
					// 章节内容
					y += m_fontSize;
					canvas.drawText(strLine, marginWidth, y, mPaint);
				}
				i++;
			}
		}

		// 当前百分比
		float fPercent = (float) (m_mbBufBegin * 1.0 / m_mbBufLen);

		// 设置进度条，设置为当前位置
		if (readjpseek != null) {
			readjpseek.setProgress(m_mbBufBegin);
		}

		String strPercent = "本章 " + df.format(fPercent * 100) + "%";

		// 客户要求 最后一页显示 100%
		if (m_islastPage == true) {
			strPercent = "本章 100%";
		}

		//快速跳转
		if (jpTex != null) {
			jpTex.setText(strPercent);
		}

		// 计算字的宽度
		int nPercentWidth = (int) pt.measureText(strPercent) + marginWidth;

		// 显示底部进度
		canvas.drawText(strPercent, mWidth - nPercentWidth, mHeight - DisplayUtil.readTextTimeBottomSpaceSize(), pt);

		//时间显示
		canvas.drawText(DateUtils.formatHM(new Date()), marginWidth, mHeight - DisplayUtil.readTextTimeBottomSpaceSize(), pt);

		//跟头部小标题
		if (title != null && !"".equals(title)) {
			canvas.drawText(title, marginWidth, DisplayUtil.readTextTitleTopSpaceSize(), pt);
		}
	}

	/**
	 * 设置小标题
	 * 
	 * @param t
	 *            小标题
	 */
	public void setTitle(String t) {
		this.title = t;
	}

	/**
	 * 设置正文 字体颜色
	 * 
	 * @param color
	 *            颜色
	 */
	public void setTextColor(int color) {
		mPaint.setColor(color);
		zPaint.setColor(color);
	}

	/**
	 * 是否为第一页
	 * 
	 * @return
	 */
	public boolean isfirstPage() {
		return m_isfirstPage;
	}

	/**
	 * 是否为最后一页
	 * 
	 * @return
	 */
	public boolean islastPage() {
		return m_islastPage;
	}

	/**
	 * 设置字体大小 并从新计算当前显示
	 * 
	 * @param fontsize
	 *            字体大小
	 */
	public void setFontSize(int fontsize) {
		// 设置字体大小
		setFontSize2(fontsize);
		m_lines.clear();
		m_mbBufEnd = m_mbBufBegin;
		// 重新获取数据
		m_lines = pageDown();
	}

	/**
	 * 设置字体大小
	 * 
	 * @param fontsize
	 *            字体大小
	 */
	public void setFontSize2(int fontsize) {
		mPaint.setTextSize(textSize[fontsize]);
		zPaint.setTextSize(textSize[fontsize]);
		int a = DisplayUtil.readTextFontSpaceSize();
		FontMetrics fm = mPaint.getFontMetrics();
		// 计算字间距
		m_fontSize = (int) (fm.descent - fm.ascent) + a;
	}

	/**
	 * 最后一页， 从当前章节向前翻页会到之前章节的最后一页
	 */
	public void last() {
		m_lines.clear();
		// 指向最后
		m_mbBufBegin = m_mbBufEnd = m_mbBufLen;
		// 计算开始位置
		pageUp();
		// 获取数据
		m_lines = pageDown();
	}

	/**
	 * 跳转到某个位置
	 * 
	 * @param p
	 *            位置
	 */
	public void jump(int p) {
		// 指向某个位置
		m_mbBufBegin = m_mbBufEnd = p;
		// 刷新
		c();
	}

	/**
	 * 向左跳 0.1%
	 */
	public void jumpleft() {
		int v = m_mbBufBegin - m_mbBufLen / 1000;
		if (v < 0)
			v = 0;
		m_mbBufBegin = m_mbBufEnd = v;
		c();
	}

	/**
	 * 向右跳 0.1%
	 */
	public void jumpRight() {
		int v = m_mbBufBegin + m_mbBufLen / 1000;
		if (v >= m_mbBufLen)
			v = m_mbBufLen - 1;
		m_mbBufBegin = m_mbBufEnd = v;
		c();
	}

	/**
	 * 刷新
	 */
	private void c() {
		m_lines.clear();
		m_lines = pageDown();
	}

	/**
	 * 更换背景
	 * 
	 * @param p
	 *            选的集合的位置
	 */
	public void cgbg(int p) {
		this.p = p;

	}

	/**
	 * 退出时候 清空数据
	 */
	public void destry() {
		m_lines.clear();
		if (m_mbBuf != null) {
			m_mbBuf.clear();
			m_mbBuf = null;
		}
	}

	public void resetHeight(int h) {
		mHeight = h;
		mVisibleHeight = h - marginHeight * 2;
	}
}
