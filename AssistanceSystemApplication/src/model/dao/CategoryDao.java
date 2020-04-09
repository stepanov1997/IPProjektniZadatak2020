package model.dao;

import model.dto.AssistanceCall;

import java.util.ArrayList;
import java.util.List;

public class CategoryDao {
    public List<AssistanceCall> getAll()
    {
        return new ArrayList<>();
    }

    public AssistanceCall get(int id)
    {
        return null;
    }

    public Integer add(AssistanceCall assistanceCall)
    {
        //TODO
        return 1;
    }

    public boolean delete(int id)
    {
        return false;
    }

    public boolean update(int id, AssistanceCall assistanceCall)
    {
        return false;
    }
}
