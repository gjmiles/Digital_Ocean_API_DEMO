package cs320.lab4.GetImageServlet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/lab4/GetImageServlet")
public class GetImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	
	public void init() throws ServletException {
		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = this.getServletContext();
		
		ByteBuffer buf;

		if(request.getParameter("name") != null && request.getParameter("name").trim().length() > 0)
		{
			response.setHeader("Content-Disposition: attachment", "filename=" + request.getParameter("name"));
			OutputStream outStream = response.getOutputStream();
			
			String fileName = context.getRealPath("images/" + request.getParameter("name"));
			Path file = Paths.get(fileName);
			buf = ByteBuffer.allocate((int) Files.size(file));

			readFileBinary(buf,fileName);
			outStream.write(buf.array());
			outStream.close();

		}
		else
		{
			response.setContentType("text/html");
			String firstPath = context.getRealPath("lab4/imageLinks.html");
			PrintWriter out = response.getWriter();
			readInStaticHTML(firstPath,out);
			out.close();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
	
	protected void readFileBinary(ByteBuffer buf,String fileName) throws IOException
	{

		//I used this site to see how to open a file to be read as binary data:
		//http://examples.javacodegeeks.com/core-java/nio/filechannel/read-from-channel-with-bytebuffer/
		ReadableByteChannel in = new FileInputStream(fileName).getChannel();
		int inBytes = 0;
		while(inBytes >= 0)
		{
			buf.rewind();
			inBytes = in.read(buf);
			buf.rewind();
		}
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