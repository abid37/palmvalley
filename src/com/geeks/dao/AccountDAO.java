/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.dao;

import com.geeks.beans.AccountBean;
import java.sql.ResultSet;
import java.util.List;

public interface AccountDAO {
    public ResultSet viewAllAccountResultSet();
    public Integer addAccountDetails(AccountBean accountBean);
    public Boolean checkAccount(AccountBean accountBean);
    public List<AccountBean> setAllTxt(AccountBean accountBean);
    public Integer updateAccountDetails(AccountBean accountBean);
    public Integer deleteAccountDetails(AccountBean accountBean);
    public Integer getIdByAccount(AccountBean accountBean);
    public List<String> getAllAccountNames();
}
