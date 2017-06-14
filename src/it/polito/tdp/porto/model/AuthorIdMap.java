package it.polito.tdp.porto.model;

import java.util.HashMap;
import java.util.Map;


public class AuthorIdMap {
	
	private Map<Integer,Author> map ;
	
	public AuthorIdMap() {
		map = new HashMap<>() ;
	}
	
	public Author get(Integer id) {
		return map.get(id) ;
	}

	public Author put(Author author) {
		Author old = map.get(author.getId()) ; 
		if(old==null) {
			map.put(author.getId(), author) ;
			return author ;
		} else {
			return old ;
		}
	}
	
}
