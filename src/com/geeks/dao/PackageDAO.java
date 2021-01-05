/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.dao;

import com.geeks.beans.PackageBean;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author xyz
 */
public interface PackageDAO {
    public ResultSet viewAllPackageResultSet(PackageBean packageBean);
    public Integer addPackageDetails(PackageBean packageBean);
    public PackageBean checkPackage(PackageBean packageBean);
    public PackageBean setAllTxt(PackageBean packageBean);
    public Integer updatePackageDetails(PackageBean packageBean);
    public Integer deletePackageDetails(PackageBean packageBean);
    public List<String> getAllPackage();
}
