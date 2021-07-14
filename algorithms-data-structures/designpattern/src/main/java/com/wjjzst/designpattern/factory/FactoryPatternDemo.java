package com.wjjzst.designpattern.factory;

public class FactoryPatternDemo {
	
	public static void main(String[] args) {
		Product product = ProductFactory.create();
		product.execute();
		
		// 如果此时有100个地方都需要获取Product的实例
		// 但是此时Product实现类改了
		// 我们只要修改一个地方即可，就是ProductFacory中
	}
	
	public interface Product {
		
		void execute();
		
	}
	
//	public static class ProductImpl1 implements Product {
//
//		public void execute() {
//			System.out.print("产品1的功能实现");  
//		}
//		
//	}
	
	public static class ProductImpl2 implements Product {

		public void execute() {
			System.out.print("产品1的功能实现");  
		}
		
	}
	
	public static class ProductFactory {
		
		public static Product create() {
			return new ProductImpl2();
		}
		
	}
	
}
