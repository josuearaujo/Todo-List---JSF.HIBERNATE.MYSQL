package com.todo.pojo;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

@ManagedBean
@SessionScoped
public class App {
	private String email;
	private String textNewTodo;
	private List<Todo> lista;

	
	public App() {
		lista = new ArrayList<Todo>();
	}
	
	
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getTextNewTodo() {
		return textNewTodo;
	}



	public void setTextNewTodo(String textNewTodo) {
		this.textNewTodo = textNewTodo;
	}



	public List<Todo> getLista() {
		return lista;
	}



	public void setLista(List<Todo> lista) {
		this.lista = lista;
	}


	public void loadList(int mode) {
		Configuration con = new Configuration().configure().addAnnotatedClass(Todo.class);
		SessionFactory sf = con.buildSessionFactory();
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		Query q;
		switch(mode) {
			case 1: // LIST ALL
				q = session.createQuery("from Todo where email = :auxEmail").setParameter("auxEmail", this.email);
				lista = q.list();
				break;
			case 2: // LIST ACTIVE
				q = session.createQuery("from Todo where email = :auxEmail and status = :auxStatus").setParameter("auxEmail", this.email).setParameter("auxStatus", 1);
				lista = q.list();
				break;
			case 3: // LIST COMPLETED
				q = session.createQuery("from Todo where email = :auxEmail and status = :auxStatus").setParameter("auxEmail", this.email).setParameter("auxStatus", 0);
				lista = q.list();
				break;
		}			
	}
	
	public String addNewTodo(int mode) {
		if(this.textNewTodo.equals("")) {
			return "TodoList";
		}
		Todo nTD = new Todo();
		nTD.setName(this.textNewTodo);
		nTD.setEmail(this.email);
		nTD.setStatus(1);
		System.out.println("Adicionando new todo!");
		
		Configuration con = new Configuration().configure().addAnnotatedClass(Todo.class);		
		SessionFactory sf = con.buildSessionFactory();		
		Session session = sf.openSession();		
		Transaction tx = session.beginTransaction();
		
		session.save(nTD);
		
		tx.commit();
		
		this.textNewTodo = "";
		
		switch (mode) {
			case 1:
				return "TodoList";
			case 2:
				return "TodoListActive";
			case 3:
				return "TodoListCompleted";
			default:
				return "TodoList";
		}
		
		
	}
	
	public String removeTodo(Todo td, int mode) {
		Configuration con = new Configuration().configure().addAnnotatedClass(Todo.class);		
		SessionFactory sf = con.buildSessionFactory();		
		Session session = sf.openSession();		
		Transaction tx = session.beginTransaction();
		
		session.delete(td);
		
		tx.commit();
		
		switch (mode) {
		case 1:
			return "TodoList";
		case 2:
			return "TodoListActive";
		case 3:
			return "TodoListCompleted";
		default:
			return "TodoList";
		}
	}
	
	public String mudaStatus(Todo td, int mode) {
		Integer newStatus;
		newStatus = (td.getStatus() == 1 ? 0 : 1);
		System.out.println("Atualizando todo! Novo status = " + newStatus);
		System.out.println(td.getEmail());
		System.out.println(td.getName() );
		System.out.println(td.getTid());
		td.setStatus(newStatus);
		
		Configuration con = new Configuration().configure().addAnnotatedClass(Todo.class);		
		SessionFactory sf = con.buildSessionFactory();		
		Session session = sf.openSession();		
		Transaction tx = session.beginTransaction();
		
		session.update(td);
		
		tx.commit();
		
		switch (mode) {
		case 1:
			return "TodoList";
		case 2:
			return "TodoListActive";
		case 3:
			return "TodoListCompleted";
		default:
			return "TodoList";
		}
	}

	
	public static void main() {
		Todo teste = new Todo();
		teste.setTid(102);
		teste.setName("Josue");
		teste.setEmail("kpods@fsdf.com");
		teste.setStatus(1);
		System.out.println("Funcionando!");
		
		Configuration con = new Configuration().configure().addAnnotatedClass(Todo.class);
		
		SessionFactory sf = con.buildSessionFactory();
		
		Session session = sf.openSession();
		
		Transaction tx = session.beginTransaction();
		
		session.save(teste);
		
		tx.commit();
	}
}
