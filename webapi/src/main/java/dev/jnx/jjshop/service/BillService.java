package dev.jnx.jjshop.service;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.dao.BillDao;
import dev.jnx.jjshop.model.BillModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.IsoFields;
import java.util.*;

@Service
public class BillService {
    @Autowired private BillDao billDao;

    public RespBean getBill(String cursor, int type){
        List<BillModel> bl=new ArrayList<>();
        boolean firstQuery = cursor.isEmpty();
        if(firstQuery) cursor = "999999";
        switch(type){
            case 0: bl = billDao.getDailyBill(cursor); break;
            case 1: bl = billDao.getWeeklyBill(cursor); break;
            case 2: bl = billDao.getMonthlyBill(cursor); break;
        }
        String msg;
        if (bl.size() < 20){
            msg = "end";
        }else{
            msg = "continue";
        }
        List<BillModel> fbl = new ArrayList<>();
        if (type == 0){
            Calendar date = Calendar.getInstance();
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());
            for(BillModel model : bl){
                while( dateStr.compareTo(model.getDate()) > 0){
                    fbl.add(BillModel.of(dateStr, "", "", BigDecimal.ZERO));
                    date.add(Calendar.DATE, -1);
                    dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());
                }
                fbl.add(model);
                date.add(Calendar.DATE, -1);
                dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());
            }
        }else if (type == 1){
            ZoneId zoneId = ZoneId.of ( "+8" );
            ZonedDateTime now = ZonedDateTime.now ( zoneId );
            int week = now.get ( IsoFields.WEEK_OF_WEEK_BASED_YEAR );
            int weekYear = now.get ( IsoFields.WEEK_BASED_YEAR );
            String dateStr = String.format("%d%02d", weekYear, week);
            for(BillModel model : bl){
                while( dateStr.compareTo(model.getWeek()) > 0){
                    fbl.add(BillModel.of("", dateStr, "", BigDecimal.ZERO));
                    week -= 1;
                    dateStr = String.format("%d%02d", weekYear, week);
                }
                fbl.add(model);
                week -= 1;
                dateStr = String.format("%d%02d", weekYear, week);
            }
        }else if (type == 2){
            Calendar date = Calendar.getInstance();
            String dateStr = new SimpleDateFormat("yyyy-MM").format(date.getTime());
            for(BillModel model : bl){
                while( dateStr.compareTo(model.getMonth()) > 0){
                    fbl.add(BillModel.of("", "", dateStr, BigDecimal.ZERO));
                    date.add(Calendar.MONTH, -1);
                    dateStr = new SimpleDateFormat("yyyy-MM").format(date.getTime());
                }
                fbl.add(model);
                date.add(Calendar.MONTH, -1);
                dateStr = new SimpleDateFormat("yyyy-MM").format(date.getTime());
            }
        }
        return RespBean.ok(msg, fbl);
    }
}
