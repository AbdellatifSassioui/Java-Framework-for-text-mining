package TF_IDF;
import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import java.lang.Math;
public class TfIdf {
	List<String> terms=new ArrayList<String>();
	int D;
	HashMap<String, ArrayList<String>> docs=null;
	public TfIdf() throws Exception {
		Mapping A=new Mapping("data_out");
		docs = A.L();
		for (ArrayList<String> i:docs.values()) {
			List<String> l=i.stream().distinct().collect(Collectors.toList());
			terms.addAll(l);
			terms=terms.stream().distinct().collect(Collectors.toList());
		}
		this.D=docs.size();
	}
	public static int occurence(String term,ArrayList<String> doc) {
		int n=0;
		for(String s:doc) {
			if (s.equals(term)) n++;
		}
		return n;
	}
	public HashMap<String,Float> tf(ArrayList<String> doc){
		HashMap<String,Float> doc_map=new HashMap<String,Float>();
		//Map<String,Float> map=new HashMap<String,Float>();
		float size=doc.size();
		for (String term:terms) {
			//doc_map.put(term, (float)Collections.frequency(doc, term)/size);
			doc_map.put(term, (float)occurence(term,doc));
		}
		return doc_map;
	}
	public HashMap<String,Float> idf(ArrayList<String> doc){
		HashMap<String,Float> doc_map=new HashMap<String,Float>();
		int d=0;
		boolean b=false; 
		for(String term:terms) {
			d=0;
			//b=false;
			for(ArrayList<String> doc_e:docs.values()) {
				if(doc_e.contains(term)) {
					d++;
					b=false;
					//System.out.println(doc_e);
				}
			}				
			doc_map.put(term, (float)Math.log10((float)D/(float)d));
		}
		return doc_map;
	}
	public HashMap<String,HashMap<String,Float>> tfidf(){
		HashMap<String,HashMap<String,Float>> Docs=new HashMap<String,HashMap<String,Float>>();
		int c=0;
		for(ArrayList<String> doc:docs.values()) {			
			HashMap<String, Float> map_tf = tf(doc);
			HashMap<String, Float> map_idf = idf(doc);
			HashMap<String, Float> doc_map = new HashMap<String, Float>();
			for(String term:map_tf.keySet()) {
				doc_map.put(term, map_tf.get(term)*map_idf.get(term));
			}
			Docs.put((String) docs.keySet().toArray()[c], doc_map);
			c++;
		}
		return Docs;
	}
}
