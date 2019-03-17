-- Script for generating 1 500 000 fake users having
-- - random 6 character long usernames
-- - random 8 character long first name and last name
-- - random password asd for easy testability
-- - registration date between 2003 and 2018

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

-- For easy testability all the users will have password 'asd' by default
INSERT INTO UserProfile (nick,first_name,last_name,pwd_hash,rank,registered)
SELECT rand_str(6),rand_str(8),rand_str(8),'688787D8FF144C502C7F5CFFAAFE2CC588D86079F9DE88304C26B0CB99CE91C6',1,rand_date()
FROM generate_series(1,1500000)