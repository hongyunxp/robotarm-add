/**
 * Author : hmg25 Description :
 */
package com.readnovel.book.base.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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
import android.util.Log;
import android.widget.Toast;

import com.readnovel.book.base.R;
import com.readnovel.book.base.common.Constants;
import com.readnovel.book.base.sp.StyleSaveUtil;
import com.readnovel.book.base.utils.DisplayUtil;
import com.readnovel.book.base.utils.ResourceUtilss;
import com.readnovel.book.base.utils.StringUtils;
import com.readnovel.book.lru.BMemCache;

public class BookPageFactory {
	public StyleSaveUtil util;
	public ArrayList<Integer> textColorList = new ArrayList<Integer>(); // 正文字体颜色的集合
	private static final String TAG = BookPageFactory.class.getName();
	private File book_file;// 需要阅读的文件
	private MappedByteBuffer m_mbBuf;// 快速读取
	private int m_mbBufLen;// 总长度
	private int m_mbBufBegin;// 章节当前位置
	private int m_mbBufEnd;// 章节结束位置
	private String m_strCharsetName = "UTF-8";// 文件编码
	private int m_backColor = 0x000000; // 背景颜色
	private Bitmap m_book_bg;
	private int mWidth;// 屏幕的宽
	private int mHeight;// 屏幕的高
	private List<String> m_lines = new ArrayList<String>(); // 记录当前页显示的所有行数对应的内容
	BMemCache bm; // 章节为粗体
	public int m_fontSize;// 字间距
	private int marginWidth = 0; //    左右与边缘的距离
	private int marginHeight = 0; //   上下与边缘的距离
	private float mVisibleWidth;// 正文的宽度
	private float mVisibleHeight; // 绘制内容的高
	private boolean m_isfirstPage, m_islastPage;// 是否为第一页，是否为最后一页
	private Context context;
	private Paint mPaint, pt, zPaint;// mPaint 正文的画笔, pt顶部跟底部小字的画笔, zPaint章节名的画笔
	private String pageNum;// 页码
	private String foreText;// 当前开头的文字
	private String chapterName;
	public ArrayList<Integer> topTextColorList = new ArrayList<Integer>(); // 顶部底部字体的颜色的集合
	StyleSaveUtil sst;
	public Bitmap lightbp;

	public BookPageFactory(Context context, int mWidth, int mHeight) {
		this.context = context;
		this.mWidth = mWidth;
		this.mHeight = mHeight;
		String[] str1 = context.getResources().getStringArray(R.array.zwcolor);
		String[] str2 = context.getResources().getStringArray(R.array.orcolor);
		for (int i = 0; i < str1.length; i++) {
			// 将 类似#FFFFFF转化为对应的 颜色值 并放入对应的list中
			textColorList.add(Color.parseColor(str1[i]));
			topTextColorList.add(Color.parseColor(str2[i]));
		}
		util = new StyleSaveUtil(context);
		//设置左右边距和上下边距
		marginWidth = DisplayUtil.getLeftRightBianJu(context);
		marginHeight = DisplayUtil.getTopBottomBianJu(context);
		sst = new StyleSaveUtil(context);
		init();
	}

	/**
	 * 打开文件 读取数据
	 * 
	 * @param f
	 *            文件对象
	 * 
	 * @throws IOException
	 */
	public void openBook(String fileName) throws IOException {
		openBook(fileName, 0);
	}

