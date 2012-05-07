package robot.arm.common;

import java.util.List;

import com.mokoclient.core.MokoClient;
import com.mokoclient.core.bean.PostBean;

public class Util {
	public static final int COVER_COUNT = 6 * 8 * 10;
	public static final int PAGE_SIZE = 10;
	public static final int PAGE_COUNT = COVER_COUNT / PAGE_SIZE;
	
	private static final int pageSizeDetail = 1;

	public static List<PostBean> getPostList(MokoClient vocationEnum, int curPage) {
		// TODO 我靠啊,没网络就崩溃了啊,要先检查有没有网络啊,坑爹!
		try {
			List<PostBean> result = null;

			if (result == null) {
				result = vocationEnum.getPostList(curPage, PAGE_SIZE);

			}

			return result;
		} catch (Throwable e) {
			// TODO 他娘的抛异常了...
			e.printStackTrace();
		}
		return null;
	}

	public static List<String> getPostDetail(MokoClient vocationEnum, String detailUrl, int curPage) {
		// TODO 检查网络先
		try {
			List<String> result = null;

			if (result == null) {
				result = vocationEnum.getPostDetail(detailUrl, curPage, pageSizeDetail);

			}

			return result;
		} catch (Throwable e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public static void login() {
		// TODO 检查网络先
		try {
			new MokoClient.Login();
		} catch (Throwable e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
