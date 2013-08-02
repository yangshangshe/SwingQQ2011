package org.fw.db.pojo;

import java.util.List;

import org.fw.utils.HibernateSessionFactory;
import org.hibernate.Session;


public class UserDAO {
	
	
	
	@SuppressWarnings("unchecked")
	public static User login(String number, String password)
    {
		 Session session  = HibernateSessionFactory.getSession();
		 List<User> list = session.createQuery("from User where number = "+number +" and password = "+password).list();
		 if(list.size()>0){
			 return list.get(0);
		 }else{
			 return null;
		 }
    }
	
	public static boolean Register(String number,String password){
		Session session = HibernateSessionFactory.getSession();
		User user = new User();
		user.setNumber(number);
		user.setPassword(password);
		session.save(user);
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public static List<User> getFriends(String userids){
		 Session session  = HibernateSessionFactory.getSession();
		 List<User> list = session.createQuery("from User where id in("+userids+")").list();
		return list;
	}
}
