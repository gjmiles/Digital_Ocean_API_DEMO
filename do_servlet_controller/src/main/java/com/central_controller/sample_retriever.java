package com.central_controller;

import com.central_controller.image_meta;
import com.central_controller.countDown;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Driver;
import com.mysql.jdbc.Statement;

import com.myjeeva.digitalocean.*;
import com.myjeeva.digitalocean.common.*;
import com.myjeeva.digitalocean.exception.*;
import com.myjeeva.digitalocean.impl.*;
import com.myjeeva.digitalocean.pojo.*;
//import com.myjeeva.digitalocean.exception.DigitalOceanException;
//import com.myjeeva.digitalocean.exception.RequestUnsuccessfulException;
//import com.myjeeva.digitalocean.impl.DigitalOceanClient;
//import com.myjeeva.digitalocean.pojo.Networks;
//import com.myjeeva.digitalocean.pojo.Network;
//import com.myjeeva.digitalocean.pojo.Droplet;
//import com.myjeeva.digitalocean.pojo.Droplets;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebServlet("/GetImageServlet")
public class sample_retriever extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	
	public void init() throws ServletException {
	    String authToken = "6dac67db52ec605114cea4de81fd3cb6629556f3e57a8021ade1c6db80765bb6";

	    DigitalOcean apiClient = new DigitalOceanClient(authToken);

	    if ( this.getServletContext().getAttribute("drop_api") == null)
		{
		    this.getServletContext().setAttribute("drop_api", apiClient);
		}

	    if ( this.getServletContext().getAttribute("load_counter") == null)
		{
		    Integer counter = 0;
		    this.getServletContext().setAttribute("load_counter", counter);
		}

	    if ( this.getServletContext().getAttribute("droplet_ips") == null)
		{
		    ArrayList<String> droplet_ips = new ArrayList<String>();
		    droplet_ips = GetDropletIPs(apiClient);
		    this.getServletContext().setAttribute("droplet_ips", droplet_ips);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = this.getServletContext();

		ArrayList<image_meta> media = new ArrayList<image_meta>();
		String json_response = "";

		Integer counter = (Integer)context.getAttribute("load_counter");
		counter++;
		context.setAttribute("load_counter",counter);

		countDown decrement = new countDown(context);
		Timer timer = new Timer();
		timer.schedule(decrement,10);

		if(request.getParameter("all") != null)
		    {
			fillMedia(request, media);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			//json_response = json_string_out(media);
			out.print(json_response);
		    }
		else if(request.getParameter("search") != null)
		    {
			searchMedia(request, media, request.getParameter("search"));
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			//json_response = json_string_out(media);
			out.print(json_response);
		    }
		else
		    {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		    }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}


    protected void searchMedia(HttpServletRequest request, ArrayList<image_meta> media, String search)
    {
	String username = "python_user";
	String password = "no_password";
	String host     = "localhost";
	String port   = "3306";
	String dbName = "cs437project";

	String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
	try{
	    // Dynamically include the MySQL Driver
	    Class.forName("org.gjt.mm.mysql.Driver").newInstance();

	    // Instantiate the Driver
	    Driver driver = new org.gjt.mm.mysql.Driver();

	    // Connect to the database
	    Connection connection = (Connection) DriverManager.getConnection(url, username, password);
	    
	    String query = "SELECT * FROM image_meta where FileName LIKE \"%" + search + "%\" OR FileId LIKE \"%" + search + "%\";";

	    
	    // Create Statement
	    Statement statement = (Statement) connection.createStatement();
	    
	    // Query the database for all Shops
	    ResultSet resultSet = statement.executeQuery(query);

	    //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");	    
	    System.out.println(query);

	    //System.out.println("Iterating Over Results");

	    while(resultSet.next() )
		{
		    media.add(new image_meta(
					    resultSet.getString("FileId"),
					    resultSet.getString("Filename"),
					    resultSet.getString("Filetype"),
					    resultSet.getLong("FileSize"),
					    resultSet.getString("DateAdded"),
					    resultSet.getString("Link")
					    ));
		}
	    
	    // Tidy Up
	    connection.close();
	    
	    
	}catch (SQLException sqle){
	}catch(Exception e){
	}

	
    }

    protected void fillMedia(HttpServletRequest request, ArrayList<image_meta> media)
    {
	String username = "python_user";
	String password = "no_password";
	String host     = "localhost";
	String port   = "3306";
	String dbName = "cs437project";

	String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
	try{
	    // Dynamically include the MySQL Driver
	    Class.forName("org.gjt.mm.mysql.Driver").newInstance();

	    // Instantiate the Driver
	    Driver driver = new org.gjt.mm.mysql.Driver();

	    // Connect to the database
	    Connection connection = (Connection) DriverManager.getConnection(url, username, password);
	    
	    String query = "SELECT * FROM image_meta";

	    
	    // Create Statement
	    Statement statement = (Statement) connection.createStatement();
	    
	    // Query the database for all Shops
	    ResultSet resultSet = statement.executeQuery(query);
	    
	    
	    //System.out.println("Iterating Over Results");
	    while(resultSet.next() )
		{
		    media.add(new image_meta(
					    resultSet.getString("FileId"),
					    resultSet.getString("Filename"),
					    resultSet.getString("Filetype"),
					    resultSet.getLong("FileSize"),
					    resultSet.getString("DateAdded"),
					    resultSet.getString("Link")
					    ));
		}
	    
	    // Tidy Up
	    connection.close();
	    
	    
	}catch (SQLException sqle){
	}catch(Exception e){
	}

	
    }

    protected void RoundRobinLoadBalance(ServletContext context, PrintWriter out)
    {
	//Make new droplet and remove droplet after a specified time (Or load goes down on servlet).
    }

    protected ArrayList<String> GetDropletIPs(DigitalOcean apiClient)
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
			    System.out.println(net.getIpAddress());
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

	protected void readInStaticHTML(String pathIn, PrintWriter out)
	{
		//I had to review how to open and read a file from the Java Documentation.
		//The code for this function is mostly based on this link:
		//http://docs.oracle.com/javase/tutorial/essential/io/file.html
		Path file = Paths.get(pathIn);
		try(InputStream in = Files.newInputStream(file);
				BufferedReader reader = new BufferedReader(new InputStreamReader(in)))
				{
			String line = null;
			while((line = reader.readLine()) != null){
				out.println(line);
			}
			reader.close();
			in.close();
				}catch(IOException x){
					System.err.println(x);
				}
	}

}
