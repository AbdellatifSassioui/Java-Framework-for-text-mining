package models;

import java.util.*;
import metrics.*;
public class KNN {
	// sotocker les classes dans une tableau
	public String[] target= {"business","entertainment","politics","sport","tech"};
	HashMap<Integer,HashMap<String,HashMap<String,Integer>>> trainSet;
	HashMap<String, HashMap<String, Integer>> testSet;
	int k;
	int F_test;
	ArrayList<String> terms=new ArrayList<String>();
	public KNN(HashMap<Integer, HashMap<String, HashMap<String, Integer>>> trainSet,int F_test,int k) {
		this.k=k;
		this.trainSet=trainSet;
		this.F_test=F_test;
	}
	public void train() {
		testSet=trainSet.get(F_test);
		trainSet.remove(F_test);
	}
	public  HashMap<String,String> test(){
		HashMap<String, Integer> temp = new HashMap<String,Integer>();
		int s=0;
		HashMap<String,HashMap<String,Integer>> distances = new HashMap<String, HashMap<String, Integer>>();
		for(int folder:trainSet.keySet()) {
			for (String doc_test:testSet.keySet()) {			
				Set<String> T = testSet.get(doc_test).keySet();
				for(String doc_train:trainSet.get(folder).keySet()) {					
					for (String term:T) {
						if (testSet.get(doc_test).get(term)==null || trainSet.get(folder).get(doc_train).get(term)==null)
							continue;
						s+=testSet.get(doc_test).get(term)*trainSet.get(folder).get(doc_train).get(term);
						
					}
					temp.put(doc_train, s);//dist(testSet.get(doc_test),trainSet.get(folder).get(doc_train)));
					s=0;
				}
//				System.out.println(temp);
				distances.put(doc_test, temp);
				temp=new HashMap<String,Integer>();//.clear();
				//System.out.println(distances.keySet().size());
 			}
		}

		
		// stocker la somme des distancesde chaque classe
		int[] som= {0,0,0,0,0};
		HashMap<String, Integer> sm = new HashMap<String,Integer>();
		HashMap<String, String> map_f = new HashMap<String,String>();
		System.out.println("hgk"+target[0]);
		System.out.println(target[3]+"nlkk");
		System.out.println(distances.get("337_politics.txt"));
		for(String test_d:distances.keySet()) {
			for(int i=0;i<target.length; i++) {
				System.out.println("step"+i);
				//check the class
				for(String near:distances.get(test_d).keySet()) {
					System.out.println("bouvl");
					if (near.substring(near.lastIndexOf('_')+1,near.lastIndexOf('.')).equals(target[i])) {
						System.out.println("sggdh");
						sm.put(near, distances.get(test_d).get(near));
					}
				}
				System.out.println(sm);
				som[i]=this.getKmaxsum(sm);
			}	
		
			int max = som[0];
			int i_max=0;
			for(int i=0;i<som.length;i++) {
				if(som[i]>max) {
					max=som[i];
					i_max=i;
				}
			}
			map_f.put(test_d, target[i_max]);
		}
		return map_f;	
	}
	private int dist(HashMap<String,Integer>doc1,HashMap<String,Integer> doc2) {
		int d=0;
		if (doc1.keySet().size()<doc2.keySet().size()) {
			for(String term:doc1.keySet()) {
				if (doc2.get(term)!=null)
					d+=doc1.get(term)*doc2.get(term);
			}
			return d;
		}
		for(String term:doc2.keySet()) {
			if (doc1.get(term)!=null)
				d+=doc1.get(term)*doc2.get(term);
		}
		return d;
	}
	private int getKmaxsum(HashMap<String,Integer> HM) {
		int sum = 0;
		Collection<Integer> ar = HM.values();
		Object[] arr_obj = ar.toArray();
		Integer[] arr = new Integer[arr_obj.length];
        for (int i = 0; i < arr_obj.length; i++) {
            arr[i] = (Integer)arr_obj[i];
        }
        System.out.println(arr_obj.length);
        Arrays.sort(arr, Collections.reverseOrder());  
        for (int i = 0; i < k; i++)
            sum+=arr[i];
      
        return sum;
    }
}
