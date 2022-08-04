package TF_IDF;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.nio.file.*;

public class Mapping {
	String path;
	public Mapping(String path) {
		this.path=path;
	}
	public List<Path> explore(String path) throws Exception{
		
	    Stream<Path> paths = Files.walk(Paths.get(path));
	    ArrayList<Path> docs = (ArrayList<Path>) paths.collect(Collectors.toList());
	    for(int p=0;p<docs.size();p++) {
	    	if(!docs.get(p).getFileName().toString().endsWith(".txt")) docs.remove(p);
	    }
	    paths.close();
	    return docs;
	} 
	public HashMap<String,ArrayList<String>> L() throws Exception {
		String[] l1;
		List<Path> docs = explore(path);
		HashMap<String,ArrayList<String>> listDocs=new HashMap<String,ArrayList<String>>();
		for (int file=0;file<docs.size();file++) {
			ArrayList<String> buffer=new ArrayList<String>();
			Scanner d1=new Scanner(docs.get(file),"utf-8");
			while(d1.hasNext()) {
				String line=d1.nextLine();
				l1=line.split(" ");
				for(int i=0;i<l1.length;i++) {
					buffer.add(l1[i]);
				}
			}
			d1.close();
			listDocs.put(docs.get(file).getFileName().toString(),buffer);
		}
		return listDocs;
	}
}