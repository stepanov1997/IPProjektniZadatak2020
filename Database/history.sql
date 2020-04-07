select integers as hour, count(User_id) as times from 
(
	select integers, o.User_id as User_id from 
	(
		SELECT  @N := (24 + @N - 1)%24 AS integers FROM mysql.help_relation, 
		(SELECT @N:=hour(now())) dum LIMIT 24
	) a
	left outer join online_history o on 
	integers >= hour(o.loginDatetime) AND 
	integers <= hour(o.logoutDatetime) AND
	TIMESTAMPDIFF(hour, o.logoutDatetime, now()) < 24
	group by integers, o.User_id
) b
group by integers
order by integers desc
