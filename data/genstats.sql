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

INSERT INTO Duration (start_time,end_time)
SELECT registered+(5 ||' minutes')::interval,registered+(45 ||' minutes')::interval FROM UserProfile;

CREATE OR REPLACE FUNCTION rand_wt() RETURNS WinnerType AS $wt$ DECLARE wt WinnerType;
BEGIN
  SELECT unnest(enum_range(NULL::WinnerType)) ORDER BY random() LIMIT 1 INTO wt;
  RETURN wt;
END;
$wt$ LANGUAGE plpgsql;

INSERT INTO Game (player1,player2,winner,duration)
SELECT id,id+floor(random(1000)),rand_wt(),id FROM LeaguePlayer LIMIT 149000