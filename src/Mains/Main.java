package Mains;
import models.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import metrics.Metrics;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cross_validation.CV;
import nlp.Mapping;
public class Main {
	static float sum=0;

	public static void main(String[] args) throws Exception {
		String src = "C:\\Users\\pc\\Desktop\\bdsas\\S3\\1 text mining";	
		/*Mapping M=new Mapping(src+"/bbc");
		HashMap<String, HashMap<String, HashMap<String, Integer>>> corpus = M.CorpustoMap();
		System.out.println("mapped");
		M.saveMap(corpus, src+"/data/occurence");
		System.out.println("saved");*/
        Gson gson  = new Gson();
        HashMap<String, HashMap<String, HashMap<String, Integer>>> b=new HashMap<String, HashMap<String, HashMap<String, Integer>>>();
        String text1 = new String(Files.readAllBytes(Paths.get(src+"/data/occurence")));
        Map<String, HashMap<String, HashMap<String, Integer>>> f = new Gson().fromJson(text1, new TypeToken<HashMap<String, HashMap<String, HashMap<String, Integer>>>>() {}.getType());
        HashMap<String, HashMap<String, HashMap<String, Integer>>> m = (HashMap<String, HashMap<String, HashMap<String, Integer>>>) f;
        System.out.println("map rempli");
        CV c=new CV(m,5);
        HashMap<Integer, HashMap<String, HashMap<String, Integer>>> A = c.split();
        Bayes B=new Bayes(A,4);
        System.out.println("training,,,");
//        B.train();
        System.out.println("trained");
        //System.out.println(B.model.keySet());
        //System.out.println("gggggggg"+B.model.get("business").keySet());
        //System.out.println(B.model.get("sport").get(B.terms_distinct.get(342)));
        System.out.println("testing............");
  //      HashMap<String, String> preds = B.test();
    //    System.out.println(preds);
      // float wx = Metrics.macro_recall(preds);      
       //System.out.println("score="+wx);
        Kmeans clf=new Kmeans(3,A);
        //ArrayList<ArrayList<Float>> means = clf.intial_means();
        HashMap<Integer, HashMap<String, HashMap<String, Integer>>> res = clf.train();
        System.out.println(res.get(4).keySet());
        System.out.println(res.get(2).keySet());
        System.out.println(res.get(3).keySet());
        System.out.println(res.get(0).keySet());
        System.out.println(res.get(1).keySet());
	}

}
