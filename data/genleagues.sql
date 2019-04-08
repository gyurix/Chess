CREATE OR REPLACE FUNCTION rand_str(int) RETURNS text AS $str$ DECLARE str text;
BEGIN
  SELECT substring(replace(encode(md5(random()::text)::bytea, 'base64'),'=',''),6,$1) INTO str;
  RETURN str;
END;
$str$ LANGUAGE plpgsql;

DO $$
BEGIN
   FOR counter IN 1..5 LOOP
      INSERT INTO League (name,championship)
      SELECT ('L' || counter::text || 'C' || id::text) ,id FROM Championship;
   END LOOP;
END; $$
