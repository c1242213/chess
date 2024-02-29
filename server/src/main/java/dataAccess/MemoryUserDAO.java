package dataAccess;

import model.UserData;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO {
    public static ArrayList<UserData> userDataList = new ArrayList<>();
}
