-- Data generation helper functions
CREATE OR REPLACE FUNCTION rand_date() RETURNS timestamp AS $rv$ DECLARE rv timestamp;
BEGIN
  select timestamp '2019-03-01 00:00:00' +
       random() * (timestamp '2019-04-30 23:00:00' -
                   timestamp '2019-03-01 00:00:00') INTO rv;
  RETURN rv;
END;
$rv$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION rand_str(int) RETURNS text AS $str$ DECLARE str text;
BEGIN
  SELECT substring(replace(encode(md5(random()::text)::bytea, 'base64'),'=',''),6,$1) INTO str;
  RETURN str;
END;
$str$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION rand_wt() RETURNS WinnerType AS $wt$ DECLARE wt WinnerType;
BEGIN
  SELECT unnest(enum_range(NULL::WinnerType)) ORDER BY random() LIMIT 1 INTO wt;
  RETURN wt;
END;
$wt$ LANGUAGE plpgsql;

-- Generate ranks
INSERT INTO Rank(name) VALUES ('user'),('mod'),('admin');

-- Generate users with 'asd' password
INSERT INTO UserProfile (nick,first_name,last_name,pwd_hash,rank,registered)
SELECT (array['Smith','Anderson','Clark','Wright','Mitchell','Johnson','Thomas','Rodriguez','Lopez','Perez','Williams',
'Jackson','Lewis','Hill','Roberts','Jones','White','Lee','Scott','Turner','Brown','Harris','Walker','Green','Phillips',
'Davis','Martin','Hall','Adams','Campbell','Miller','Thompson','Allen','Baker','Parker','Wilson','Garcia','Young',
'Gonzalez','Evans','Moore','Martinez','Hernandez','Nelson','Edwards','Taylor','Robinson','King','Carter','Collins'])[floor(random() * 50 + 1)],
(array['James','David','Christopher','George','Ronald','John','Richard','Daniel','Kenneth','Anthony','Robert','Charles',
'Paul','Steven','Kevin','Michael','Joseph','Mark','Edward','Jason','William','Thomas','Donald','Brian','Jeff','Mary',
'Jennifer','Lisa','Sandra','Michelle','Patricia','Maria','Nancy','Donna','Laura','Linda','Susan','Karen','Carol','Sarah',
'Barbara','Margaret','Betty','Ruth','Kimberly','Elizabeth','Dorothy','Helen','Sharon','Deborah'])[floor(random() * 50 + 1)],
(array['BadMr.Frosty','Kraken','Boomer','Lumberjack','Boomerang','Mammoth','Boss','Mastadon','Budweiser','Master','Bullseye',
'Meatball','Buster','Mooch','Butch','Mr.President','Buzz','Outlaw','Canine','Ratman','CaptianRedBeard','Renegade','Champ',
'Sabertooth','Coma','Scratch','Crusher','Sentinel','Diesel','Speed','Doctor','Spike','Dreads','Subwoofer','Frankenstein',
'Thunderbird','Froggy','Tornado','General','Troubleshoot','Godzilla','Vice','Hammerhead','Viper','HandyMan','Wasp',
'HoundDog','Wizard','Indominus','Zodiac','KingKong'])[floor(random() * 50 + 1)],
'688787D8FF144C502C7F5CFFAAFE2CC588D86079F9DE88304C26B0CB99CE91C6',1,rand_date()
FROM generate_series(1,25000);

-- Generate championships
INSERT INTO Duration (start_time,end_time)
SELECT registered+(5 ||' minutes')::interval,registered+(1 ||' years')::interval FROM UserProfile LIMIT 10;

INSERT INTO Championship (name,duration)
SELECT rand_str(12),id FROM Duration ORDER BY id DESC LIMIT 10;

-- Generate leagues
DO $$
BEGIN
   FOR counter IN 1..5 LOOP
      INSERT INTO League (name,championship)
      SELECT ('L' || counter::text || 'C' || id::text) ,id FROM Championship;
   END LOOP;
END; $$;

-- Generate league players
INSERT INTO LeaguePlayer (player,league,elo)
SELECT generate_series,floor(random()*50+1),floor(random()*1000+1) FROM generate_series(1,15000);


-- Generate Games
INSERT INTO Duration (start_time,end_time)
SELECT registered+(5 ||' minutes')::interval,registered+(45 ||' minutes')::interval FROM UserProfile;

DO $$
BEGIN
   FOR counter IN 1..15 LOOP
      INSERT INTO Game (player1,player2,winner,duration)
      SELECT id,floor(id+random()*1000),rand_wt(),10+id FROM LeaguePlayer LIMIT 10000;
   END LOOP;
END; $$;

-- Generate Friends
INSERT INTO Duration (start_time,end_time)
SELECT registered+(5 ||' minutes')::interval,registered+(45 ||' minutes')::interval FROM UserProfile;
DO $$
BEGIN
   FOR counter IN 1..15 LOOP
      INSERT INTO Friendship (user1,user2,duration)
      SELECT id,floor(id+random()*1000),25010+id FROM UserProfile LIMIT 14000;
   END LOOP;
END; $$;

-- Generate Permissions
INSERT INTO Permission (name,rank) VALUES ('changerank',3),('viewprofile',3),('editprofile',3),('viewgames',3),('editgames',3);
INSERT INTO Permission (name,rank) VALUES ('viewprofile',2),('editprofile',2),('viewgames',2),('editgames',2);
INSERT INTO Permission (name,rank) VALUES ('viewprofile',1),('viewgames',1);