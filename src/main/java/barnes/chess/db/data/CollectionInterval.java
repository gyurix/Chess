package barnes.chess.db.data;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;

public enum CollectionInterval {
  DAILY {
    @Override
    public Timestamp getStart(Date date) {
      Calendar cal = CollectionInterval.getCalendar(date);
      cal.set(cal.get(YEAR), cal.get(MONTH), cal.get(DAY_OF_MONTH), 0, 0, 0);
      return new Timestamp(cal.getTimeInMillis());
    }

    @Override
    public Timestamp getEnd(Date date) {
      Calendar cal = CollectionInterval.getCalendar(date);
      cal.set(cal.get(YEAR), cal.get(MONTH), cal.get(DAY_OF_MONTH) + 1, 0, 0, 0);
      return new Timestamp(cal.getTimeInMillis());
    }
  }, WEEKLY {
    @Override
    public Timestamp getStart(Date date) {
      Calendar cal = CollectionInterval.getCalendar(date);
      int dayOfWeek = (cal.get(DAY_OF_WEEK) + 5) % 7;
      cal.add(DAY_OF_MONTH, -dayOfWeek);
      cal.set(cal.get(YEAR), cal.get(MONTH), cal.get(DAY_OF_MONTH), 0, 0, 0);
      return new Timestamp(cal.getTimeInMillis());
    }

    @Override
    public Timestamp getEnd(Date date) {
      Calendar cal = CollectionInterval.getCalendar(date);
      int dayOfWeek = (cal.get(DAY_OF_WEEK) + 5) % 7;
      cal.add(DAY_OF_MONTH, -dayOfWeek);
      cal.set(cal.get(YEAR), cal.get(MONTH), cal.get(DAY_OF_MONTH) + 7, 0, 0, 0);
      return new Timestamp(cal.getTimeInMillis());
    }
  }, MONTHLY {
    @Override
    public Timestamp getStart(Date date) {
      Calendar cal = CollectionInterval.getCalendar(date);
      cal.set(cal.get(YEAR), cal.get(MONTH), 0, 0, 0, 0);
      return new Timestamp(cal.getTimeInMillis());
    }

    @Override
    public Timestamp getEnd(Date date) {
      Calendar cal = CollectionInterval.getCalendar(date);
      //noinspection MagicConstant
      cal.set(cal.get(YEAR), cal.get(MONTH) + 1, 0, 0, 0, 0);
      return new Timestamp(cal.getTimeInMillis());
    }
  }, OVERALL {
    @Override
    public Timestamp getStart(Date date) {
      return new Timestamp(0L);
    }

    @Override
    public Timestamp getEnd(Date date) {
      return new Timestamp(System.currentTimeMillis() + 86400000);
    }
  };

  public static Calendar getCalendar(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal;
  }

  public abstract Timestamp getEnd(Date date);

  public abstract Timestamp getStart(Date date);
}
