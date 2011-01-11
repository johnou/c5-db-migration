package com.carbonfive.db.jdbc.schema;

import com.carbonfive.db.DatabaseTestUtils;
import org.junit.Ignore;
import org.junit.Test;

import javax.sql.DataSource;
import java.net.InetAddress;

import static java.lang.String.format;
import static java.lang.System.getProperty;
import static junit.framework.Assert.fail;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assume.assumeThat;

public class DropDatabaseTest
{
    @Ignore
    @Test
    public void dropMysqlDatabase() throws Exception
    {
        dropDatabase(format("jdbc:mysql://%s/drop_database_test", getProperty("jdbc.host", "localhost")));
    }

    @Ignore
    @Test
    public void dropPostgresqlDatabase() throws Exception
    {
        dropDatabase(format("jdbc:postgresql://%s/drop_database_test", getProperty("jdbc.host", "localhost")));
    }

    @Test
    public void dropSqlServer2000Database() throws Exception
    {
        assumeThat(InetAddress.getLocalHost().getHostAddress(), startsWith("10.4.5"));

        dropDatabase("jdbc:jtds:sqlserver://sqlserver2000/drop_database_test");
    }

    @Test
    public void dropSqlServer2005Database() throws Exception
    {
        assumeThat(InetAddress.getLocalHost().getHostAddress(), startsWith("10.4.5"));

        dropDatabase("jdbc:jtds:sqlserver://sqlserver2005/drop_database_test");
    }

    void dropDatabase(String url) throws Exception
    {
        final String username = "dev";
        final String password = "dev";

        new CreateDatabase(url, username, password).execute();
        new DropDatabase(url, username, password).execute();

        try
        {
            DataSource dataSource = DatabaseTestUtils.createDataSource(url, username, password);
            dataSource.getConnection().close(); // Throws an exception if database is not found.
            fail("Exception should have been thrown indicating that the database does not exist.");
        }
        catch (Exception ignored)
        {
        }
    }
}