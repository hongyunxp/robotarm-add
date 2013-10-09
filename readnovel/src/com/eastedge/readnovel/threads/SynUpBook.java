package com.eastedge.readnovel.threads;

import org.json.JSONObject;

import com.xs.cn.http.HttpImpl;

public class SynUpBook extends Thread {

	private String uid, token, add, del;

	public SynUpBook(String uid, String token, String add, String del) {
		this.uid = uid;
		this.token = token;
		this.add = add;
		this.del = del;
	}

	public void run() {
		//		JSONObject json = HttpComm.sendJSONToServer(Constants.URL + "/web/book.php?a=synchro_myfavor&uid=" + uid + "&token=" + token
		//				+ "&ct=android&data=json&add_ids=" + add + "&del_ids=" + del);
		
		JSONObject json = HttpImpl.synMyfavor(uid, token, add, del);
	}
}
