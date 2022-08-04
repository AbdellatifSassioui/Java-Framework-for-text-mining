package stemmer;

/*

# khoja Algorithm (a command-line version)

A command line version of Koja algorithm (An Arabic rooting algorithm). The algorithm reduces Arabic words to their roots.

Version 1.0

Author: Motaz Saad (motaz dot saad at gmail dot com)


This software is a modification of Khoja algorithm for Arabic rooting. The original implementation is available at http://zeus.cs.pacificu.edu/shereen/ArabicStemmerCode.zip

The algorithm is described in:

- Khoja S., Garside R., "Stemming Arabic text", Computer Science Department, Lancaster University, Lancaster, UK, 1999.

- http://zeus.cs.pacificu.edu/shereen/research.htm#stemming

*/




import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StemedFile {
	public void m(String in,String out) throws UnsupportedEncodingException, IOException, URISyntaxException{
		String stopwords = new Scanner(Paths.get("stopwords.txt"), "UTF-8").useDelimiter("\\A").next();
		List<String> stopwordsList = Arrays.asList(stopwords.split("\n"));
		
		ArabicStemmerKhoja mystemmer = new ArabicStemmerKhoja(); 

		String fin;
		String fout;	
		
		fin=in;
		fout=out;
	    Stream<Path> paths = Files.walk(Paths.get(in));
	    ArrayList<Path> docs = (ArrayList<Path>) paths.collect(Collectors.toList());
	    for(int i=0;i<docs.size();i++) {
	    	if(!docs.get(i).toString().endsWith(".txt")) docs.remove(i);
	    }
	    
		Scanner input;
		Formatter output;
		for(int j=1;j<docs.size();j++) {
			File f=new File(out+"\\out_"+docs.get(j).getFileName().toString());
			f.createNewFile();		
			f.setWritable(true);
		}
	    Stream<Path> paths_out = Files.walk(Paths.get(out));
	    ArrayList<Path> docs_out = (ArrayList<Path>) paths_out.collect(Collectors.toList());
		System.out.println("test1");
		try{
			for(int j=1;j<docs.size();j++) {
				System.out.println(docs.size());
		//		File f=new File(out+"\\out_"+docs.get(j).getFileName().toString());
			//	f.createNewFile();			
				input = new Scanner(docs.get(j).toRealPath(),"utf-8");
				System.out.println(docs.get(j).getFileName());
				output = new Formatter(docs_out.get(j).toRealPath().toString(), "utf-8");
				while (input.hasNextLine()) {
					System.out.print("j");
					String line = input.nextLine();
					String lineout = new String();
					String[] tokens = line.split("\\s");
					for (String token : tokens) {
						if (!stopwordsList.contains(token)) {    // ignore stopwords
							String result = mystemmer.stem(token);    // Khoja rooting algorithm
							lineout += result + " ";
						}
					}
					output.format("%s\n", lineout);
				}
				input.close();
				output.close();
				//System.exit(0);
			}
		}
		catch (IOException ioException){
			System.err.println(ioException.getMessage());
			System.exit(-1);
		} catch(NoSuchElementException excp) {
			System.err.println(excp.getMessage());
		} catch (FormatterClosedException excp){
			System.err.println(excp.getMessage());
		} catch (IllegalStateException excp){
			System.err.println(excp.getMessage());
		}

	}
} // end class


