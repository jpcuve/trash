import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

public class AbsoluteTimeRangeForDay {
    public static void main(String[] args) {
        List<String> zones = Arrays.asList("Europe/London", "Europe/Paris", "America/Montevideo");
        zones.forEach(zone -> {
            System.out.println("Now: " + System.currentTimeMillis());
            System.out.println("Zone: " + zone);
            ZoneId zoneId = ZoneId.of(zone);
            LocalDateTime now = LocalDateTime.now(zoneId);
            System.out.println(now);
            LocalDateTime startOfDay = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0, 0);
            LocalDateTime endOfDay = startOfDay.plusDays(1L);
            System.out.println("Range: " + startOfDay + " " + endOfDay);
            long start = startOfDay.atZone(zoneId).toInstant().toEpochMilli();
            long end = endOfDay.atZone(zoneId).toInstant().toEpochMilli();
            System.out.println("Absolute range: " + start + " " + end);
        });
    }
}
