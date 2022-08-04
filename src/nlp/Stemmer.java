package nlp;
import java.io.*;
public class Stemmer
{
	public static void main(String[] args) {
			PorterStemmer s=new PorterStemmer();
			String r = s.stemWord("techable");
			System.out.println(r);
	}

}