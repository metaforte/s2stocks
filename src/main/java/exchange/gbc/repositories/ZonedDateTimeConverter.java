package exchange.gbc.repositories;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
/*
 * Used to convert ZonedDateTime to java.sql.TimeStamp.
 * Service layers should expect UTC time zone values in returned value from db.
 * Care should be taken to store only UTC time stamps into DB.
 * User time zones are to be handled at UI layer.
 * 
 */
@Converter(autoApply = true)
public class ZonedDateTimeConverter implements AttributeConverter<java.time.ZonedDateTime, java.sql.Timestamp> {

    @Override
    public java.sql.Timestamp convertToDatabaseColumn(ZonedDateTime entityValue) {
       return Timestamp.from(entityValue.toInstant());
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(java.sql.Timestamp databaseValue) {
        LocalDateTime localDateTime = databaseValue.toLocalDateTime();
        return localDateTime.atZone(ZoneId.of("UTC"));
    }

}
