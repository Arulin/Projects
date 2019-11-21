package data;

import java.io.*;
import java.util.ArrayList;

public class Data {
	private FileReader reader;
	private String wayToFile;
	private long fileLength;
	private final File file = new File("");
	
	public Data(String wayToFile){
		this.wayToFile = wayToFile;
		fileLength = new File(wayToFile).length();
	}
	//читам файл
	public ArrayList<Person> startRead(int sumOfByte) {
		char[] readArray =  new char[sumOfByte];
		ArrayList<Person> workers = new ArrayList<>();
		if (wayToFile != null) {
			try {
				reader = new FileReader(file.getAbsolutePath() + wayToFile);
				while(reader.ready()){
					short count = 0;
					while (count != sumOfByte) {
						readArray[count] = (char) reader.read();
						count++;
					}
					if(count == 128) workers.add(new Person(readArray));
					count = 0;
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return workers;
	}


	//записываем в файл
	public void startWrite(Person person, boolean append){

		try {
			FileWriter writer = new FileWriter(new File(wayToFile), append);
			writer.write(person.toString());
			System.out.println(person.toString());
			writer.flush();
			writer.close();

		}catch (IOException e){
			System.out.println("Error write");
			e.printStackTrace();
		}
	}

	public void startWrite(ArrayList<Person> employees){
		StringBuffer buffer = new StringBuffer();
		for(Person person : employees){
			buffer.append(person.toString());
		}

		try{
			FileWriter writer = new FileWriter(file.getAbsolutePath() + wayToFile, false);
			writer.write((String.valueOf(buffer)));
			writer.flush();
			writer.close();
		}catch (IOException e){
			System.out.println("Error write");
			e.printStackTrace();
		}

	}




}