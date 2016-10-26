package com.sjwlib.core.typedef;

public class URLData {
	private String key;
	private long expires;
	private String netType;
	private String url;

	public URLData(){

	}
	public URLData(String key,String netType){
		this.key = key;
		this.netType = netType;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getExpires() {
		return expires;
	}

	public void setExpires(long expires) {
		this.expires = expires;
	}

	public String getNetType() {
		return netType;
	}

	public void setNetType(String netType) {
		this.netType = netType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
