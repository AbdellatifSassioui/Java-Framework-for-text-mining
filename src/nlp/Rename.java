package nlp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
public class Rename {
	public static void main(String[] args)
	{
		
		String data="C:\\Users\\pc\\Desktop\\bdsas\\S3\\1 text mining\\bbc";
		try {
			Scanner input=new Scanner(System.in);
			Stream<Path> paths = Files.list(Paths.get(data));
			ArrayList<Path> Folders = (ArrayList<Path>) paths.collect(Collectors.toList());
			for(int j=0;j<Folders.size();j++) {
				Stream<Path> docs_p = Files.walk(Folders.get(j));
				System.out.println(docs_p.toString());
				ArrayList<Path> docs = (ArrayList<Path>) docs_p.collect(Collectors.toList());
				for(int i=1;i<docs.size();i++) {
					System.out.println(docs.get(i).toString());
					File file = new File(docs.get(i).toString());
					File rename = new File(docs.get(i).toString().replaceAll(".txt", "")+"_"+Folders.get(j).getFileName().toString()+".txt");
					boolean flag = file.renameTo(rename);
					if (flag == true) {
						System.out.println("File Successfully Rename");
					}
					else {
						System.out.println("Operation Failed");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
