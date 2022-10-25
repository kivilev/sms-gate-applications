package com.kivilev.service.grpc.mapper;

import org.springframework.stereotype.Component;

@Component
//@Converter
public class ZonedDateTimeConverter {//implements AttributeConverter<ZonedDateTime, Timestamp> {

   /* private final Clock clock;

    public ZonedDateTimeConverter(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Timestamp convertToDatabaseColumn(ZonedDateTime zonedDateTime) {
        return zonedDateTime != null ? Timestamp.from(zonedDateTime.toInstant()) : null;
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(Timestamp dbData) {
        return dbData != null ? ZonedDateTime.ofInstant(dbData.toInstant(), clock.getZone()) : null;
    }*/
}