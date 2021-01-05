/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.dao;

import com.geeks.beans.CategoryBean;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author xyz
 */
public interface CategoryDAO {
    public ResultSet viewAllCategoryResultSet();
    public Integer addCategoryDetails(CategoryBean categoryBean);
    public Boolean checkCategory(CategoryBean categoryBean);
    public List<CategoryBean> setAllTxt(CategoryBean categoryBean);
    public Integer updateCategoryDetails(CategoryBean categoryBean);
    public Integer deleteCategoryDetails(CategoryBean categoryBean);
}
