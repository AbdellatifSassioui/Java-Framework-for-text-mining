package TF_IDF;
import stemmer.StemedFile;
import java.util.*;
import cosinus.Similarity;
import java.io.*;

public class Main{
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Scanner input=new Scanner(System.in);
	    System.out.println("Enter votre requete : ");
	    String query = input.nextLine();
	    input.close();
	    Similarity S=new Similarity(query);
	    String dist=S.cos_similarity();
	    System.out.println("le document que vous voulez c'est: "+dist);
	
	}
}