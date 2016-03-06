package com.central_controller;

import com.central_controller.image_meta;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import java.io.IOException;

public class MetaSerializer extends JsonSerializer<image_meta> {
    @Override
    public void serialize(image_meta meta, JsonGenerator jsonGenerator,
			  SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", meta.getFileid());
        jsonGenerator.writeStringField("filename", meta.getFilename());
        jsonGenerator.writeStringField("filetype", meta.getFileType());
        jsonGenerator.writeNumberField("filesize", meta.getFileSize());
        jsonGenerator.writeStringField("filedate", meta.getFileDate());
        jsonGenerator.writeStringField("link", meta.getFileLink());
        jsonGenerator.writeEndObject();
    }
}
