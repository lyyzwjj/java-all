package com.wjjzst.designpattern.observer;

import java.util.Observable;
import java.util.Observer;

public class ObserverPatternDemo {
	
	public static void main(String[] args) {
		Subject subject = new Subject(0); 
		
		Observer observer = new ConcreteObserver();
		subject.addObserver(observer); 
		
		subject.setState(1);
		subject.setState(2);  
	}
	
	public static class Subject extends Observable {
		
		private Integer state;
		
		public Subject(Integer state) {
			this.state = state;
		}
		
		public Integer getState() {
			return state;
		}
		public void setState(Integer state) {
			// 在这里状态就改变了
			this.state = state;
			// 通知关联的一些观察者，说我的状态变化了
			this.setChanged();
//			this.notifyObservers(state);  
			this.notifyObservers();
		}
		
	}
	
	public static class ConcreteObserver implements Observer {

		public void update(Observable o, Object arg) {
//			Integer state = (Integer) arg;
			Subject subject = (Subject) o;
			Integer state = subject.getState();
			System.out.println("目标对象的状态变化成：" + state);  
		}
		
	}
	
}
