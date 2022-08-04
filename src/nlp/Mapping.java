package nlp;
import java.util.*;
import com.google.gson.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.IOException;
import java.nio.file.*;
import TF_IDF.TfIdf;

public class Mapping {
	String src = "C:\\Users\\pc\\Desktop\\bdsas\\S3\\1 text mining";
	String path;
	PorterStemmer mystemmer;
	List<String> stopwordsList;
	String stopwords;
	//HashMap<String,ArrayList<String>> listDocs;
	public Mapping(String path) throws IOException {
		mystemmer=new PorterStemmer();
		stopwords = new Scanner(Paths.get("stopwords.txt"), "UTF-8").useDelimiter("\\A").next();
		stopwordsList = Arrays.asList(stopwords.split("\n"));
		this.path=path;
	}
	public List<Path> explore(Path p) throws Exception{
		
	    Stream<Path> paths = Files.list(p);
	    ArrayList<Path> docs = (ArrayList<Path>) paths.collect(Collectors.toList());
	    paths.close();
	    return docs;
	} 
	public HashMap<String,ArrayList<String>> L(Path p) throws Exception {
		String[] l1;
		List<Path> docs = explore(p);
		HashMap<String,ArrayList<String>> listDocs=new HashMap<String,ArrayList<String>>();
		for (int file=0;file<docs.size();file++) {
			ArrayList<String> buffer=new ArrayList<String>();
			Scanner d1=new Scanner(docs.get(file),"utf-8");
			while(d1.hasNext()) {
				String line=d1.nextLine();
				l1=line.split(" ");
				for(String token:l1) {
					if (!stopwordsList.contains(token)) {    // ignore stopwords
						String result = mystemmer.stemWord(token);    // stem  algorithm
					
						buffer.add(result);
					}
				}
			}
			d1.close();
			listDocs.put(docs.get(file).getFileName().toString(),buffer);
		}
		return listDocs;
	}		
	private int occurence(String term,ArrayList<String> doc) {
		int n=0;
		for(String s:doc) {
			if (s.equals(term)) n++;
		}
		return n;
	}
	public HashMap<String,HashMap<String,Integer>> toMap(Path p) throws Exception{
		HashMap<String,HashMap<String,Integer>> docs=new HashMap<String,HashMap<String,Integer>>();
		List<String> terms=new ArrayList<String>();
		HashMap<String, ArrayList<String>> listDocs=L(p);
		for(String d:listDocs.keySet()) {
			terms=listDocs.get(d).stream().collect(Collectors.toList());
			HashMap<String,Integer> doc=new HashMap<String,Integer>();
			for(String term:terms) {
				doc.put(term, TfIdf.occurence(term,listDocs.get(d)));
			}
			docs.put(d, doc);
		}
		return docs;
	}
	public HashMap<String,HashMap<String,HashMap<String,Integer>>> CorpustoMap() throws Exception{
		Stream<Path> path_categories=Files.list(Paths.get(path));
		ArrayList<Path> categories = (ArrayList<Path>) path_categories.collect(Collectors.toList());
		HashMap<String,HashMap<String,HashMap<String,Integer>>> corpus=new HashMap<String,HashMap<String,HashMap<String,Integer>>>();
		for (Path cat:categories) {
			corpus.put(cat.getFileName().toString(), toMap(cat));
		}
		return corpus;
	}
	public void saveMap(HashMap<String,HashMap<String,HashMap<String,Integer>>> corpus,String dir) {
        Gson gson=new Gson();
		String jsonFormat1 = gson.toJson(corpus);
        try {
			Files.write(Paths.get(dir), jsonFormat1.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}