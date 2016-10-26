package com.sjwlib.core.net;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;
import com.sjwlib.R;
import com.sjwlib.core.typedef.URLData;

public class UrlConfigManager {

	public static URLData findAddress(Context context, final String findKey) {
		final XmlResourceParser xmlParser = context.getResources().getXml(R.xml.url);
		int eventCode;
		Map<String,String> keyList = new HashMap<String,String>();
		try {
			eventCode = xmlParser.getEventType();
			while (eventCode != XmlPullParser.END_DOCUMENT) {
				switch (eventCode) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						if ("Node".equals(xmlParser.getName())) {
							final String key = xmlParser.getAttributeValue(	null, "Key");
							if(!keyList.containsKey(key)) {
								keyList.put(key, xmlParser.getAttributeValue(null, "Url"));
							}
							if (keyList.containsKey(findKey)) {
								final URLData urlData = new URLData();
								String url = keyList.get(findKey) + xmlParser.getAttributeValue(null,"Url");
								urlData.setKey(findKey);
								urlData.setNetType(xmlParser.getAttributeValue(null, "netType"));
								urlData.setUrl(url);
								return urlData;
							}
						}
						break;
					case XmlPullParser.END_TAG:
						break;
					default:
						break;
				}
				eventCode = xmlParser.next();
			}
		} catch (final XmlPullParserException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			xmlParser.close();
		}
		return null;
	}
}