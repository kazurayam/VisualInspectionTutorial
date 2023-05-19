package my.sample;

import com.kazurayam.materialstore.core.JobTimestamp;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class T07JobTimestampOperationsTest {

    @Test
    public void test_now() {
        JobTimestamp now = JobTimestamp.now();
        System.out.println("now=" + now.toString());
    }

    @Test
    public void test_constructor_from_string() {
        String ts = "20230519_204902";
        JobTimestamp jt = new JobTimestamp(ts);
        assertEquals(ts, jt.toString());
    }

    @Test
    public void test_constructor_from_LocalDateTime() {
        LocalDateTime now = LocalDateTime.now();
        JobTimestamp jt = JobTimestamp.create(now);
        System.out.println("jt=" + jt.toString());
    }

    @Test
    public void test_value() {
        JobTimestamp jt = JobTimestamp.now();
        LocalDateTime ldt = jt.value();
        System.out.println("ldt=" + ldt.toString());
    }

    @Test
    public void test_laterThan() {
        JobTimestamp base = JobTimestamp.now();
        JobTimestamp jt1 = JobTimestamp.laterThan(base);
        JobTimestamp jt2 = JobTimestamp.laterThan(base, jt1);
        System.out.println(String.format("base=%s, jt1=%s, jt2=%s",
                base.toString(), jt1.toString(), jt2.toString()));
    }

    @Test
    public void test_theTimeOrLaterThan() {
        JobTimestamp thanThis = new JobTimestamp("20230516_010101");
        JobTimestamp theTime  = new JobTimestamp("20230516_010102");
        assertEquals(theTime,
                JobTimestamp.theTimeOrLaterThan(thanThis, theTime));
        //
        thanThis = new JobTimestamp("20230516_010102");
        theTime  = new JobTimestamp("20230516_010101");
        assertEquals(new JobTimestamp("20230516_010103"),
                JobTimestamp.theTimeOrLaterThan(thanThis, theTime));
    }

    @Test
    public void test_max() {
        JobTimestamp jt1 = new JobTimestamp("20220216_070203");
        JobTimestamp jt2 = new JobTimestamp("20230516_010800");
        JobTimestamp max = JobTimestamp.max(jt1, jt2);
        assertEquals(jt2, max);
    }

    @Test
    public void test_plus() {
        JobTimestamp base = new JobTimestamp("20230516_010800");
        JobTimestamp calc = base.plusDays(1).plusHours(2)
                .plusMinutes(3).plusSeconds(4);
        assertEquals(new JobTimestamp("20230517_031104"), calc);
    }

    @Test
    public void test_minus() {
        JobTimestamp base = new JobTimestamp("20230516_111810");
        JobTimestamp calc = base.minusDays(1).minusHours(2)
                .minusMinutes(3).minusSeconds(4);
        assertEquals(new JobTimestamp("20230515_091506"), calc);
    }

    @Test
    public void test_endOfTheMonth() {
        JobTimestamp base = new JobTimestamp("20230516_111810");
        assertEquals(new JobTimestamp("20230531_235959"),
                base.endOfTheMonth());
    }

    @Test
    public void test_beginningOfTheMonth() {
        JobTimestamp base = new JobTimestamp("20230516_111810");
        assertEquals(new JobTimestamp("20230501_000000"),
                base.beginningOfTheMonth());
    }

    @Test
    public void test_betweenSeconds() {
        JobTimestamp jt1 = new JobTimestamp("20230516_000000");
        JobTimestamp jt2 = new JobTimestamp("20230516_030405");
        assertEquals(3 * 60 * 60 + 4 * 60 + 5,
                JobTimestamp.betweenSeconds(jt1, jt2));
    }

    @Test
    public void test_equals() {
        JobTimestamp jt1 = new JobTimestamp("20230516_000000");
        JobTimestamp jt2 = new JobTimestamp("20230516_030405");
        JobTimestamp jt3 = new JobTimestamp("20230516_030405");
        assertFalse(jt1.equals(jt2));
        assertTrue(jt2.equals(jt3));
    }

    @Test
    public void test_compareTo() {
        JobTimestamp jt1 = new JobTimestamp("20230516_000000");
        JobTimestamp jt2 = new JobTimestamp("20230516_030405");
        JobTimestamp jt3 = new JobTimestamp("20230516_030405");
        assertTrue(jt1.compareTo(jt2) < 0);
        assertTrue(jt2.compareTo(jt3) == 0);
        assertTrue(jt2.compareTo(jt1) > 0);
    }

    @Test
    public void test_isValid() {
        assertTrue(JobTimestamp.isValid("20230516_030405"));
        assertFalse(JobTimestamp.isValid("this is not a valid JobTimestamp"));
    }
}
