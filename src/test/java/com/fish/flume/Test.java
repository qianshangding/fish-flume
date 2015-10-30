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
package com.fish.flume;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableList;

/**
 * @version 1.0
 * @author Fish
 */
public class Test {
	public static void main(String[] args) throws ParseException {
		String str = "[\"10.0.2.76\",\"-\",\"-\",\"GET / HTTP/1.1\",\"-\",\"/\",\"304\",\"0\",\"179\",\"346453\",\"1\",\"1446084943.565\",\".\",\"-\",\"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:41.0) Gecko/20100101 Firefox/41.0\",\"411\",\"0.000\",\"-\",\"29/Oct/2015:10:15:43 +0800\",\"-\"]";
		JSON.parseArray(str);

		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		System.out.println("list：" + list);
		ImmutableList<String> imlist = new ImmutableList.Builder<String>().build();
		System.out.println("imlist：" + imlist);
		imlist = ImmutableList.copyOf(list);
		System.out.println("imlist：" + imlist);

		

		List<String> list1 = new ArrayList<String>();
		list1.add("a1");
		list1.add("b1");
		list1.add("c1");
		imlist = ImmutableList.copyOf(list1);
		System.out.println("imOflist：" + imlist);
		
		
		String ss = "29/Oct/2015:16:51:01 +0800";
		DateFormat format = new SimpleDateFormat("dd/MMMMM/yyyy:HH:mm:ss z", Locale.ENGLISH);
		System.out.println(new Locale("en"));
		format.parse(ss);
	}
}
