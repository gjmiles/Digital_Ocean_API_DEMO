package com.central_controller;

import com.central_controller.MetaSerializer;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(using = MetaSerializer.class)
public class image_meta {
    private String file_id;
    private String file_name;
    private String file_type;
    private long file_size;
    private String file_date;
    private String server_loc;

    public image_meta(String id, String name, String type, long size, String date, String link){
	//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss z");	
	this.file_id = id;
	this.file_name = name;
	this.file_type = type;
	this.file_size = size;
	this.file_date = date;//formatter.format(date);
	this.server_loc = link;
    }

    public String getFileid() { return file_id; };
    public String getFilename() { return file_name; };
    public String getFileType() { return file_type; };
    public long getFileSize() { return file_size; };
    public String getFileDate() { return file_date; };
    public String getFileLink() { return server_loc; };
}
