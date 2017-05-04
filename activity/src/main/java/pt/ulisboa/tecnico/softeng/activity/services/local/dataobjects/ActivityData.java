package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.activity.domain.Activity;

public class ActivityData {

	private String name;
	private String code;
	private int minAge;
	private int maxAge;
	private int capacity;
	
	
	public ActivityData(){
	
	}
	
	public ActivityData(Activity activity){
		
		this.name = activity.getName();
		this.code = activity.getCode();
		this.minAge = activity.getMinAge();
		this.maxAge = activity.getMaxAge();
		this.capacity = activity.getCapacity();
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public int getMinAge() {
		return this.minAge;
	}

	public void setMinAge(int minAge) {
		this.minAge = minAge;
	}
	
	public int getMaxAge() {
		return this.maxAge;
	}

	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}	
	
	public int getCapacity() {
		return this.capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

}