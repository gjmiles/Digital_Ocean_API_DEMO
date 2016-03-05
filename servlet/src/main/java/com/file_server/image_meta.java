package com.file_server;

import com.file_server.MetaSerializer;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(using = MetaSerializer.class)
public class image_meta {
    private String file_name;
    private String file_type;
    private long file_size;
    private String file_date;

    public image_meta(String name, String type, long size, ZonedDateTime date){
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss z");	
	this.file_name = name;
	this.file_type = type;
	this.file_size = size;
	this.file_date = formatter.format(date);
    }

    public String getFilename() { return file_name; };
    public String getFileType() { return file_type; };
    public long getFileSize() { return file_size; };
    public String getFileDate() { return file_date; };
}