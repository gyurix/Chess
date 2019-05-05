package barnes.chess.db.entity;

import barnes.chess.db.DB;
import barnes.chess.utils.ErrorAcceptedConsumer;
import barnes.chess.utils.ErrorAcceptedRunnable;
import barnes.chess.utils.ThreadUtil;
import lombok.Getter;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEntity {
  private String fieldNames;
  private String fieldValuePlaceholders;
  private Field[] fields;
  @Getter
  protected int id;

  public AbstractEntity() {
    initFields();
  }

  public void delete(ErrorAcceptedConsumer<Boolean> resultHandler) {
    if (id == 0)
      throw new RuntimeException("This " + getTable() + " instance is not registered in the database");
    DB.getInstance().command(resultHandler, "DELETE FROM " + getTable() + " WHERE id = ?", id);
  }

  private Object[] getFieldValues() {
    int len = fields.length;
    Object[] out = new Object[len];
    for (int i = 0; i < len; ++i) {
      try {
        out[i] = fields[i].get(this);
      } catch (Throwable e) {
        e.printStackTrace();
      }
    }
    return out;
  }

  public String getTable() {
    return getClass().getSimpleName();
  }

  private void initFields() {
    StringBuilder valuePlaceholderOut = new StringBuilder();
    StringBuilder namesOut = new StringBuilder();
    List<Field> fieldList = new ArrayList<>();
    Class cl = getClass();
    while (cl != AbstractEntity.class) {
      for (Field f : cl.getDeclaredFields()) {
        f.setAccessible(true);
        if (f.getAnnotation(SkipField.class) == null) {
          valuePlaceholderOut.append(",?");
          namesOut.append(',').append(f.getName());
          fieldList.add(f);
        }
      }
      cl = cl.getSuperclass();
    }
    fields = new Field[fieldList.size()];
    fieldList.toArray(fields);
    fieldNames = namesOut.substring(1);
    fieldValuePlaceholders = valuePlaceholderOut.substring(1);
  }

  public void insert(ErrorAcceptedRunnable runnable) {
    DB.getInstance().query((rs) -> {
              rs.next();
              id = rs.getInt(1);
              System.out.println("Inserted " + getTable() + " #" + id);
              rs.close();
              if (runnable != null)
                ThreadUtil.ui(runnable);
            },
            "INSERT INTO " + getTable() + "  (" + fieldNames + ") " +
                    "VALUES (" + fieldValuePlaceholders + ") RETURNING id", getFieldValues());
  }

  public void load(ResultSet rs) {
    try {
      id = rs.getInt("id");
      for (Field f : fields) {
        Object o = rs.getObject(f.getName());
        if (o != null) {
          if (f.getType().isEnum())
            for (Object ec : f.getType().getEnumConstants()) {
              if (o.equals(ec.toString()))
                f.set(this, ec);
            }
          else
            f.set(this, o);
        }
      }
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  public void update(String key, Object value) {
    DB.getInstance().update("UPDATE " + getTable() + " SET " + key + " = ? WHERE id = ?", value, id);
  }
}
