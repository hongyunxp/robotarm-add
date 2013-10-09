/*
 * Copyright 2011 Sina.
 *
 * Licensed under the Apache License and Weibo License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.open.weibo.com
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eastedge.readnovel.weibo.net;


/**
 * Encapsulation a Weibo error, when weibo request can not be implemented successful.
 *
 * @author  ZhangJie (zhangjie2@staff.sina.com.cn)
 */
public class WeiboException extends Exception {

	private static final long serialVersionUID = 475022994858770424L;
	
	
	private int statusCode = -1;
	
	
	
    public WeiboException(String msg) {
        super(msg);
    }

    public WeiboException(Exception cause) {
        super(cause);
    }

    public WeiboException(String msg, int statusCode) {
        super(msg);
        this.statusCode = statusCode;
    }

    public WeiboException(String msg, Exception cause) {
        super(msg, cause);
    }

    public WeiboException(String msg, Exception cause, int statusCode) {
        super(msg, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }
    
    
	public WeiboException() {
		super(); 
	}

	public WeiboException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public WeiboException(Throwable throwable) {
		super(throwable);
	}

	public WeiboException(int statusCode) {
		super();
		this.statusCode = statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
}
