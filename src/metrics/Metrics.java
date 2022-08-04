package metrics;

import java.util.HashMap;

public class Metrics {
	public static String[] target= {"business","entertainment","politics","sport","tech"};
	public static float recall(HashMap<String,String> preds,String c) {
		int tp=0;
		int fn =0;
		for (String key:preds.keySet()) {
			if(key.substring(key.lastIndexOf("_")+1,key.lastIndexOf('.')).equals(c) && c.equals(preds.get(key))) {
				tp++;
				break;
			}
			if(key.substring(key.lastIndexOf("_")+1,key.lastIndexOf('.')).equals(c) && !c.equals(preds.get(key))) {
				fn++;
			}
		}
		float recall=tp/(tp+fn);
		return recall;	
 	}
	public static float precision(HashMap<String,String> preds,String c) {
		int tp=0;
		int fp=0;
		for (String key:preds.keySet()) {
			if(key.substring(key.lastIndexOf("_")+1,key.lastIndexOf('.')).equals(c) && c.equals(preds.get(key))) {
				tp++;
				break;
			}
			if(!key.substring(key.lastIndexOf("_")+1,key.lastIndexOf('.')).equals(c) && c.equals(preds.get(key))) {
				fp++;
			}
		}
//		System.out.println(tp+fp);
		float precision=tp/(float)(tp+fp);
		System.out.println(precision);
		return precision;	
	}
	public static float f_measure(HashMap<String,String> preds,String c) {
		float recall = recall(preds,c);
		float precision = precision(preds,c);
		float f1 = 2*(recall*precision)/(recall+precision);
		System.out.println(f1);
		return f1;
	}
	public static float macro_recall(HashMap<String,String> preds) {
		float macro=0;
		for(String c:target) {
			macro+=recall(preds,c)/5.0;
		}
		return macro;
	}
	public static float macro_precision(HashMap<String,String> preds) {
		float macro=0;
		for(String c:target) {
			macro+=precision(preds,c)/5.0;
		}
		return macro;
	}
	public static float macro_f_measure(HashMap<String,String> preds) {
		float macro=0;
		int N = target.length;
		for(String c:target) {
			macro+=f_measure(preds,c)/5.0;
		}
		return macro;
	}
}
