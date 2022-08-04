package models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Bayes {
	
	public HashMap<String,HashMap<String,Double>> model=new HashMap<String,HashMap<String,Double>>();
	HashMap<String,Double> proba_clas=new HashMap<String, Double>();
	String[] target= {"business","entertainment","politics","sport","tech"};
	public ArrayList<String> terms=new ArrayList<String>();
	HashMap<Integer,HashMap<String,HashMap<String,Integer>>> trainSet;
	HashMap<Integer,HashMap<String,HashMap<String,Integer>>> corpus;
	public List<String> terms_distinct;
	int F_test;
	HashMap<String, HashMap<String, Integer>> testSet;
	public Bayes(HashMap<Integer,HashMap<String,HashMap<String,Integer>>> corpus,int F_test) {
		this.trainSet=corpus;
		this.F_test=F_test;
	}
	public HashMap<String, HashMap<String, Double>> train() {
		testSet=trainSet.get(F_test);
		trainSet.remove(F_test);
		for(Integer folder:trainSet.keySet()) {
			for(String doc:trainSet.get(folder).keySet()) {
				for(String term:trainSet.get(folder).get(doc).keySet()) {
					terms.add(term);
				}	
			}
		}
		System.out.println("terms===="+terms.size());
		this.terms_distinct = terms.stream().distinct().collect(Collectors.toList());
		System.out.println("distinct==="+terms_distinct.size());
		HashMap<String,Double> value_model=new HashMap<String, Double>();
//		model=new HashMap<String,HashMap<String,Double>>();
		double[] probas=new double[target.length];
		for(int i=0;i<probas.length;i++) {
			probas[i]=this.p_classe(target[i]);
			//System.out.println("proba tech"+target[4]);
		}
		System.out.println("proba tech"+probas[4]);
		for(int i=0;i<probas.length;i++) {
			System.out.println("step"+i);
			model.put(target[i], new HashMap<String,Double>());
			for(String term:this.terms_distinct) {
				double p = this.p_term(term, target[i]);
				value_model.put(term, p);
				model.get(target[i]).put(term, p);
			}
			System.out.println("i*i"+value_model.keySet().toArray()[i*i*i]);
			System.out.println(value_model.values().toArray()[i*i*i]);
			proba_clas.put(target[i], this.p_classe(target[i]));
			//model.put(target[i], value_model);
			System.out.println(model.get(target[0]).get(terms_distinct.get(342)));
			value_model.clear();
			System.out.println("lspp");
		}
        System.out.println("gggggggg"+model.get("business").keySet().size());
//        System.out.println(model.get("sport").get(terms_distinct.get(342)));
		return model;
	}
	private double p_classe(String classe) {
		int n_c=0,n=0;
		for(Integer folder:trainSet.keySet()) {
			for(String doc:trainSet.get(folder).keySet()) {
				if(doc.substring(doc.lastIndexOf('_')+1,doc.lastIndexOf('.')).equals(classe))
					n_c++;
				n++;
			}
		}
		return n_c/(float)n;	
	}
	private double p_term(String term,String classe) {
		int m = terms_distinct.size();
		int n=0,nk=0;
		for(Integer folder:trainSet.keySet()) {
			for(String doc:trainSet.get(folder).keySet()) {
				if(!doc.substring(doc.lastIndexOf('_')+1,doc.lastIndexOf('.')).equals(classe))
					continue;
				Collection<Integer> aa = trainSet.get(folder).get(doc).values();
				int sum = aa.stream().mapToInt(i->i).sum();
				if (trainSet.get(folder).get(doc).containsKey(term)) {
					nk+=trainSet.get(folder).get(doc).get(term);
				}
				n+=sum;
			}
		}
		double p = (float)(nk+1)/(float)(n+m);
		return p;
	}
	public HashMap<String,String> test() {
		HashMap<String, String> map_f = new HashMap<String,String>();
		double[] probas=new double[target.length];
		for(String doc:testSet.keySet()) {
			for(int i=0;i<target.length;i++) {
				//System.out.println("===========>"+target[2]);
				double c = proba_clas.get(target[i]);
				double p_t = 1;
				//out.println(testSet.get(doc).keySet());
				for(String term:testSet.get(doc).keySet()) {
				//	System.out.println(term);

					if(model.get(target[i]).get(term)!=null) {
						p_t = this.model.get(target[i]).get(term)*10000;
						System.out.println("term p="+p_t);
						c*=p_t;
					}
				}
				System.out.println("jk"+p_t);
				System.out.println("c="+c);
				probas[i]=c;
			}
			map_f.put(doc, target[argmax(probas)]);
		}
		return map_f;
	}
	private int argmax(double[] arr) {
		double max = arr[0];
		int argmax=0;
		for(int i=0;i<arr.length;i++) {
			if(arr[i]>max) {
				max=arr[i];
				argmax=i;
			}
		}
		return argmax;
	}
}
