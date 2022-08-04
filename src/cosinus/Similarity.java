package cosinus;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import TF_IDF.Mapping;
import TF_IDF.TfIdf;
import stemmer.ArabicStemmerKhoja;

public class Similarity {
	String query;
	HashMap<String,Float> query_map;
	HashMap<String, HashMap<String, Float>> docs;
	public Similarity(String query) throws Exception {
		ArrayList<String> new_query=new ArrayList<String>();
		this.query=query;
		String stopwords = new Scanner(Paths.get("stopwords.txt"), "UTF-8").useDelimiter("\\A").next();
		List<String> stopwordsList = Arrays.asList(stopwords.split("\n"));
		String[] tokens=query.split(" ");
		ArabicStemmerKhoja mystemmer=new ArabicStemmerKhoja();
		for (String token : tokens) {
			if (!stopwordsList.contains(token)) {    // ignore stopwords
				String result = mystemmer.stem(token);    // Khoja rooting algorithm
				new_query.add(result);
			}
		}
		String data_path="data_out";
		Mapping A=new Mapping(data_path);
		HashMap<String, ArrayList<String>> corpus=A.L();	    
		ArrayList<String> terms=new ArrayList<String>();
		TfIdf W=new TfIdf();
		docs = W.tfidf();
		query_map = W.tf(new_query);
	}
	public float cos(HashMap<String,Float> doc1,HashMap<String,Float> doc2) {
		float numenator=0;
		float denomenator=0;
		float doc1_norm=0;
		float doc2_norm=0;
		for(String t:doc1.keySet()) {
			numenator+=doc1.get(t)*doc2.get(t);
			doc1_norm+=doc1.get(t)*doc1.get(t);
			doc2_norm+=doc2.get(t)*doc2.get(t);
		}
		denomenator=(float) (Math.sqrt(doc1_norm)*Math.sqrt(doc2_norm));
		return numenator/denomenator;
	}
	public String cos_similarity() throws IOException {
		HashMap<String,Float> l = new HashMap<String,Float>();
		int i=0;
		int c=0;
		float cosi=0;
		String mydoc = "";
		float my_cos=0;
		for(String doc:docs.keySet()) {
			cosi= cos(query_map,docs.get(doc));
			if(cosi>my_cos) {
				my_cos=cosi;
				mydoc=doc;
						
			}
			l.put((String) docs.keySet().toArray()[i],cos(query_map,docs.get(doc)));
			i++;
		}
		return mydoc;
	}
}