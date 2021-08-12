package com.wjjzst.designpattern._17flyweight享元模式;

import java.util.HashMap;
import java.util.Map;

public class FlyweightPatternDemo {
	
	public static void main(String[] args) {
		Flyweight flyweight1 = FlyweightFactory.get("对象1"); 
		flyweight1.execute();
		
		Flyweight flyweight2 = FlyweightFactory.get("对象1"); 
		flyweight2.execute();
		
		System.out.println(flyweight1 == flyweight2);  
		
		// 享元
		// 享受，元数据
		// 同一个数据，我就认为是一个元数据，整个系统里这个数据就一份，缓存起来
		// 整个系统对这个数据，全部享受他一个对象实例即可
		
		// 直接既有内存来缓存一块数据，用享元模式
	}

	public static interface Flyweight {
		
		void execute();
		String getName();
		void setName(String name);
		
	}
	
	public static class ConcreteFlyweight implements Flyweight {

		private String name;
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public ConcreteFlyweight(String name) {
			super();
			this.name = name;
		}

		public void execute() {
			System.out.println(name + "执行功能逻辑");
		}
		
	}
	
	public static class FlyweightFactory {
		
		private static Map<String, Flyweight> cachePool = new HashMap<String, Flyweight>();
		
		public static Flyweight get(String name) {
			Flyweight flyweight = cachePool.get(name);
			if(flyweight == null) {
				flyweight = new ConcreteFlyweight(name);
				cachePool.put(name, flyweight);
			}
			return flyweight;
		}
		
	}
	
}
