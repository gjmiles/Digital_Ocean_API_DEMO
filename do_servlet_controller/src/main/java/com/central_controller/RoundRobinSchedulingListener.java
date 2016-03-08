package com.central_controller;

import com.central_controller.dropletHandler;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.Iterator;

import com.myjeeva.digitalocean.*;
import com.myjeeva.digitalocean.common.*;
import com.myjeeva.digitalocean.exception.*;
import com.myjeeva.digitalocean.impl.*;
import com.myjeeva.digitalocean.pojo.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class RoundRobinSchedulingListener implements ServletContextListener {
    private Timer timer;
    private Timer longerTimer;

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {	
	timer.cancel();
	longerTimer.cancel();
	timer.purge();
	longerTimer.purge();
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

	TimerTask rrTimer = new RoundRobinTimerTask(servletContextEvent.getServletContext());

	TimerTask serveCheck = new newChecker(servletContextEvent.getServletContext());

	timer= new Timer();
	timer.schedule(rrTimer, 0,( 30 * 1000 ));

	longerTimer= new Timer();
	longerTimer.schedule(serveCheck, 0,( 120 * 1000 ));
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


    class newChecker extends TimerTask {
	private ServletContext context;

	public newChecker(ServletContext inContext){
	    this.context = inContext;
	}

	@Override
	public void run()
	{
	    String ip_addr = "";
	    ArrayList<String> droplet_ips = (ArrayList<String>)this.context.getAttribute("droplet_ips");
	    ArrayList<Droplet> drops = (ArrayList<Droplet>)this.context.getAttribute("newdrops");

	    if(drops != null && !drops.isEmpty())
		{
		    Iterator<Droplet> it = drops.iterator();
		    while(it.hasNext())
			{
			    try{
				dropletHandler dropHandler = (dropletHandler)this.context.getAttribute("drop_api");
				Droplet updatedDrop =dropHandler.getAPI().getDropletInfo(it.next().getId());
				DropletStatus status = updatedDrop.getStatus();
				System.out.println("Checking if new server is active: " + status.toString());

				if(status.equals(DropletStatus.ACTIVE))
				    {
					for(Network net : updatedDrop.getNetworks().getVersion4Networks())
					    {
						ip_addr = net.getIpAddress();

						if( droplet_ips != null )
						    {
							droplet_ips.add(ip_addr);
							this.context.setAttribute("droplet_ips", droplet_ips);
						    }
					    }
					it.remove();
				    }
			    }catch(DigitalOceanException e)
				{
				}catch(RequestUnsuccessfulException e)
				{
				}
			}
		}
	}
    }
}
