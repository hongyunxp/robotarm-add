package robot.arm.common;

import java.util.List;

import com.mokoclient.core.MokoClient;
import com.mokoclient.core.bean.PostBean;
import com.mokoclient.core.bean.PostDetailBean;

public class Util {
	public static final int COVER_COUNT = 6 * 8 * 10;
	public static final int PAGE_SIZE = 10;
	public static final int PAGE_COUNT = COVER_COUNT / PAGE_SIZE;
	
	private static final int pageSizeDetail = 1;

	public static List<PostBean> getPostList(MokoClient vocationEnum, int curPage) {
		try {
			List<PostBean> result = null;

			if (result == null) {
				result = vocationEnum.getPostList(curPage, PAGE_SIZE);

			}

			return result;
		} catch (Throwable e) {
		}
		return null;
	}

	public static PostDetailBean getPostDetail(MokoClient vocationEnum, String detailUrl, int curPage) {
		try {
			PostDetailBean result = null;

			if (result == null) {
				result = vocationEnum.getPostDetail(detailUrl, curPage, pageSizeDetail);

			}

			return result;
		} catch (Throwable e) {
		}
		return null;
	}

	public static void login() {
		try {
			new MokoClient.Login();
		} catch (Throwable e) {
		}
	}
}
