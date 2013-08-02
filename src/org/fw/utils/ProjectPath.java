package org.fw.utils;

public class ProjectPath {

	/**
	 * 获取项目路径
	 * @return
	 */
	public static String getProjectPath(){
		return System.getProperty("user.dir")+System.getProperty("file.separator");
	}
}
