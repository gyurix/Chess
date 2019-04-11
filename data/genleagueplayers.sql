INSERT INTO LeaguePlayer (player,league,elo)
SELECT generate_series,floor(random()*50+1),floor(random()*10000+1) FROM generate_series(1,150000);