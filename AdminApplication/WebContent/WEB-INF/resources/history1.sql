SELECT integers as hour, 
       Count(DISTINCT o.user_id) AS times
FROM   (SELECT @n := @n + 1 AS integers 
        FROM   mysql.help_relation, 
               (SELECT @n := -1) dum 
        LIMIT  24) a 
       LEFT OUTER JOIN online_history o 
                    ON ( logindatetime <= IF(Hour(Now()) >= integers, 
                                          Adddate( 
                                                            Cast(Cast(Now() AS 
                                                            date) AS 
                                                            datetime), 
                                          INTERVAL integers hour), 
                                          Subdate( 
                                          Cast(Cast(Now() AS date) 
                                          AS 
                                          datetime), INTERVAL 
                                          24 - integers hour)) 
                         AND IFNULL(logoutdatetime, logindatetime) >= IF(Hour(Now()) >= integers, 
                                                   Adddate(Cast(Cast(Now() AS 
                                                           date) AS 
                                                           datetime), 
                                                                     INTERVAL 
                                                   integers 
                                                                     hour), 
                                                                     Subdate( 
                                               Cast(Cast(Now() AS date) AS 
                                               datetime), 
                                               INTERVAL 24 - integers hour)) ) 
                        OR ( logindatetime >= IF(Hour(Now()) >= integers, 
                                                    Adddate(Cast(Cast(Now() AS 
                                                            date) AS 
                                                            datetime), 
                                                                     INTERVAL 
                                                    integers 
                                                                     hour), 
                                              Subdate( 
                                              Cast(Cast(Now() AS date) AS 
                                              datetime), INTERVAL 
                                                                     24 
                                                         - integers hour 
                                              ) 
                                              ) 
                             AND ( logindatetime <= Adddate( 
                                   IF(Hour(Now()) >= 
                                      integers, 
                                                    Adddate( 
                                                      Cast(Cast(Now( 
                                                      ) AS 
                                                      date) AS datetime 
                                                                       ), 
                                                    INTERVAL integers hour) 
                                                    , 
                                                            Subdate( 
                                                                Cast(Cast(Now() 
                                                                AS date 
                                                                ) 
                                                                AS datetime), 
                                                    INTERVAL 24 - integers hour) 
                                   ) 
                                                    , 
                                                             INTERVAL 1 hour) 
                                    OR IFNULL(logoutdatetime, logindatetime) <= Adddate( 
                                       IF(Hour(Now()) >= 
                                          integers, 
                                                      Adddate( 
                                           Cast(Cast(Now() AS date) AS 
                                           datetime), 
                                       INTERVAL integers hour), 
                                           Subdate( 
                                           Cast(Cast(Now() AS date) AS 
                                           datetime), 
                                           INTERVAL 24 - integers hour)), 
                                                     INTERVAL 1 hour) ) ) 
                           AND Timestampdiff(hour, IFNULL(logoutdatetime, logindatetime), Now()) < 24 
GROUP  BY integers; 