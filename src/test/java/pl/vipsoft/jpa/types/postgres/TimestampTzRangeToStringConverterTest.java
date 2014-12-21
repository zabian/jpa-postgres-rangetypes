package pl.vipsoft.jpa.types.postgres;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class TimestampTzRangeToStringConverterTest {

    private List<TimestampTzRange> getTestTimestampTzRanges() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2014);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date lowerDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 1);
        Date uperDate = calendar.getTime();

        ArrayList<TimestampTzRange> ranges = Lists.newArrayList();
        for (Date from : Arrays.asList(null, lowerDate)) {
            for (Date to : Arrays.asList(null, uperDate)) {
                for (boolean leftOpen : Arrays.asList(false, true)) {
                    for (boolean rightOpen : Arrays.asList(false, true)) {
                        TimestampTzRange range = new TimestampTzRange(from, to, leftOpen, rightOpen);
                        ranges.add(range);
                    }
                }
            }
        }
        return ranges;
    }

    private List<String> getTestRanges() {
        ArrayList<String> ranges = Lists.newArrayList();
        for (String from : Arrays.asList("-infinity", "\"2014-01-01 00:00:00+01\"")) {
            for (String to : Arrays.asList("infinity", "\"2015-01-01 00:00:00+01\"")) {
                for (String left : Arrays.asList("[", "(")) {
                    for (String right : Arrays.asList("]", ")")) {
                        String value = String.format("%s%s,%s%s", left, from, to, right);
                        ranges.add(value);
                    }
                }
            }
        }
        return ranges;
    }

    @Test
    public void patternTest() {
        Pattern pattern = Pattern.compile(TimestampTzRangeToStringConverter.PATTERN_STRING);
        List<String> testRanges = getTestRanges();
        for (String testRange : testRanges) {
            System.out.print("Matching: " + testRange);
            Matcher matcher = pattern.matcher(testRange);
            assertTrue(matcher.matches());
            System.out.println(" correct");
        }
    }

    @Test
    public void testConvertToDatabaseColumn() {
        List<TimestampTzRange> timestampTzRanges = getTestTimestampTzRanges();
        List<String> testRanges = getTestRanges();
        assertEquals(timestampTzRanges.size(), testRanges.size());

        TimestampTzRangeToStringConverter converter = new TimestampTzRangeToStringConverter();
        for (int i = 0; i < timestampTzRanges.size(); i++) {
            String expectedRange = testRanges.get(i);
            System.out.println(expectedRange);
            String convertionResult = converter.convertToDatabaseColumn(timestampTzRanges.get(i));
            assertEquals(expectedRange, convertionResult);
        }

    }

    @Test
    public void testConvertToEntityAttribute() {
        List<String> testRanges = getTestRanges();
        List<TimestampTzRange> timestampTzRanges = getTestTimestampTzRanges();
        assertEquals(timestampTzRanges.size(), testRanges.size());

        TimestampTzRangeToStringConverter converter = new TimestampTzRangeToStringConverter();
        for (int i = 0; i < testRanges.size(); i++) {
            TimestampTzRange expectedRange = timestampTzRanges.get(i);
            TimestampTzRange convertionResult = converter.convertToEntityAttribute(testRanges.get(i));
            assertEquals(expectedRange, convertionResult);
        }
    }
}