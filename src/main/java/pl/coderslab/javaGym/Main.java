package pl.coderslab.javaGym;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Main {

    public static void main(String[] args) {

        ZoneId zoneId = ZoneId.of("Poland");
        System.out.println(zoneId);
        System.out.println(ZonedDateTime.now(zoneId));

////        Integer recurency = 5;
////
////        for (int i = 0; i < recurency; i ++) {
////            Period weekly = Period.ofDays(7 * i);
////            LocalDateTime time = LocalDateTime.now().plus(weekly);
////            System.out.println(time.toString());
////        }
//
//        String uniqueID = UUID.randomUUID().toString();
//        System.out.println(uniqueID);

    }

}
