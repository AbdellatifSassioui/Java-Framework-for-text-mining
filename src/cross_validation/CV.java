package cross_validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class CV {
	int cv;
	HashMap<String,HashMap<String,HashMap<String,Integer>>> corpus;
	public CV(HashMap<String,HashMap<String,HashMap<String,Integer>>>corpus,int cv){
		this.corpus=corpus;
		this.cv=cv;
	}
	public HashMap<Integer,HashMap<String,HashMap<String,Integer>>> split() {
		HashMap<Integer,HashMap<String,HashMap<String,Integer>>> Cv=new HashMap<Integer,HashMap<String,HashMap<String,Integer>>>(); 
		Set<String> clas = corpus.keySet();
		String classes[] =new String[clas.size()];
		//clas contient les noms des documents
		clas.toArray(classes);
		ArrayList<Integer> n_c=new ArrayList<Integer>();
		ArrayList<HashMap<String,HashMap<String,Integer>>> List_CV=new ArrayList<HashMap<String,HashMap<String,Integer>>>();
//initialiser LIST_CV
		for (int i=0;i<cv;++i) {
			List_CV.add(i,new HashMap<String,HashMap<String,Integer>>());
		}
		ArrayList<HashMap<String, HashMap<String, Integer>>> F1=new ArrayList<HashMap<String, HashMap<String, Integer>>>();
		ArrayList<HashMap<String, HashMap<String, Integer>>> F2=new ArrayList<HashMap<String, HashMap<String, Integer>>>();
		ArrayList<HashMap<String, HashMap<String, Integer>>> F3=new ArrayList<HashMap<String, HashMap<String, Integer>>>();
		ArrayList<HashMap<String, HashMap<String, Integer>>> F4=new ArrayList<HashMap<String, HashMap<String, Integer>>>();
		//recuper le nombre de docs dans chaque c 
		for (String s:clas) {
			n_c.add(corpus.get(s).keySet().size());
		}
		for(int i=0;i<cv;i++) {
			Cv.put(i, null);
		}
		for(int s=0;s<classes.length;s++) {
			Set<String> set_docs=corpus.get(classes[s]).keySet();
			String docs[] =new String[set_docs.size()];
			docs=set_docs.toArray(docs);
		//	System.out.println(docs[1]);
			for(int j=0;j<n_c.get(s)-cv;j+=cv) {
				for(int c=j;c<j+cv;c++) {
					if (c>n_c.get(s)) break;
					HashMap<String, HashMap<String, Integer>> temp = new HashMap<String,HashMap<String,Integer>>();
					//System.out.println("==>"+corpus.get(classes[s]).get(docs[c]).size());
					temp.put(docs[c], corpus.get(classes[s]).get(docs[c]));
					//System.out.println(temp.keySet());
					List_CV.get(c-j).put(docs[c], corpus.get(classes[s]).get(docs[c]));
				}
			}
		}
		for(int i=0;i<cv;i++) {
	//		System.out.println(List_CV.get(i).keySet());
			Cv.put(i, List_CV.get(i));
		}
		return Cv;
	}
}
