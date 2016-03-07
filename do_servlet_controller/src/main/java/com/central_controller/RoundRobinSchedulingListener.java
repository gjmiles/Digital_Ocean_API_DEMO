package com.central_controller;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class RoundRobinSchedulingListener implements ServletContextListener {
    private Timer timer;

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {	
	timer.cancel();
	timer.purge();
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

	TimerTask rrTimer = new RoundRobinTimerTask(servletContextEvent.getServletContext());

	timer= new Timer();
	timer.schedule(rrTimer, 0,( 30 * 1000 ));
    }

    class RoundRobinTimerTask extends TimerTask {
	private ServletContext context;

	public RoundRobinTimerTask(ServletContext inContext){
	    this.context = inContext;
	}

	@Override
	public void run() {
	    ArrayList<String> droplet_ips = (ArrayList<String>)this.context.getAttribute("droplet_ips");
	    Integer current_ip_index = (Integer)this.context.getAttribute("current_ip_index");

	    if(droplet_ips != null && current_ip_index != null)
		{
		    if( droplet_ips.size() > 0 )
			{
			    System.out.println(droplet_ips.size());
			    Integer next_ip_index = ((current_ip_index + 1) %  droplet_ips.size());
			    System.out.println("Round robining: " + String.valueOf(next_ip_index));
			    String current_ip = droplet_ips.get(next_ip_index);
			    System.out.println("Round robining: " + current_ip);
			    this.context.setAttribute("current_ip_index", next_ip_index);
			    this.context.setAttribute("current_ip", current_ip);
			}
		}
	}
    }

}
