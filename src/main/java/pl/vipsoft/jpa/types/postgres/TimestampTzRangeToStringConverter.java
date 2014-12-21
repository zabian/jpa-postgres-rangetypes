package pl.vipsoft.jpa.types.postgres;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zabian on 12.12.14.
 */
@Converter(autoApply = true)
public class TimestampTzRangeToStringConverter implements AttributeConverter<TimestampTzRange, String> {

    static final String PATTERN_STRING = "^(\\[|\\()" +
        "(-infinity|\"[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}[+-][0-9]{2}\")," +
        "(infinity|\"[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}[+-][0-9]{2}\")(\\)|\\])$";
    public static final String TIMESTAMP_PATTERN = "\"yyyy-MM-dd HH:mm:ssX\"";

    @Override
    public String convertToDatabaseColumn(TimestampTzRange attribute) {
        SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_PATTERN);
        String range;
        range = attribute.isLeftOpen() ? "(" : "[";
        range += attribute.isInfinityFrom() ? "-infinity" : sdf.format(attribute.getDateFrom());
        range += ",";
        range += attribute.isInfinityTo() ? "infinity" : sdf.format(attribute.getDateTo());
        range += attribute.isRightOpen() ? ")" : "]";
        return range;
    }

    @Override
    public TimestampTzRange convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            System.out.println("null value");
            return null;
        }
        TimestampTzRange range = new TimestampTzRange();
        if ("empty".equals(dbData)) {
            return range;
        }
        Pattern pattern = Pattern.compile(PATTERN_STRING);
        Matcher matcher = pattern.matcher(dbData);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid value: " + dbData);
        }
        boolean leftOpen = "(".equals(matcher.group(1));
        SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_PATTERN);
        String lowerBound = matcher.group(2);
        Date from = null;
        if (!"-infinity".equals(lowerBound)) {
            try {
                from = sdf.parse(lowerBound);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        String upperBound = matcher.group(3);
        Date to = null;
        if (!"infinity".equals(upperBound)) {
            try {
                to = sdf.parse(upperBound);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        boolean rightOpen = ")".equals(matcher.group(4));
        return new TimestampTzRange(from, to, leftOpen, rightOpen);
    }
}
