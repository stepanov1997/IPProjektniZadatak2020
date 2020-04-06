package model.dao;

import model.beans.UserBean;
import model.dto.History;
import model.dto.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class UserDao {

    public List<UserBean> getUsers()
    {
        return Arrays.asList(
                new UserBean(new User("Kristijan", "Stepanov", "kristijan.stepanov", "kiki1997", true, true)),
                new UserBean(new User("Milan", "Medic", "milan.medic", "milan1234", false, false)));
    }

    public boolean giveAccess(User user) {
        return true;
    }

    public boolean blockUser(User user) {
        return true;
    }

    public String resetPassword(User user) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<8; i++)
        {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public Integer getNumberOfActiveUsers() {
        return (int)getUsers().stream().filter(e -> e.getUser().isLogged()).count();
    }

    public Integer getNumberOfRegistredUsers() {
        return (int)getUsers().stream().filter(e -> e.getUser().isRegistered()).count();
    }

    public History get24hoursHistory() {
        Map<LocalDateTime, Integer> hashMap = new HashMap<>();
        Random random = new Random();
        var dateTime = LocalDateTime.now();
        for(int i=0; i<24; i++)
        {
            hashMap.put(dateTime.minusHours(i), 1 + random.nextInt(100));
        }
        var result = hashMap.entrySet().stream().sorted((a, b) -> a.getKey().isBefore(b.getKey()) ? 1 : -1).collect(Collectors.toList());
        return new History(result, "Hour", "Logins");
    }
}
