package src.downloadManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;


public class download_handler {
	
	private String directory;
	private long remeber_download;
	private long fileSize;

	  public download_handler() {
	    	this.remeber_download = 0;
	    }
	  
	public void setPath(String string_path)
	{
		directory = string_path;
	}
	
	public void setfileSize(long size)
	{
		this.fileSize = size;
	}
	
	public long download_file(String fileName, String ip_ad)
    {	
	//String rest_api_ip = ip_ad;
	//String rest_api_url = "http://" + rest_api_ip + "/servlet/GetImageServlet?name=" + fileName;
	String rest_api_url = ip_ad;
	//get_path("/home/ninelegs/");
	String dir = ("C:/Users/Andy/Desktop/eclipse437/DownloadManager/src/");
	try{
	    URL download_url = new URL(rest_api_url);
		//URL download_url = new URL(ip_ad);
	    InputStream attempt_connect = download_url.openStream();
	    
	    
	    File save_file =  new File(dir + fileName);
	    OutputStream save_stream = new FileOutputStream(save_file);
	    byte[] buffer = new byte[1024*8];
	    int bytes_read = 0;
	    
	    int last_download_pos = 0;
	    
	    while((bytes_read = attempt_connect.read(buffer)) != -1)
		{
	    	remeber_download += bytes_read;
	    	save_stream.write(buffer,last_download_pos,bytes_read);
		}
	    attempt_connect.close();
	    save_stream.close();
	    
	}
	catch (MalformedURLException e){
	    e.printStackTrace();
	} catch(IOException e) {
	    e.printStackTrace();
	}

	return remeber_download;
	
	
    }

	
}
