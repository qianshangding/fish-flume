/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.fish.flume;

import org.junit.Test;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * @version 1.0
 * @author Fish
 */
public class TestGuava {
	class TestEvent {
		private final int message;

		public TestEvent(int message) {
			this.message = message;
			System.out.println("event message:" + message);
		}

		public int getMessage() {
			return message;
		}
	}

	class EventListener {
		public int lastMessage = 0;

		@Subscribe
		public void listen(TestEvent event) {
			lastMessage = event.getMessage();
			System.out.println("监控到message变化成:" + lastMessage);
		}

		public int getLastMessage() {
			return lastMessage;
		}
	}

	@Test
	public void testReceiveEvent() throws Exception {

		EventBus eventBus = new EventBus("test");
		EventListener listener = new EventListener();

		eventBus.register(listener);

		eventBus.post(new TestEvent(200));
		eventBus.post(new TestEvent(300));
		eventBus.post(new TestEvent(400));

		System.out.println("最后message的值是:" + listener.getLastMessage());

		eventBus.unregister(listener);

		eventBus.post(new TestEvent(500));

		System.out.println("没有监控的情况下，最后message的值是:" + listener.getLastMessage());
	}
}
