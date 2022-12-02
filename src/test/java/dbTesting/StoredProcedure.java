package dbTesting;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import utils.CompareResultSets;

/*
 
 Syntax for procedures:
 
  {call procedure_name()}                ===> no input and return no output
  {call procedure_nam(?,?)}              ===> accepts input and return no output
  {? = call procedure_nam()}             ===> accepts no input and return output
  {? = call procedure_nam(?)}            ===> accepts input and return output
 
 */

public class StoredProcedure {

	public Connection connection;
	public Statement statement;
	public ResultSet resultSet;

	@BeforeClass
	public void createConnection() throws SQLException {
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels", "root", "210795");
		statement = connection.createStatement();
	}

	@AfterClass
	void tearDown() throws SQLException {
		connection.close();
	}

	@Test(priority = 1)
	void test_storedProceduredExists() throws SQLException {
		// to check particular procedure is there (here it is selectallcustomers or
		// SelectAllCustomers (case insensitive)
		resultSet = statement.executeQuery("show procedure status where name='SelectAllCustomers';");
		resultSet.next();
		Assert.assertEquals(resultSet.getString("name"), "SelectAllCustomers");
	}

	@Test(priority = 2)
	void selectAllCustomers() throws SQLException {
		// steps
		CallableStatement callabeStatement = connection.prepareCall("{call SelectAllCustomers()}");
		resultSet = callabeStatement.executeQuery();

		// Test query
		ResultSet resultSet2 = statement.executeQuery("select * from customers;");
		Assert.assertTrue(CompareResultSets.compareResultSets(resultSet, resultSet2));
	}

	@Test(priority = 3)
	void selectAllCustomersByCity() throws SQLException {
		// steps
		CallableStatement callabeStatement = connection.prepareCall("{call SelectAllCustomersByCity(?)}");
		// give the inputParam
		callabeStatement.setString(1, "Singapore");
		resultSet = callabeStatement.executeQuery();

		// Test query
		ResultSet resultSet2 = statement.executeQuery("select * from customers where city = 'Singapore';");
		Assert.assertTrue(CompareResultSets.compareResultSets(resultSet, resultSet2));
	}

	@Test(priority = 4)
	void selectAllCustomersByCityAndPin() throws SQLException {
		// steps
		CallableStatement callabeStatement = connection.prepareCall("{call SelectAllCustomersByCityAndPin(?,?)}");
		// give the inputParam
		callabeStatement.setString(1, "Singapore");
		callabeStatement.setString(2, "079903");
		resultSet = callabeStatement.executeQuery();

		// Test query
		ResultSet resultSet2 = statement
				.executeQuery("select * from customers where city = 'Singapore' and postalCode='079903';");
		// Compare 2 resultsets
		Assert.assertTrue(CompareResultSets.compareResultSets(resultSet, resultSet2));
	}
}
