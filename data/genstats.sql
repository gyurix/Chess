CREATE OR REPLACE FUNCTION rand_date() RETURNS timestamp AS $rv$ DECLARE rv timestamp;
BEGIN
  select timestamp '2003-01-01 00:00:00' +
       random() * (timestamp '2018-12-31 23:00:00' -
                   timestamp '2003-01-01 00:00:00') INTO rv;
  RETURN rv;
END;
$rv$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION rand_str(int) RETURNS text AS $str$ DECLARE str text;
BEGIN
  SELECT substring(replace(encode(md5(random()::text)::bytea, 'base64'),'=',''),6,$1) INTO str;
  RETURN str;
END;
$str$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION rand_res() RETURNS text AS $str$ DECLARE str text;
BEGIN
  SELECT RAND(WinnerType) AS str;
  RETURN str;
END;
$str$ LANGUAGE plpqsql;

INSERT INTO Game (player1,player2,winner,duration)
SELECT rand_str(6),rand_str(6),rand_res(),rand_date()
FROM generate_series(1,5)