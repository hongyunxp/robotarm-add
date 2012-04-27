package robot.arm.common;

import java.util.List;

import com.mokoclient.core.MokoClient;
import com.mokoclient.core.bean.PostBean;

public class Util {
	private static final int pageSize = 10;
	private static final int pageSizeDetail = 2;

	public static List<PostBean> getPostList(MokoClient vocationEnum, int curPage) {
		// TODO 我靠啊,没网络就崩溃了啊,要先检查有没有网络啊,坑爹!
		try {
			List<PostBean> result = null;

			if (result == null) {
				result = vocationEnum.getPostList(curPage, pageSize);

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
}
