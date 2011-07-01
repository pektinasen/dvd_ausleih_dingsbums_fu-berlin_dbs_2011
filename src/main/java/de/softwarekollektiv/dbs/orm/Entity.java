package de.softwarekollektiv.dbs.orm;

import java.lang.reflect.Field;



public class Entity {
	public void save(){
		StringBuilder sqlUpdate = new StringBuilder();
		sqlUpdate.append("INSERT INTO ").
					append(this.getClass().getSimpleName()+"s ").
					append("VALUES (");

		
		for (Field field : this.getClass().getFields()){
//			sqlUpdate.append();
		}
	}
}
