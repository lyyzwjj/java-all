package com.wjjzst.designpattern._12command命令模式;

public class CommandPatternDemo {
	
	public static void main(String[] args) {
		Command commandA = new CommandA();
		Command commandB = new CommandB();
		
		Invoker invoker = new Invoker();
		
		invoker.setCommand(commandA); 
		invoker.execute();
		
		invoker.setCommand(commandB);
		invoker.execute();
		
		// 我给大家举个例子
		// 我之前在龙果讲了一个课程，就是缓存架构课程
		// 有两种请求要发送过来执行
		// 一种请求是读请求，一种请求是写请求，不同请求要执行的功能逻辑是不一样的
		// 此时就非常适合用这个命令模式
		// 将读请求的功能逻辑封装到ReadCommand里面去，将写请求的功能逻辑封装到WriteCommand里面去
		// 然后设置一个通用的一个命令执行的类
		// 读请求来了，就封装ReadCommand，交给同一个命令执行类来执行即可
		// 写请求来了，就封装WriteCommand，交给同一个命令感知性类来执行即可
		
		// 所以为什么要用这种模式呢？
		// 其实很多时候，用模式，是采用模式的合理的思想，去良好的设计你的代码
		// 你运用了模式之后，就可以很好的表达你的代码组件是干嘛的
		// 调用一个工具类的某个方法，太恶心了，从面相对象设计的角度，你没有任何面向对象的设计，你就是一个面向过程
		// 面向方法在执行这个功能
		// 如果你的面向对象的设计太烂的话，几乎没有设计，到了1年以后，你的系统的代码很难看懂，几乎没人能看懂
		// 但是反过来说，按照设计模式体现出的优秀的面向对象设计的思想，来规划你的代码的设计，可能几年以后，
		// 别人过来，不通命令，代表了不同的逻辑，代码就很好理解，可读性，维护性，扩展性
		
		// 构造器模式，builder，工厂模式
	}
	
	public interface Command {
		
		void execute();
		
	}
	
	public static class CommandA implements Command {
		
		public void execute() {
			System.out.println("命令A的功能逻辑");  
		}
		
	}
	
	public static class CommandB implements Command {
		
		public void execute() {
			System.out.println("命令B的功能逻辑"); 
		}
		
	}
	
	public static class Invoker {
		
		private Command command;
		
		public Command getCommand() {
			return command;
		}

		public void setCommand(Command command) {
			this.command = command;
		}

		public void execute() {
			System.out.println("一些别的逻辑A");
			command.execute();
			System.out.println("一些别的逻辑B");
		} 
		
	}
	
}
