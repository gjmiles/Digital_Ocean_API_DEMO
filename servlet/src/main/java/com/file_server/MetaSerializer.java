package com.file_server;

import com.file_server.image_meta;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import java.io.IOException;

public class MetaSerializer extends JsonSerializer<image_meta> {
    @Override
    public void serialize(image_meta meta, JsonGenerator jsonGenerator,
			  SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("filename", meta.getFilename());
        jsonGenerator.writeStringField("filetype", meta.getFileType());
        jsonGenerator.writeNumberField("filesize", meta.getFileSize());
        jsonGenerator.writeStringField("filedate", meta.getFileDate());
        jsonGenerator.writeEndObject();
    }
}
