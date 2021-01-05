/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.connectivity;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author xyz
 */
public class Connectivity {
    private static Connection dbCon = null;

	public static Connection connect() {
		try {
			if (dbCon == null) {
				Class.forName("com.mysql.jdbc.Driver");
				dbCon = DriverManager.getConnection("jdbc:mysql://localhost:3306/palmvalley", "root","");
			}
			return dbCon;
		} catch (Exception x) {
			x.printStackTrace();
		}
		return null;
	}
}
