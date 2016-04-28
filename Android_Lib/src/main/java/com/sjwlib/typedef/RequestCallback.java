package com.sjwlib.typedef;

public interface RequestCallback
{
	public void onSuccess(String content);

	public void onFail(String errorMessage);
}
