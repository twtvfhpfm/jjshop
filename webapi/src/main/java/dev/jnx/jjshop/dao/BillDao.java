package dev.jnx.jjshop.dao;

import dev.jnx.jjshop.model.BillModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillDao {
    List<BillModel> getDailyBill(String lastMinDate);
    List<BillModel> getWeeklyBill(String lastMinWeek);
    List<BillModel> getMonthlyBill(String lastMinMonth);
}
