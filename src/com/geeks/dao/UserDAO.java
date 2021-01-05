/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.dao;

import com.geeks.beans.UserBean;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author xyz
 */
public interface UserDAO {
    public Boolean login(UserBean userBean);
    public ResultSet viewAllUserResultSet();
    public Integer addUserDetails(UserBean userBean);
    public Boolean checkUser(UserBean userBean);
    public List<UserBean> setAllTxt(UserBean userBean);
    public Integer updateUserDetails(UserBean userBean);
    public Integer deleteUserDetails(UserBean userBean);
}
