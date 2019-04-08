CREATE OR REPLACE FUNCTION rand_str(int) RETURNS text AS $str$ DECLARE str text;
BEGIN
  SELECT substring(replace(encode(md5(random()::text)::bytea, 'base64'),'=',''),6,$1) INTO str;
  RETURN str;
END;
$str$ LANGUAGE plpgsql;

INSERT INTO Duration (start_time,end_time)
SELECT registered+(5 ||' minutes')::interval,registered+(1 ||' years')::interval FROM UserProfile LIMIT 10;

INSERT INTO Championship (name,duration)
SELECT rand_str(12),id FROM Duration ORDER BY id DESC LIMIT 10;