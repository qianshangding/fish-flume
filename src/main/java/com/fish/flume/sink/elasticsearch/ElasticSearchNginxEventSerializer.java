/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * \"License\"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.fish.flume.sink.elasticsearch;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.conf.ComponentConfiguration;
import org.apache.flume.sink.elasticsearch.ContentBuilderUtil;
import org.apache.flume.sink.elasticsearch.ElasticSearchEventSerializer;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.fish.flume.sink.elasticsearch.serializer.Serializer;
import com.fish.flume.sink.elasticsearch.serializer.SerializerType;
import com.fish.flume.sink.elasticsearch.serializer.StringSerializer;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class ElasticSearchNginxEventSerializer implements ElasticSearchEventSerializer {
	public static final Logger logger = LoggerFactory.getLogger(ElasticSearchNginxEventSerializer.class);

	public static final String DEFAULT_SERIALIZER = "string";

	public static final Serializer DEFAULT_SERIALIZER_OBJECT = new StringSerializer();

	private static final String FIELDS = "fields";

	private static ImmutableMap<String, Serializer> serializers = new ImmutableMap.Builder<String, Serializer>()
			.build();
	ImmutableList<String> fieldList = new ImmutableList.Builder<String>().build();

	@Override
	public XContentBuilder getContentBuilder(Event event) throws IOException {
		XContentBuilder builder = jsonBuilder().startObject();
		appendBody(builder, event);
		appendHeaders(builder, event);
		return builder;
	}

	private void appendBody(XContentBuilder builder, Event event) throws IOException, UnsupportedEncodingException {
		String body = new String(event.getBody(), charset);
		List<String> list = JSON.parseArray(body, String.class);
		for (int index = 0; index < list.size(); index++) {
			serializerData(builder, fieldList.get(index), list.get(index));
		}
	}

	private void serializerData(XContentBuilder builder, String key, String data) throws IOException,
			UnsupportedEncodingException {
		Serializer serializer = getSerializer(key);
		serializer.serializer(builder, data);
	}

	private Serializer getSerializer(String key) {
		Serializer result = serializers.get(key);
		if (result != null) {
			return result;
		}
		return DEFAULT_SERIALIZER_OBJECT;
	}

	private void appendHeaders(XContentBuilder builder, Event event) throws IOException {
		Map<String, String> headers = event.getHeaders();
		for (String key : headers.keySet()) {
			ContentBuilderUtil.appendField(builder, key, headers.get(key).getBytes(charset));
		}
	}

	@Override
	public void configure(Context context) {

		String fieldsStr = context.getString(FIELDS);
		Preconditions.checkArgument(!StringUtils.isEmpty(fieldsStr), "ElasticSearchNginxEventSerializer至少有一个字段");

		String[] fields = fieldsStr.split("\\s+");

		Map<String, Serializer> map = new HashMap<String, Serializer>();
		Context serializerContext = new Context(context.getSubProperties(FIELDS + "."));
		for (String field : fields) {
			Context fieldContext = new Context(serializerContext.getSubProperties(field + "."));
			String clazzName = fieldContext.getString("serializer", DEFAULT_SERIALIZER);
			try {
				map.put(field, newInstance(field, clazzName, fieldContext));
			} catch (ClassNotFoundException e) {
				Throwables.propagate(e);
			} catch (InstantiationException e) {
				Throwables.propagate(e);
			} catch (IllegalAccessException e) {
				Throwables.propagate(e);
			}
		}
		fieldList = ImmutableList.copyOf(fields);
		serializers = ImmutableMap.copyOf(map);
	}

	@SuppressWarnings("unchecked")
	private Serializer newInstance(String field, String clazzName, Context context) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {

		Class<? extends Serializer> clazz = null;
		try {
			clazz = SerializerType.valueOf(clazzName.toUpperCase(Locale.ENGLISH)).getBuilderClass();
		} catch (IllegalArgumentException e) {
		}
		if (clazz == null) {
			clazz = (Class<? extends Serializer>) Class.forName(clazzName);
		}
		Serializer serializer = clazz.newInstance();
		serializer.initialize(context, field);
		return serializer;
	}

	@Override
	public void configure(ComponentConfiguration conf) {
		// NO-OP...
	}

	public static ImmutableMap<String, Serializer> getSerializers() {
		return serializers;
	}
}
