package com.beassolution.rule.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * ObjectMapper configuration class for the Beas Rule Engine.
 * 
 * <p>This class configures the Jackson ObjectMapper with custom serialization
 * and deserialization settings including:
 * <ul>
 *   <li>Custom date/time serialization formats</li>
 *   <li>Null value handling</li>
 *   <li>Case-insensitive property mapping</li>
 *   <li>Custom serializers and deserializers</li>
 * </ul>
 * 
 * <p>The configuration ensures consistent JSON handling across the application
 * with proper date/time formatting and error handling.
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class ObjectMapperConfig {

    /**
     * Creates and configures the ObjectMapper with custom settings.
     * 
     * <p>This bean configures the Jackson ObjectMapper with specific settings
     * for optimal JSON serialization and deserialization in the rule engine context.
     * 
     * <p>Configuration includes:
     * <ul>
     *   <li>Custom date/time serialization</li>
     *   <li>Null value exclusion</li>
     *   <li>Case-insensitive property mapping</li>
     *   <li>Custom serializers for OffsetDateTime and LocalDate</li>
     * </ul>
     * 
     * @return Configured ObjectMapper instance
     */
    @Bean(name = "objectMapper")
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = JsonMapper
                .builder()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
                .build();

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(OffsetDateTime.class, new OffsetDateTimeSerializer());
        javaTimeModule.addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer());
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }

    /**
     * Custom serializer for OffsetDateTime objects.
     * 
     * <p>This serializer converts OffsetDateTime objects to ISO instant format
     * strings for consistent JSON representation.
     * 
     * @author Beas Solution Team
     * @version 1.0
     * @since 1.0
     */
    public static class OffsetDateTimeSerializer extends JsonSerializer<OffsetDateTime> {
        /**
         * Serializes an OffsetDateTime to ISO instant format.
         * 
         * @param offsetDateTime The OffsetDateTime to serialize
         * @param jsonGenerator The JsonGenerator to write to
         * @param serializerProvider The SerializerProvider
         * @throws IOException if serialization fails
         */
        @Override
        public void serialize(OffsetDateTime offsetDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(DateTimeFormatter.ISO_INSTANT.format(offsetDateTime));
        }
    }

    /**
     * Custom deserializer for OffsetDateTime objects.
     * 
     * <p>This deserializer converts ISO instant format strings back to
     * OffsetDateTime objects.
     * 
     * @author Beas Solution Team
     * @version 1.0
     * @since 1.0
     */
    public static class OffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {
        /**
         * Deserializes an ISO instant format string to OffsetDateTime.
         * 
         * @param jsonParser The JsonParser to read from
         * @param deserializationContext The DeserializationContext
         * @return Parsed OffsetDateTime
         * @throws IOException if deserialization fails
         */
        @Override
        public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return OffsetDateTime.parse(jsonParser.getText());
        }
    }

    /**
     * Custom serializer for LocalDate objects.
     * 
     * <p>This serializer converts LocalDate objects to ISO instant format
     * strings by treating them as midnight UTC on the given date.
     * 
     * @author Beas Solution Team
     * @version 1.0
     * @since 1.0
     */
    public static class LocalDateSerializer extends JsonSerializer<LocalDate> {
        /**
         * Serializes a LocalDate to ISO instant format.
         * 
         * @param localDate The LocalDate to serialize
         * @param jsonGenerator The JsonGenerator to write to
         * @param serializerProvider The SerializerProvider
         * @throws IOException if serialization fails
         */
        @Override
        public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            OffsetDateTime offsetDateTime = OffsetDateTime.of(localDate, LocalTime.of(0, 0), ZoneOffset.UTC);
            jsonGenerator.writeString(DateTimeFormatter.ISO_INSTANT.format(offsetDateTime));
        }
    }

    /**
     * Custom deserializer for LocalDate objects.
     * 
     * <p>This deserializer converts ISO instant format strings to LocalDate
     * objects by extracting the date part from the instant.
     * 
     * @author Beas Solution Team
     * @version 1.0
     * @since 1.0
     */
    public static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
        /**
         * Deserializes an ISO instant format string to LocalDate.
         * 
         * @param jsonParser The JsonParser to read from
         * @param deserializationContext The DeserializationContext
         * @return Parsed LocalDate
         * @throws IOException if deserialization fails
         */
        @Override
        public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return OffsetDateTime.parse(jsonParser.getText()).toLocalDate();
        }
    }
}