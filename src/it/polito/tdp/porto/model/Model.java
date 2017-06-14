package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {

	PortoDAO dao; 
	private UndirectedGraph<Author, DefaultEdge> graph ;
	private AuthorIdMap authorIdMap ;
	private List<Author> autori ;
	private PaperIdMap paperIdMap ;
	private List<Paper> articoli ;

	public Model() {
		this.dao = new PortoDAO();
		this.authorIdMap = new AuthorIdMap() ;
		this.paperIdMap = new PaperIdMap();
	}

	private UndirectedGraph<Author, DefaultEdge> getGrafo() {
		if(this.graph==null) {
			this.creaGrafo();
		}
		return this.graph ;
	}
	
	public void creaGrafo(){

		this.graph = new SimpleGraph<Author, DefaultEdge>(DefaultEdge.class) ;
		
		Graphs.addAllVertices(graph, this.getAllAutori()) ;

		for(Author a1 : autori) {
			for (Author a2 : autori) {
				if(!a1.equals(a2)) {
					for(Paper p1 : a1.getArticoli()) {
						for(Paper p2 : a2.getArticoli()) {
							if(p1.equals(p2)) 
								graph.addEdge(a1, a2);		
						}
					}
				}
			}
		}
		
	}
	
	public List<Author> getAllAutori() {
		if(this.autori==null) {
			this.autori = dao.getAllAutori(authorIdMap) ;
			for(Author a : autori) {
				a.setArticoli(dao.getArticoliPerAutore(a, paperIdMap));
			}
				
		}
		return this.autori ;
	}
	
	public List<Paper> getAllArticoli() {
		if(this.articoli == null) {
			this.articoli = dao.getAllArticoli(paperIdMap);
			for(Paper p : articoli) {
				p.setAutori(dao.getAutoriPerArticolo(p, authorIdMap));
			}
		}
		return this.articoli;
	}
	
	public List<Author> trovaCoautori(Author a) {
		
		List<Author> coautori = new ArrayList<Author>();
		coautori = Graphs.neighborListOf(graph, a);
		
		return coautori;
	}
	
	public List<Author> trovaNonCoautori(Author a) {
		
		List<Author> nonCoautori = this.getAllAutori();
    	nonCoautori.removeAll(this.trovaCoautori(a));
    	nonCoautori.remove(a);
		
		return nonCoautori;
	}

	public List<Paper> sequenzaArticoli(Author autore1, Author autore2) {
		List<Paper> articoli = new ArrayList<>() ; 
	
		DijkstraShortestPath<Author, DefaultEdge> dijkstra = 
				new DijkstraShortestPath<Author, DefaultEdge>(this.graph, autore1, autore2);
		
		List<DefaultEdge> edges = dijkstra.getPathEdgeList() ;
		
		for(DefaultEdge e: edges) {
			Author a1 = graph.getEdgeSource(e) ;
			Author a2 = graph.getEdgeTarget(e) ;
			Paper p = null;
			
			for(Paper p1: a1.getArticoli()) 
				for(Paper p2: a2.getArticoli()) 
					if(p1.equals(p2)) {
						p = p1;
						break;
					}
			articoli.add(p) ;
		}
		
		return articoli;
	}
}
