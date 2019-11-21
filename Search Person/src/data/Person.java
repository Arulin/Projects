package data;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class Person {
	private String sername;
	private String name;
	private String lastname;
	private GregorianCalendar birthday;
	private String phoneNumber;

	public Person(char[] array) {
		workWithData(array);
	}

	public Person(String sername, String name, String lastname, String bd, String phoneNumber){
		this.sername = sername;
		this.name = name;
		this.lastname = lastname;
		this.birthday = dateBirthday(bd);
		this.phoneNumber = phoneNumber;
	}


	private  void workWithData(char[] array) {
		if (array.length != 128) {
			return;
		}

		sername = new String(new StringBuffer().append(array, 0, 29));
		sername = sername.replaceAll(" ", "");

		name = new String(new StringBuffer().append(array, 30, 19));
		name = name.replaceAll(" ", "");

		lastname = new String(new StringBuffer().append(array, 50, 19));
		lastname = lastname.replaceAll(" ", "");

		birthday = dateBirthday(new String(new StringBuffer().append(array, 70, 8)));

		phoneNumber = new String(new StringBuffer().append(array, 78, 49));
		phoneNumber = phoneNumber.replaceAll(" ", "");
	}

	private GregorianCalendar dateBirthday(String bd) {
		int day = Integer.parseInt(bd.substring(0,2));
		int month = Integer.parseInt(bd. substring(2,4)) - 1;
		int year = Integer.parseInt(bd. substring(4));
		
		GregorianCalendar date = new GregorianCalendar(year, month, day);
		
		return date;
	}

	public String getSername() {
		return sername;
	}

	public String getName() {
		return name;
	}

	public String getLastname() {
		return lastname;
	}

	public String getBirthday(){
		SimpleDateFormat bdF = new SimpleDateFormat("dd.MM.YYYY");
		String date = bdF.format(birthday.getTime());
		return date;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(convertForFile(sername, 30));
		buffer.append(convertForFile(name, 20));
		buffer.append(convertForFile(lastname, 20));
		buffer.append(convertForFile(new SimpleDateFormat("ddMMYYYY").format(birthday.getTime()), 8));
		buffer.append(convertForFile(phoneNumber, 50));

		return new String(buffer);
	}

	private String convertForFile(String field, int size){
		StringBuilder builder = new StringBuilder();
		builder = builder.append(field);
		for(int i = field.length(); i < size; i++){
			builder.append(" ");
		}
		return  new String(builder);
	}
}