	/**
	 * 读取数据
	 * 
	 * @param f
	 *            文件对象
	 * @param beg
	 *            开始位置
	 * 
	 * @throws IOException
	 */
	public void openBook(String fileName, int beg)  {
		book_file = new File(fileName);
		if (book_file.exists()) {
			long lLen = book_file.length();
			m_mbBufLen = (int) lLen;
			try {
				m_mbBuf = new RandomAccessFile(book_file, "r").getChannel().map(FileChannel.MapMode.READ_ONLY, 0, lLen);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			m_mbBufBegin = m_mbBufEnd = beg;
			m_lines.clear();
		}else{
			Toast.makeText(context, "文件不存在", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 向前翻页
	 * 
	 * @throws IOException
	 */
	public void prePage() throws IOException {
		if (m_mbBufBegin <= 0) {
			m_mbBufBegin = 0;
			m_isfirstPage = true;
			return;
		} else
			m_isfirstPage = false;
		m_lines.clear();
		pageUp();
		m_lines = pageDown();
	}

	/*
	 * 
	 * 设置背景
	 * 
	 */
	public void setBg(int bgid) {
		mPaint.setColor(textColorList.get(bgid));
		zPaint.setColor(textColorList.get(bgid));
		//		pt.setColor(topTextColorList.get(bgid));
		pt.setColor(textColorList.get(bgid));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		int[] wh = StringUtils.getScreenPix((Activity) context);
		if (bm == null)
			bm = BMemCache.getInstance();
		if (bm.get("bg" + bgid) == null) {
			bm.put("bg" + bgid, Bitmap.createScaledBitmap(
					BitmapFactory.decodeResource(context.getResources(), Constants.READ_BG_LIST.get(bgid), options), mWidth, wh[1], true));
		}
		m_book_bg = bm.get("bg" + bgid);
	}

	/**
	 * 向后翻页
	 * 
	 * @throws IOException
	 */
	public void nextPage() throws IOException {
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

	/**
	 * 文字渲染到画布上
	 * 
	 * @param c
	 *            需渲染的画布
	 */
	public void onDraw(final Canvas c) {
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
			// 设置背景
			if (m_book_bg == null)
				c.drawColor(m_backColor);
			else

				c.drawBitmap(m_book_bg, 0, 0, null);
			// y文本距上边距的距离
			int y = DisplayUtil.getTopBetweenContent(context);
			int i = 0;

			for (String strLine : m_lines) {
				if ("".equals(strLine)) {
					// 空行加 20边距 判断段之间的距离
					y += DisplayUtil.getDuanBetween(context);
					c.drawText(strLine, marginWidth, y, mPaint);
				} else if (m_mbBufBegin == 0 && i == 0) {
					// 章节开始的时候 章节名粗体
					y += m_fontSize;
					c.drawText(strLine, marginWidth, y, zPaint);
					chapterName = strLine;
				} else {
					// 章节内容
					y += m_fontSize;
					c.drawText(strLine, marginWidth, y, mPaint);
				}
				i++;
			}
		}

		// 每页开头的章节名的显示
		if (chapterName != null) {
			//		章节名距离上面的距离
			c.drawText(chapterName, marginWidth, DisplayUtil.getZhangjieBetweenTop(context), pt);

		}

		// 画每页显示的百分比
		if (pageNum != null)
			c.drawText(pageNum, mWidth / 2 - 30, mHeight - DisplayUtil.getDuanBetween(context), pt);

		lightbp = BitmapFactory.decodeResource(context.getResources(),
				ResourceUtilss.getDrawableResource(context, "light" + String.valueOf(setlight(sst.getlight()))));
		Bitmap bg = Bitmap.createScaledBitmap(lightbp,

		DisplayUtil.getLightHeightBetween(context),

		DisplayUtil.getLightWidthBetween(context),

		true);

		c.drawBitmap(bg,

		mWidth - DisplayUtil.getLightBetween(context),

		mHeight - DisplayUtil.getDuanBetween(context) - DisplayUtil.getone(context), null);

		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dFormat = new SimpleDateFormat("HH : mm");

		c.drawText(dFormat.format(date),

		mWidth - DisplayUtil.getTimeBetween(context),

		mHeight - DisplayUtil.getDuanBetween(context) - DisplayUtil.gettwo(context), pt);
	}

	public int setlight(int intLevel) {
		int m = intLevel / 10;
		int n = intLevel % 10;
		if (n >= 5)
			m += 1;
		return m * 10;
	}

	/**
	 * 设置字体大小
	 * 
	 * @param fontsize
	 *            字体大小
	 */
	public void setFontSize(int fontsize) {

		mPaint.setTextSize(fontsize);
		zPaint.setTextSize(fontsize);
		//		int a = 7;
		FontMetrics fm = mPaint.getFontMetrics();
		// 计算字间距  行高度
		m_fontSize = (int) (fm.descent - fm.ascent) + DisplayUtil.getHangHeight(context);
	}

	/**
	 * 设置阅读时的背景图片
	 * 
	 * @param BG
	 */
	public void setBgBitmap(Bitmap BG) {
		if (m_book_bg != null && !m_book_bg.isRecycled()) {
			m_book_bg.recycle();
		}
		m_book_bg = BG;

	}

	/**
	 * 是否是第一页
	 * 
	 * @return
	 */
	public boolean isfirstPage() {
		return m_isfirstPage;
	}

	/**
	 * 是否是最后一页
	 * 
	 * @return
	 */
	public boolean islastPage() {
		return m_islastPage;
	}

	/**
	 * 当前开始位置
	 * 
	 * @return
	 */
	public int getBufBegin() {
		return m_mbBufBegin;
	}

	/**
	 * 当前结束位置
	 * 
	 * @return
	 */
	public int getBufEnd() {
		return m_mbBufEnd;
	}

	/**
	 * 当前页码
	 * 
	 * @return
	 */
	public String getPageNum() {
		return pageNum;
	}

	/**
	 * 设置当前页码
	 * 
	 * @param pageNum
	 */
	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	/**
	 * 当前页开头文字
	 * 
	 * @return
	 */
	public String getForeText() {
		return foreText;
	}

	/**
	 * 设置当前的章节名
	 */

	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;

	}

	/*******************************************************************************
	 * 
	 * 以下是私有方法不对外
	 * 
	 *******************************************************************************/

	/**
	 * 初始化
	 */
	public void init() {
		// 正文
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(textColorList.get(sst.getreadbg()));
		mPaint.setTextSize(m_fontSize);
		//		mPaint.setTextSize(util.getFontsize());
		// 初始化画笔 （章节名画笔 粗体）
		zPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		zPaint.setTextAlign(Align.LEFT);
		zPaint.setFakeBoldText(true);
		zPaint.setColor(textColorList.get(sst.getreadbg()));
		zPaint.setTextSize(m_fontSize);
		//		zPaint.setTextSize(util.getFontsize());
		pt = new Paint(Paint.ANTI_ALIAS_FLAG);
		pt.setTextSize(DisplayUtil.getZhangJieFontSize(context));
		pt.setColor(textColorList.get(sst.getreadbg()));
		// 设置Paint的基准线
		mPaint.setTextAlign(Align.LEFT);
		mPaint.setTextSize(m_fontSize);
		// 文字区域的宽高 = 屏幕宽高   - 上下左右间距
		mVisibleWidth = mWidth - marginWidth * 2;
		mVisibleHeight = mHeight - marginHeight * 2;
	}

	public void resetHeight() {
		// 正文
		mVisibleWidth = mWidth - marginWidth * 2;
		mVisibleHeight = mHeight - marginHeight * 2;
	}

	/**
	 * Author : hmg25 Version: 1.0 Description :
	 */
	// 向上一页读进行的读计算
	private byte[] readParagraphBack(int nFromPos) {
		int nEnd = nFromPos;
		int i;
		byte b0, b1;
		if (m_strCharsetName.equals("UTF-16LE")) {
			i = nEnd - 2;
			while (i > 0) {

				b0 = m_mbBuf.get(i);
				b1 = m_mbBuf.get(i + 1);
				if (b0 == 0x0a && b1 == 0x00 && i != nEnd - 2) {
					i += 2;
					break;
				}
				i--;
			}

		} else if (m_strCharsetName.equals("UTF-16BE")) {
			i = nEnd - 2;
			while (i > 0) {
				b0 = m_mbBuf.get(i);
				b1 = m_mbBuf.get(i + 1);
				if (b0 == 0x00 && b1 == 0x0a && i != nEnd - 2) {
					i += 2;
					break;
				}
				i--;
			}
		} else {
			i = nEnd - 1;
			while (i > 0) {
				b0 = m_mbBuf.get(i);
				if (b0 == 0x0a && i != nEnd - 1) {
					i++;
					break;
				}
				i--;
			}
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

	// 向下一页读进行的读计算
	private byte[] readParagraphForward(int nFromPos) {
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

	// 翻页的方法
	private List<String> pageDown() {
		String strParagraph = "";
		List<String> lines = new ArrayList<String>();
		int curLine = 0;// 当前显示行数
		int curHeight = 0;// 当前页显示行高度
		// 判断高度 和当前的位置
		while (curHeight < mVisibleHeight && m_mbBufEnd < m_mbBufLen) {
			// 读取一段
			byte[] paraBuf = readParagraphForward(m_mbBufEnd);
			// 移动游标
			m_mbBufEnd += paraBuf.length;
			try {
				// 指定段落文字编码
				strParagraph = new String(paraBuf, m_strCharsetName);
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG, e.getMessage(), e);
			}

			// 结尾符
			String strReturn = "";
			if (strParagraph.indexOf("\r\n") != -1) {
				strReturn = "\r\n";
				strParagraph = strParagraph.replaceAll("\r\n", "");
			} else if (strParagraph.indexOf("\n") != -1) {
				strReturn = "\n";
				strParagraph = strParagraph.replaceAll("\n", "");
			}

			// 如果替换后所有的内容为空 即 ""
			if (strParagraph.length() == 0) {
				// 当为第一行时
				if (curLine == 0) {
					try {

						// 移动游标
						m_mbBufBegin += strReturn.getBytes(m_strCharsetName).length;
					} catch (UnsupportedEncodingException e) {
						Log.e(TAG, e.getMessage(), e);
					}
				} else {
					// 添加到显示行
					lines.add(strParagraph);
					// 空行高度加20；
					curHeight += DisplayUtil.getDuanBetween(context);
				}
			}
			// 当段落文字不为空时，递归
			while (strParagraph.length() > 0) {
				// 强制换行
				int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth, null);

				// 计算当前显示行高度
				curHeight += m_fontSize;
				// 当段落文字显示不下时跳出
				if (curHeight >= mVisibleHeight) {
					break;
				}
				// 添加到显示行
				lines.add(strParagraph.substring(0, nSize));
				// 剩下的字符继续换行
				strParagraph = strParagraph.substring(nSize);
			}
			// 当前段落文字不能显示的行回退
			if (strParagraph.length() != 0) {
				try {
					m_mbBufEnd -= (strParagraph + strReturn).getBytes(m_strCharsetName).length;
				} catch (UnsupportedEncodingException e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}
			curLine++;
		}
		// 页面开头的文字
		if (lines.iterator().hasNext()) {
			foreText = lines.iterator().next();
		}
		return lines;
	}

	// 向上翻页进行的放法
	private void pageUp() {

		if (m_mbBufBegin < 0)
			m_mbBufBegin = 0;
		Vector<String> lines = new Vector<String>();
		int curHeight = 0;
		String strParagraph = "";

		while (curHeight < mVisibleHeight && m_mbBufBegin > 0) {
			Vector<String> paraLines = new Vector<String>();
			byte[] paraBuf = readParagraphBack(m_mbBufBegin);
			m_mbBufBegin -= paraBuf.length;
			try {
				strParagraph = new String(paraBuf, m_strCharsetName);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			strParagraph = strParagraph.replaceAll("\r\n", "");
			strParagraph = strParagraph.replaceAll("\n", "");

			// 内容为空时
			if (strParagraph.length() == 0) {
				paraLines.add(strParagraph);
				// 添加空白行高
				curHeight += DisplayUtil.getDuanBetween(context);
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
					curHeight -= DisplayUtil.getDuanBetween(context);
				} else {
					curHeight -= m_fontSize;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		m_mbBufEnd = m_mbBufBegin;
		return;

	}

	public void setmHeight(int mHeight) {
		this.mHeight = mHeight;
	}

	public int getmHeight() {
		return mHeight;
	}

}
