

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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/GetImageServlet")
public class sample_retriever extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	
	public void init() throws ServletException {
		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = this.getServletContext();
		
		ByteBuffer buf;
		long file_size;
		long file_left = 0;

		if(request.getParameter("name") != null && request.getParameter("name").trim().length() > 0)
		{
			response.setHeader("Content-Disposition: attachment", "filename=" + request.getParameter("name"));
			OutputStream outStream = response.getOutputStream();
			
			String fileName = context.getRealPath("geoimages/" + request.getParameter("name"));
			System.out.println(fileName);
			Path file = Paths.get(fileName);
			file_size = (long) Files.size(file);
			System.out.println(file_size);
			
			//while(file_left <= file_size)
			//{
			
			//buf.put(b);
			//ReadableByteChannel in = new FileInputStream(fileName).getChannel();
			FileChannel in = new FileInputStream(fileName).getChannel();
			buf = ByteBuffer.allocate(2048);
			long offset = 0;
			while(offset <= file_size)
			{
				int length = 0;
				if((file_size - offset) < 2048)
				{
					System.out.println((file_size - offset));
					buf = ByteBuffer.allocate((int) (file_size - offset));
				}

				while(in.read(buf, offset) > 0)
				{
					buf.flip();
					while(buf.hasRemaining())
					{
						//readFileBinary(buf,fileName);
						//outStream.write(buf.get());
						outStream.write(buf.get());
					}
					//file_left++;
					//}
				}
				buf.rewind();
				offset += 2048;
			}
			
			//Overshot
			/*
			if((offset - file_size) != 0)
			{
				System.out.println(offset);
				buf.rewind();
				while(in.read(buf, (file_size - (offset - file_size))) > 0)
				{
					buf.flip();
					while(buf.hasRemaining())
					{
						//readFileBinary(buf,fileName);
						//outStream.write(buf.get());
						outStream.write(buf.get());
					}
					//file_left++;
					//}
				}
				
			}*/
			outStream.close();

		}
		else
		{
			response.setContentType("text/html");
			String firstPath = context.getRealPath("image_links/imageLinks.html");
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
			buf.flip();
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