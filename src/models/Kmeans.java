package models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Kmeans {
	int K;
	ArrayList<ArrayList<Float>> means=new ArrayList<ArrayList<Float>>(K);
	HashMap<String, HashMap<String, Integer>> corpus=new HashMap<String,HashMap<String,Integer>>();
	List<String> terms_distinct=new ArrayList<String>();
	public Kmeans(int k,HashMap<Integer, HashMap<String, HashMap<String, Integer>>> data) {
		this.K=k;
		for(Integer i:data.keySet()) {
			for(String doc:data.get(i).keySet()){
				corpus.put(doc,data.get(i).get(doc));
			}
		}
		ArrayList<String> terms = new ArrayList<String>();
		for(Integer folder:data.keySet()) {
			for(String doc:data.get(folder).keySet()) {
				for(String term:data.get(folder).get(doc).keySet()) {
					terms.add(term);
				}	
			}
		}
		this.terms_distinct = terms.stream().distinct().collect(Collectors.toList());
	}
	public HashMap<Integer, HashMap<String, HashMap<String, Integer>>> train(){
		HashMap<Integer, HashMap<String, HashMap<String, Integer>>> map_f = new HashMap<Integer, HashMap<String, HashMap<String, Integer>>>();
		HashMap<Integer, HashMap<String, HashMap<String, Integer>>> map_f2 = new HashMap<Integer, HashMap<String, HashMap<String, Integer>>>();
		HashMap<Integer,ArrayList<Float>> old_means = new HashMap<Integer,ArrayList<Float>>();
		HashMap<Integer,ArrayList<Float>> new_means = new HashMap<Integer,ArrayList<Float>>();
		HashMap<String, HashMap<String, Integer>> temp = new HashMap<String,HashMap<String,Integer>>();
		int t = 0;
		Random rndm=new Random();
		for(int i=0;i<K;i++) {
			map_f.put(i, new HashMap<String,HashMap<String,Integer>>());
		}
		for(String doc:corpus.keySet()) {			
			int r=(int)(rndm.nextDouble()*K);
			map_f.get(r).put(doc, corpus.get(doc));
		}
		for(int j=0;j<K;j++) {
			old_means.put(j,this.calcul_means(map_f, j));
		}
		new_means=new HashMap<Integer,ArrayList<Float>>(old_means);	
		do {
			map_f2.clear();
			map_f2=new HashMap<>(map_f);
			System.out.println("m2"+map_f2.keySet());
			for(int i=0;i<K;i++) {
				map_f.put(i, new HashMap<String,HashMap<String,Integer>>());
			}
			System.out.println("iteration"+t);
			List<Double> old_means.c=new ArrayList<Double>(new_means);
			t++;
			//affectation
			int i_min=0,d,d_min=0;
			for(String doc:corpus.keySet()) {
				for(int j=0;j<K;j++) {
					d=this.dist(corpus.get(doc), old_means.get(j));
					j++;
					if(d>d_min) {
						d_min=d;
						i_min=j;
					}
				}
				temp.put(doc, corpus.get(doc));
		//		System.out.println("first"+temp.get(doc).size());
				map_f.put(i_min, temp);
				temp=new HashMap<String, HashMap<String, Integer>>();
			}
			//new_means=new ArrayList<ArrayList<Float>>();
			//means update
			for(int j=0;j<K;j++) {
				new_means.put(j,this.calcul_means(map_f, j));
			}			
		}
		while(!map_f.equals(map_f2));
		//while(!old_means.equals(new_means));
		return map_f;
	}
	private ArrayList<Float> calcul_means(HashMap<Integer, HashMap<String, HashMap<String, Integer>>> map_f,int c){
		ArrayList<Float> w=new ArrayList<Float>();
		int size = this.terms_distinct.size();
		for(String term:this.terms_distinct) {
			float s=0;
	//		System.out.println("ccc"+c);
		//	System.out.println(map_f.keySet());
			for(String doc:map_f.get(c).keySet()) {		
				if(map_f.get(c).get(doc).get(term)!=null) {
					s+=map_f.get(c).get(doc).get(term);
				}
			}
			w.add(s/(float)size);
		}
		return w;
	}
/*	private ArrayList<Float> calcul_means2(HashMap<Integer,ArrayList<String>> map_f,int c){
		ArrayList<Float> w=new ArrayList<Float>();
		for(String term:this.terms_distinct) {
			int s=0;
			for(String doc:map_f.get(c).keySet()) {		
				if(map_f.get(c).get(doc).get(term)!=null) {
					s+=map_f.get(c).get(doc).get(term);
				}
			}
			w.add(s/(float)map_f.get(c).keySet().size());
		}
		return w;
	}*/
	private ArrayList<ArrayList<Float>> intial_means() {
		Set<String> D = corpus.keySet();
		String[] docs = new String[D.size()];
		D.toArray(docs);
		ArrayList<Float> w=new ArrayList<Float>();
		Random rndm=new Random();
		int m = corpus.size();
		for(int i=0;i<this.K;i++) {
			int r=(int) (rndm.nextFloat()*m/2);
			w=new ArrayList<Float>();
			for(int term=0;term<this.terms_distinct.size();term++) {
				if(corpus.get(docs[r]).get(this.terms_distinct.get(term))==null) {
					w.add(0f);
				}
				else {
					
					w.add((float)corpus.get(docs[r]).get(terms_distinct.get(term)));
				}
			}
			means.add(w);
		}
		return means;
	}
	private int dist(HashMap<String,Integer>doc1,ArrayList<Float> w) {
		int d=0;
		for(int i=0;i<this.terms_distinct.size();i++) {
			if(doc1.get(this.terms_distinct.get(i))!=null) {
				int w_term;
				w_term = doc1.get(this.terms_distinct.get(i));
				d+=w_term*w.get(i);
			}
		}
		return d;
	}
	private void affectation() {
		int i_min=0,d,d_min=0;
		for(String doc:corpus.keySet()) {
			for(int j=0;j<K;j++) {
				d=this.dist(corpus.get(doc), means.get(j));
				j++;
				if(d>d_min) {
					d_min=d;
					i_min=j;
//					System.out.println(i_min);
				}
			}
			temp.put(doc, corpus.get(doc));
	//		System.out.println("first"+temp.get(doc).size());
			map_f.put(i_min, temp);
			temp=new HashMap<String, HashMap<String, Integer>>();
		}
	}
}
	
