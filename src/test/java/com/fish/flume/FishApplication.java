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

import java.io.File;
import java.util.List;
import java.util.Random;

import org.apache.flume.lifecycle.LifecycleAware;
import org.apache.flume.node.Application;
import org.apache.flume.node.PollingPropertiesFileConfigurationProvider;
import org.apache.flume.node.PollingZooKeeperConfigurationProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

/**
 * @version 1.0
 * @author Fish
 */
public class FishApplication {

	private File baseDir;

	@Before
	public void setup() throws Exception {
		// baseDir = Files.createTempDir();
	}

	@After
	public void tearDown() throws Exception {
		// FileUtils.deleteDirectory(baseDir);
	}

	@Test
	public void testFLUME1854() throws Exception {
		File configFile = new File(getClass().getClassLoader().getResource("fish-failover-balance.properties")
				.getFile());
		// Files.copy(new
		// File(getClass().getClassLoader().getResource("flume-conf.properties").getFile()),
		// configFile);
		Random random = new Random();
		// for (int i = 0; i < 3; i++) {
		try {
			// System.setProperty(Application.CONF_MONITOR_CLASS,
			// "org.apache.flume.instrumentation.http.HTTPMetricsServer");
			// System.setProperty(Application.CONF_MONITOR_PREFIX + "port",
			// "41414");

			EventBus eventBus = new EventBus("test-event-bus");
			PollingPropertiesFileConfigurationProvider configurationProvider = new PollingPropertiesFileConfigurationProvider(
					"fish1", configFile, eventBus, 30000);
			List<LifecycleAware> components = Lists.newArrayList();
			components.add(configurationProvider);
			Application application = new Application(components);
			eventBus.register(application);
			application.start();
			Thread.sleep(random.nextInt(1000000000));
			// application.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// }
	}

	/**
	 * 
	 * @Description:通过PollingPropertiesFileConfigurationProvider加载配置文件
	 * @throws Exception
	 * @author Fish
	 * @date 2015年10月27日
	 */
	@Test
	public void testFlumeToProps() throws Exception {
		File configFile = new File(getClass().getClassLoader().getResource("fish-es.properties").getFile());
		// File configFile = new
		// File(getClass().getClassLoader().getResource("fish-failover-balance.properties")
		// .getFile());
		// File configFile = new
		// File(getClass().getClassLoader().getResource("flume-zookeeper.properties").getFile());
		Random random = new Random();
		EventBus eventBus = new EventBus("test-event-bus");
		PollingPropertiesFileConfigurationProvider configurationProvider = new PollingPropertiesFileConfigurationProvider(
				"a1", configFile, eventBus, 1);
		List<LifecycleAware> components = Lists.newArrayList();
		components.add(configurationProvider);
		Application application = new Application(components);
		eventBus.register(application);
		application.start();
		Thread.sleep(random.nextInt(1000000000));
		// application.stop();
	}

	/**
	 * 
	 * @Description:通过PollingPropertiesFileConfigurationProvider加载配置文件
	 * @throws Exception
	 * @author Fish
	 * @date 2015年10月27日
	 */
	@Test
	public void testFlumeToZookeeper() throws Exception {
		Random random = new Random();
		EventBus eventBus = new EventBus("test-event-bus");
		PollingZooKeeperConfigurationProvider configurationProvider = new PollingZooKeeperConfigurationProvider("a1",
				"10.0.1.85:2181,10.0.1.86:2181,10.0.1.87:2181", "/flume", eventBus);
		List<LifecycleAware> components = Lists.newArrayList();
		components.add(configurationProvider);
		Application application = new Application(components);
		eventBus.register(application);
		application.start();
		Thread.sleep(random.nextInt(1000000000));
		// application.stop();
	}
}
