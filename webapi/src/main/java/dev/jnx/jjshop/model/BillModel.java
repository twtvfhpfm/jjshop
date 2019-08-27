package dev.jnx.jjshop.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class BillModel {
    @Getter @Setter String date;
    @Getter @Setter String week;
    @Getter @Setter String month;
    @Getter @Setter BigDecimal inCome;

    public static BillModel of(String date, String week, String month, BigDecimal inCome){
        BillModel b = new BillModel();
        b.setDate(date);
        b.setWeek(week);
        b.setMonth(month);
        b.setInCome(inCome);
        return b;
    }
}
