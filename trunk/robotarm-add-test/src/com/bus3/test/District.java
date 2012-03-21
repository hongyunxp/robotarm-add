/**
 * 
 */
package com.bus3.test;

/**
 * @author li.li
 *
 * Mar 21, 2012
 *
 */
public class District {
	private String id;
	private String parentId;
	private String name;
	
	public District(String id,String parentId,String name){
		this.id=id;
		this.parentId=parentId;
		this.name=name;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
