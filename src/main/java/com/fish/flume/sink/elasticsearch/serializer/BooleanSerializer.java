/*
 * Copyright(c) 2015 gvtv.com.cn All rights reserved.
 * distributed with this file and available online at
 * http://www.gvtv.com.cn/
 */
package com.fish.flume.sink.elasticsearch.serializer;

import java.io.IOException;

import org.apache.flume.Context;
import org.elasticsearch.common.xcontent.XContentBuilder;

/**
 * @version 1.0
 * @author Fish
 */
public class BooleanSerializer implements Serializer {
	private String FIELD;

	@Override
	public void initialize(Context context, String field) {
		this.FIELD = field;
	}

	@Override
	public void serializer(XContentBuilder builder, String data) throws IOException {
		// TODO Auto-generated method stub
		builder.field(
				FIELD,
				!((data == null) || "false".equalsIgnoreCase(data) || "off".equalsIgnoreCase(data)
						|| "no".equalsIgnoreCase(data) || "0".equalsIgnoreCase(data) || "".equalsIgnoreCase(data)));
	}

	@Override
	public void configure(Context context) {
		// TODO Auto-generated method stub
	}
}
