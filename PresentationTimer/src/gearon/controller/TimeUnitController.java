package gearon.controller;

import gearon.dao.TimeUnitDAO;
import gearon.dao.impl.TimeUnitDAOImpl;

public class TimeUnitController {
	TimeUnitDAO timeUnitDAO = new TimeUnitDAOImpl();
	
	public void setMinute(int minute){
		timeUnitDAO.setMinute(minute);
	}
	
	public int getMinute(){
		return timeUnitDAO.getMinute();
	}
}
