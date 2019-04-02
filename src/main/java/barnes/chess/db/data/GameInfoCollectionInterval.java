package barnes.chess.db.data;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;

public enum GameInfoCollectionInterval {
  DAILY {
    @Override
    Timestamp getStart(Date date) {
      Calendar cal = GameInfoCollectionInterval.getCalendar(date);
      cal.set(cal.get(YEAR), cal.get(MONTH), cal.get(DAY_OF_MONTH), 0, 0, 0);
      return new Timestamp(cal.getTimeInMillis());
    }

    @Override
    Timestamp getEnd(Date date) {
      Calendar cal = GameInfoCollectionInterval.getCalendar(date);
      cal.set(cal.get(YEAR), cal.get(MONTH), cal.get(DAY_OF_MONTH), 23, 59, 59);
      return new Timestamp(cal.getTimeInMillis());
    }
  }, WEEKLY {
    @Override
    Timestamp getStart(Date date) {
      Calendar cal = GameInfoCollectionInterval.getCalendar(date);
      int dayOfWeek = (cal.get(DAY_OF_WEEK) + 5) % 7;
      cal.add(DAY_OF_MONTH, -dayOfWeek);
      cal.set(cal.get(YEAR), cal.get(MONTH), cal.get(DAY_OF_MONTH), 0, 0, 0);
      return new Timestamp(cal.getTimeInMillis());
    }

    @Override
    Timestamp getEnd(Date date) {
      Calendar cal = GameInfoCollectionInterval.getCalendar(date);
      int dayOfWeek = (cal.get(DAY_OF_WEEK) + 5) % 7;
      cal.add(DAY_OF_MONTH, -dayOfWeek);
      cal.set(cal.get(YEAR), cal.get(MONTH), cal.get(DAY_OF_MONTH) + 6, 23, 59, 59);
      return new Timestamp(cal.getTimeInMillis());
    }
  }/*, MONTHLY {
    @Override
    Timestamp getStart(Date date) {

    }

    @Override
    Timestamp getEnd(Date date) {
      return null;
    }
  }, OVERALL {
    @Override
    Timestamp getStart(Date date) {
      return null;
    }

    @Override
    Timestamp getEnd(Date date) {
      return null;
    }
  }*/;

  public static Calendar getCalendar(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal;
  }

  abstract Timestamp getEnd(Date date);

  abstract Timestamp getStart(Date date);
}
