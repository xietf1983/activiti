package com.xtsoft.configuration;

public class Test {
	public static void main(String[] args) {
		String[] aaa =PropsUtil.getArray("aaa");
		for(String t:aaa){
			System.out.println(t);
		}
		System.out.println(PropsUtil.get("tt"));
		System.out.println(PropsUtil.get("mm"));

	}

}
