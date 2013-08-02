package org.fw.qq.plugins.screencut;


import java.io.File;
import java.util.ArrayList;

import javax.swing.filechooser.FileFilter;

/**
 * 文件过滤器
 * @author 逝水
 *
 */
public class MyFileFilter extends FileFilter{

	private String desc;
	
	private ArrayList<String> filters = new ArrayList<String>();
	
	public MyFileFilter()
	{
		
	}
	

	public MyFileFilter(String filter)
	{
		filters.add(filter);
	}
	

	public MyFileFilter(String filter,String desc)
	{
		filters.add(filter);
		this.desc = desc;
	}
	
	@Override
	public boolean accept(File arg0) {
		String fileName = arg0.getName().toLowerCase();
		//如果小于等于0显示所有文件
		if(fileName.length()==0)
		{
			return true;
		}
		if(arg0.isDirectory())
		{//显示文件夹
			return true;
		}
		//循环匹配过滤文件
		for(int i=0;i<filters.size();i++){
			String fileExt = filters.get(i);
			if(fileName.endsWith(fileExt)){
				return true;
			}
		}
		return false;
	}
	/**
	 * 添加过滤器
	 * @param str 过滤器名称 例如".jpg"
	 */
	public void addFilter(String str){
		this.filters.add(str);
	}
	/**
	 * 添加过滤器
	 * @param str 过滤器名称 例如".jpg"
	 * @param desc 此过滤器的描述。例如："JPG Images" 
	 */
	public void addFilter(String str,String desc)
	{
		this.filters.add(str);
		this.desc = desc;
	}
	/**
	 * 添加过滤器
	 * @param str 递多个 过滤器名称 例如:{".jpg",".gif"}
	 * @param desc 此过滤器的描述。例如："JPG and GIF Images"
	 */
	public void addFilter(String[] str,String desc){
		for(int i=0;i<str.length;i++)
		{
			System.out.println(str.length);
			this.filters.add(str[i]);
		}
		this.desc = desc;
	}
	/**
	 * 移除过滤器
	 * @param index 要移除的序号
	 */
	public void removeFilter(int index){
		this.filters.remove(index);
	}
	
	/**
	 * 清空过滤器
	 */
	public void removeAllFilter(){
		this.filters.removeAll(filters);
	}
	/**
	 * 设置过滤器描述
	 * @param desc 描述
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}


	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return desc;
	}

}
