package org.fw.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Config {

	private static Logger log = Logger.getLogger(Config.class);
	
	private String path;

	private Properties config;
	
	

	public Config() {
	}

	public Config(String path) {
		config = new Properties();
		setPath(path);
	}

	/**
	 * 将此 Properties 表中的属性列表（键和元素对）写入输出流
	 * 
	 * @param comments
	 */
	public void saveConfig(String comments) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path);
			config.store(fos, comments);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("读取配置文件失败");
		}
		if (fos != null) {
			try {
				fos.close();
			} catch (Exception e) {
				log.info("无法关闭数据流");
				e.printStackTrace();
			}
		}
	}

	public void savePropertie(String key, String value, String comments) {
		setPropertie(key, value);
		saveConfig(comments);
	}

	public void savePropertie(String key, String value) {
		savePropertie(key, value, null);
	}

	private void setPropertie(String key, String value) {
		config.setProperty(key, value);
	}
	/**
	 * 获取属性值
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		return config.getProperty(key);
	}
	/**
	 * 获取属性值，为空则返回默认值
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getProperty(String key, String defaultValue) {
		String value = getProperty(key);
		value = value != null ? value : defaultValue;
		return value;
	}

	public void remove(Object key) {
		config.remove(key);
	}

	public void removeAndSave(Object key) {
		remove(key);
		saveConfig();
	}

	public void saveConfig() {
		saveConfig(null);
	}

	/**
	 * 设置配置文件路径
	 * 
	 * @param path
	 */
	private void setPath(String path) {
		this.path = path;
		checkExist();
		loadConfig();
	}

	public String getPath() {
		return path;
	}

	/**
	 * 载入配置文件
	 * 
	 */
	private void loadConfig() {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
			config.clear();
			config.load(fis);
		} catch (Exception e) {
			log.info("载入配置文件时出现异常");
			e.printStackTrace();
		}
		if (fis != null) {
			try {
				fis.close();
			} catch (Exception e) {
				log.info("无法关闭数据流");
				e.printStackTrace();
			}
		}
	}

	/**
	 * 检查是否存在该文件
	 * 
	 */
	private void checkExist() {
		File file = new File(path);
		File parent = file.getParentFile();
		if (!parent.exists())
			parent.mkdirs();
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				log.info("创建新的配置文件失败");
				e.printStackTrace();
			}
		}
	}
	
}
