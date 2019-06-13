package gearon.dao.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import gearon.dao.TimeUnitDAO;

public class TimeUnitDAOImpl extends TimeUnitDAO{
	
	FileWriter fileWriter = null;
	BufferedReader reader = null;
	File file = new File("config.txt");
	
	@Override
	public void setMinute(int minute) {
		// TODO Auto-generated method stub
		try {
			fileWriter = new FileWriter(file);
			fileWriter.write(String.valueOf(minute));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				if(fileWriter != null){
					fileWriter.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getMinute() {
		// TODO Auto-generated method stub
		int minute = 0;
		try {
			FileReader fileReader = new FileReader(file);
			reader = new BufferedReader(fileReader);
			String str = reader.readLine();
			minute = Integer.parseInt(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			minute = 0;
			e.printStackTrace();
		} finally{
			try{
				if(reader != null){
					reader.close();
				}
			}catch(Exception e){
				minute = 0;
				e.printStackTrace();
			}
		}
		return minute;
	}

}
