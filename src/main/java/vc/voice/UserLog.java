package vc.voice;

public class UserLog {
	private String id[];
	private String date[];
	private int length;
	
	public UserLog(String id) {
		this.id = new String[SQLHandler.countusertext(id)];
		this.date = new String[SQLHandler.countusertext(id)];
		this.length = SQLHandler.countusertext(id);
	}
	public UserLog(String[] Iid, String[] Idate) {
		id= Iid;
		date = Idate;
	}
	
	public void setid(String Iid, int i)
	{
		id[i] = Iid;
	}
	
	public void setdate(String Idate, int i)
	{
		date[i] = Idate;
	}
	
	public String[] getid()
	{
		return id;
	}
	public String[] getdate()
	{
		return date;
	}
	
	public int getlength()
	{
		return length;
	}
}
