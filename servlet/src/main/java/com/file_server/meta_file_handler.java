package com.file_server;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Driver;
import com.mysql.jdbc.Statement;

import cs320.hw3.CoffeeShopLocator.CoffeeShopBean;

/**
 * Servlet implementation class BorrowController
 */
@WebServlet("/FinalExam/BorrowController")
public class BorrowController extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BorrowController() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	// TODO Auto-generated method stub
	String myName, myType;
	RequestDispatcher dispatcher;
	ArrayList<MediaBean> media = new ArrayList<MediaBean>();
	String success = "";
	System.out.println(Boolean.parseBoolean(request.getParameter("lend")));
	System.out.println(Boolean.parseBoolean(request.getParameter("type")));

	if(request.getParameter("meta") != null)
	    {
		searchBeans(request, media, request.getParameter("search"));
		dispatcher = request.getRequestDispatcher("/WEB-INF/FinalExam/DisplayAllMediaView.jsp");
		request.setAttribute( "medias", media );

		dispatcher.forward(request,response);
		return;

	    }
	if(Boolean.parseBoolean(request.getParameter("return")))
	    {

		returnBean(request,request.getParameter("returnTitle"));
		dispatcher = request.getRequestDispatcher("/WEB-INF/FinalExam/DisplayAllMediaView.jsp");
		fillBeans(request, media);

		request.setAttribute( "medias", media );

		dispatcher.forward(request,response);
		return;
	    }
	if(Boolean.parseBoolean(request.getParameter("changeBean")))
	    {
		System.out.println("Changing");
		dispatcher = request.getRequestDispatcher("/WEB-INF/FinalExam/LendView.jsp");
		if(request.getParameter("borrowItem") != null)
		    {
			dispatcher = request.getRequestDispatcher("/WEB-INF/FinalExam/DisplayAllMediaView.jsp");
			changeBeans(request,request.getParameter("mediaName"),request.getParameter("borrowItem"));
			fillBeans(request, media);

			request.setAttribute( "medias", media );
			dispatcher.forward(request,response);
			return;
		    }
		else
		    dispatcher.forward(request,response);
	    }
	if(Boolean.parseBoolean(request.getParameter("meta")))
	    {
		System.out.println("IN lend");
		dispatcher = request.getRequestDispatcher("/WEB-INF/FinalExam/LendView.jsp");
		request.setAttribute("mediaName", request.getParameter("name"));
		request.setAttribute("mediaType", request.getParameter("type"));
		request.setAttribute("queryName", request.getParameter("name").replaceAll(" ", "_"));
		request.setAttribute("queryType", request.getParameter("type").replaceAll(" ", "_"));
		dispatcher.forward(request,response);
	    }
	else if(Boolean.parseBoolean(request.getParameter("goDisplay")))
	    {
		System.out.println("IN media");

		dispatcher = request.getRequestDispatcher("/WEB-INF/FinalExam/DisplayAllMediaView.jsp");
		fillBeans(request, media);

		// Attach the 'users' collection to the request object
		request.setAttribute( "medias", media );
		dispatcher.forward(request, response);
		return;
	    }
	else 
	    {
		dispatcher = request.getRequestDispatcher("/WEB-INF/FinalExam/AddMediaView.jsp");
		
		if( request.getParameter("dropDown") == ""
		    || request.getParameter("AddMenu") == null)
		    {
			//System.out.println("Failed Check");
			dispatcher.forward(request, response);
			return;
		    }
		else
		    {
			try
			    {
				MediaBean addMedia = new MediaBean();
				Date now = new Date();
				success = "Record Added.";
				myType = request.getParameter("dropDown").trim();
				myName = request.getParameter("AddMenu").trim();
				System.out.println(myName + " " + myType);
				addMedia.setType(myType);
				addMedia.setTitle(myName);
				addMedia.setDateAdded(now.toString());
				addBeans(request,addMedia);
				// Forward the request and response to the view
				request.setAttribute("success", success);
				dispatcher.forward(request, response);
			    }
			catch( Exception e)
			    {
				dispatcher.forward(request, response);
				return;
			    }
		    }
	    }

	
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	// TODO Auto-generated method stub
	doGet(request,response);
    }
    
    protected void addBeans(HttpServletRequest request, MediaBean media)
    {
	String username = "cs320stu66";
	String password = "q6Y!I*Jd";
	String host     = "cs3.calstatela.edu";
	String port   = "3306";
	String dbName = "cs320stu66";

	String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
	try{
	    // Dynamically include the MySQL Driver
	    Class.forName("org.gjt.mm.mysql.Driver").newInstance();

	    // Instantiate the Driver
	    Driver driver = new org.gjt.mm.mysql.Driver();

	    // Connect to the database
	    Connection connection = (Connection) DriverManager.getConnection(url, username, password);
	    
	    //String query = "SELECT StoreId,Name,Phone,Street1,Street2,Street3,City,Country,PostalCode,Latitude,Longitude FROM CoffeeShops where Country = \"US\" ";
	    String query = "INSERT INTO `Media` VALUES" +"(" + "\""+ media.getType() + "\"" + "," + 
		"\""+media.getTitle() + "\"" + "," + "\""+media.getDateAdded() + "\"" + "," + "\"False" + "\"" + "," + "\"\"" + "," + "\"\"" + ");";
	    
	    // Create Statement
	    Statement statement = (Statement) connection.createStatement();
	    statement.execute(query);
	    // Tidy Up
	    connection.close();
	    
	    
	}catch (SQLException sqle){
	}catch(Exception e){
	}

	
    }
    
    protected void returnBean(HttpServletRequest request, String name)
    {
	String username = "cs320stu66";
	String password = "q6Y!I*Jd";
	String host     = "cs3.calstatela.edu";
	String port   = "3306";
	String dbName = "cs320stu66";

	String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
	try{
	    // Dynamically include the MySQL Driver
	    Class.forName("org.gjt.mm.mysql.Driver").newInstance();

	    // Instantiate the Driver
	    Driver driver = new org.gjt.mm.mysql.Driver();
	    
	    name = name.replaceAll("_", " ");
	    // Connect to the database
	    Connection connection = (Connection) DriverManager.getConnection(url, username, password);
	    
	    //String query = "SELECT StoreId,Name,Phone,Street1,Street2,Street3,City,Country,PostalCode,Latitude,Longitude FROM CoffeeShops where Country = \"US\" ";
	    String query = "UPDATE `Media` set Loan=\"false\" WHERE Title=\"" + name + "\";";
	    System.out.println(query);

	    // Create Statement
	    Statement statement = (Statement) connection.createStatement();
	    statement.execute(query);
	    query = "UPDATE `Media` set Borrower=\"\" WHERE Title=\"" + name + "\";";
	    System.out.println(query);
	    Statement newStatement = (Statement) connection.createStatement();

	    newStatement.execute(query);
	    query = "UPDATE `Media` set DateOut=\"\" WHERE Title=\"" + name + "\";";
	    statement.execute(query);
	    // Tidy Up
	    connection.close();
	    
	    
	}catch (SQLException sqle){
	}catch(Exception e){
	}
    }
    protected void changeBeans(HttpServletRequest request, String name, String borrowItem)
    {
	String username = "cs320stu66";
	String password = "q6Y!I*Jd";
	String host     = "cs3.calstatela.edu";
	String port   = "3306";
	String dbName = "cs320stu66";

	String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
	try{
	    // Dynamically include the MySQL Driver
	    Class.forName("org.gjt.mm.mysql.Driver").newInstance();

	    // Instantiate the Driver
	    Driver driver = new org.gjt.mm.mysql.Driver();
	    
	    name = name.replaceAll("_", " ");
	    // Connect to the database
	    Connection connection = (Connection) DriverManager.getConnection(url, username, password);
	    
	    //String query = "SELECT StoreId,Name,Phone,Street1,Street2,Street3,City,Country,PostalCode,Latitude,Longitude FROM CoffeeShops where Country = \"US\" ";
	    String query = "UPDATE `Media` set Loan=\"true\" WHERE Title=\"" + name + "\";";
	    System.out.println(query);

	    // Create Statement
	    Statement statement = (Statement) connection.createStatement();
	    statement.execute(query);
	    query = "UPDATE `Media` set Borrower=\"" + borrowItem +"\" WHERE Title=\"" + name + "\";";
	    System.out.println(query);
	    Statement newStatement = (Statement) connection.createStatement();

	    newStatement.execute(query);
	    query = "UPDATE `Media` set DateOut=\"" + new Date().toString() +"\" WHERE Title=\"" + name + "\";";
	    statement.execute(query);
	    // Tidy Up
	    connection.close();
	    
	    
	}catch (SQLException sqle){
	}catch(Exception e){
	}

	
    }
    protected void fillBeans(HttpServletRequest request, ArrayList<MediaBean> media)
    {
	String username = "cs320stu66";
	String password = "q6Y!I*Jd";
	String host     = "cs3.calstatela.edu";
	String port   = "3306";
	String dbName = "cs320stu66";
	Double distance;

	String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
	try{
	    // Dynamically include the MySQL Driver
	    Class.forName("org.gjt.mm.mysql.Driver").newInstance();

	    // Instantiate the Driver
	    Driver driver = new org.gjt.mm.mysql.Driver();

	    // Connect to the database
	    Connection connection = (Connection) DriverManager.getConnection(url, username, password);
	    
	    //String query = "SELECT StoreId,Name,Phone,Street1,Street2,Street3,City,Country,PostalCode,Latitude,Longitude FROM CoffeeShops where Country = \"US\" ";
	    String query = "SELECT * FROM Media";

	    
	    // Create Statement
	    Statement statement = (Statement) connection.createStatement();
	    
	    // Query the database for all Shops
	    ResultSet resultSet = statement.executeQuery(query);
	    
	    
	    //System.out.println("Iterating Over Results");
	    while(resultSet.next() )
		{
		    media.add(new MediaBean(
					    resultSet.getString("Type"),
					    resultSet.getString("Title"),
					    resultSet.getString("DateAdded"),
					    resultSet.getString("Loan"),
					    resultSet.getString("DateOut"),
					    resultSet.getString("Borrower")
					    ));
		}
	    
	    // Tidy Up
	    connection.close();
	    
	    
	}catch (SQLException sqle){
	}catch(Exception e){
	}

	
    }
    
    protected void searchBeans(HttpServletRequest request, ArrayList<MediaBean> media, String search)
    {
	String username = "cs320stu66";
	String password = "q6Y!I*Jd";
	String host     = "cs3.calstatela.edu";
	String port   = "3306";
	String dbName = "cs320stu66";
	Double distance;

	String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
	try{
	    // Dynamically include the MySQL Driver
	    Class.forName("org.gjt.mm.mysql.Driver").newInstance();

	    // Instantiate the Driver
	    Driver driver = new org.gjt.mm.mysql.Driver();

	    // Connect to the database
	    Connection connection = (Connection) DriverManager.getConnection(url, username, password);
	    
	    //String query = "SELECT StoreId,Name,Phone,Street1,Street2,Street3,City,Country,PostalCode,Latitude,Longitude FROM CoffeeShops where Country = \"US\" ";
	    String query = "SELECT * FROM Media where Title LIKE \"%" + search + "%\" OR Borrower LIKE \"%" + search + "%\";";

	    
	    // Create Statement
	    Statement statement = (Statement) connection.createStatement();
	    
	    // Query the database for all Shops
	    ResultSet resultSet = statement.executeQuery(query);
	    
	    System.out.println(query);

	    //System.out.println("Iterating Over Results");
	    while(resultSet.next() )
		{
		    media.add(new MediaBean(
					    resultSet.getString("Type"),
					    resultSet.getString("Title"),
					    resultSet.getString("DateAdded"),
					    resultSet.getString("Loan"),
					    resultSet.getString("DateOut"),
					    resultSet.getString("Borrower")
					    ));
		}
	    
	    // Tidy Up
	    connection.close();
	    
	    
	}catch (SQLException sqle){
	}catch(Exception e){
	}

	
    }

}
