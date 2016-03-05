package com.central_controller;

import java.util.TimerTask;
import javax.servlet.ServletContext;

class countDown extends TimerTask {
    private ServletContext context;
    
    public countDown(ServletContext inContext){
	this.context = inContext;
    }

    @Override
    public void run()
    {
	Integer counter = (Integer)context.getAttribute("load_counter");

	if(counter != 0)
	    {
		counter--;
		context.setAttribute("load_counter",counter);
	    }
    }
}
