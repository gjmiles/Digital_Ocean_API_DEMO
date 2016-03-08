package com.central_controller;

import java.util.ArrayList;

import com.myjeeva.digitalocean.*;
import com.myjeeva.digitalocean.common.*;
import com.myjeeva.digitalocean.exception.*;
import com.myjeeva.digitalocean.impl.*;
import com.myjeeva.digitalocean.pojo.*;

public class dropletHandler {
    private DigitalOcean apiClient;
    private ArrayList<Droplet> dynamic_drops;

    public dropletHandler(String authToken){
	apiClient = new DigitalOceanClient(authToken);
	dynamic_drops = new ArrayList<Droplet>();
    }

    //Return Id
    public void CreateFileServer()
    {
	Droplet newFileServer = new Droplet();
	newFileServer.setName("fileServer");
	newFileServer.setSize("4gb");
	newFileServer.setRegion(new Region("sfo1"));
	newFileServer.setImage(new Image(16161680)); 
	newFileServer.setEnableBackup(Boolean.FALSE);
	newFileServer.setEnableIpv6(Boolean.FALSE);
	newFileServer.setEnablePrivateNetworking(null);
	ArrayList<Key> keys = new ArrayList<Key>();
	keys.add(new Key("01:6b:70:a0:81:52:1b:fe:da:6c:d7:b2:a3:6b:5d:da"));
	newFileServer.setKeys(keys);
	newFileServer.setUserData(null);
	try{
	    Droplet droplet = apiClient.createDroplet(newFileServer);
	    dynamic_drops.add(droplet);
	}catch(DigitalOceanException e)
	    {
	    }catch(RequestUnsuccessfulException e)
	    {
	    }
    }

    public ArrayList<Droplet> getNewDroplets()
    {
	return dynamic_drops;
    }

    public DigitalOcean getAPI()
    {
	return 	apiClient;
    }

    public String destroyFileServer()
    {
	String ip_addr = "";

	try{
	    if(!dynamic_drops.isEmpty())
		{
		    Droplet delDrop = dynamic_drops.get(0);
		    Integer id = delDrop.getId();

		    for(Network net : delDrop.getNetworks().getVersion4Networks())
			{
			    ip_addr = net.getIpAddress();
			    apiClient.deleteDroplet(id);
			    dynamic_drops.remove(0);
			}
		}	
	}catch(DigitalOceanException e)
	    {
	    }
	catch(RequestUnsuccessfulException e)
	    {
	    }
	
	return ip_addr;
    }

    public ArrayList<String> GetDropletIPs()
    {	
	Integer pageNo = 1;
	Integer perPage = 1;

	ArrayList<String> ip_addrs = new ArrayList<String>();	

	try{
	    Droplets droplets = apiClient.getAvailableDroplets(pageNo, null);

	    for(Droplet drop : droplets.getDroplets())
		{
		    for(Network net : drop.getNetworks().getVersion4Networks())
			{
			    ip_addrs.add(net.getIpAddress());
			    //System.out.println(net.getIpAddress());
			}
		}

	}
	catch(DigitalOceanException e)
	    {
	    }
	catch(RequestUnsuccessfulException e)
	    {
	    }

	return ip_addrs;
    }

}
