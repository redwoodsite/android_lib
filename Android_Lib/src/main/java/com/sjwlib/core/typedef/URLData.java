package com.sjwlib.core.typedef;

public class URLData {
	private String address;
	private String key;
	private String netType;
	private long expires;

	public URLData(){

	}
	public URLData(String key,String netType){
		this.key = key;
		this.netType = netType;
	}

	public URLData(String key,String netType, String address){
		this.key = key;
		this.netType = netType;
		this.address = address;
	}

	public String getUrl() {
		if(address.endsWith("/"))
			return address + key;
		else
			return address + "/" + key;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}



}
