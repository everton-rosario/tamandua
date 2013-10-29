package br.com.tamandua.indexer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * JDBCIndexer
 * Indexador via conteudo JDBC
 */
public class JDBCIndexer {
	/** Conexao padrao */
	protected Connection con;

    /** Configuracao do DB */
	public static String dburl;        
	public static String driver;        
	public static String user;        
	public static String password;
	
	private static BasicDataSource ds;
	
	/**
	 * Faz a conexao com o BD
	 * 
	 * @return a conexao
	 * @throws SQLException - se algum erro ocorrer na conexao
	 */
	public static Connection getConnection() throws Exception {
		Connection con = null;
		DataSource ds = setupDataSource();
		
		try {
			con = ds.getConnection();
		} catch(SQLException sqle) {
			throw sqle;
		}

		return con;
	}
	
    private synchronized static DataSource setupDataSource() throws Exception {
    	if (ds == null) {
    		ds = new BasicDataSource();
    		ds.setDriverClassName(driver);
    		ds.setUsername(user);
    		ds.setPassword(password);
    		ds.setUrl(dburl);
    		ds.setMaxActive(30);
    	}
        
        return ds;
    }
    
    public static void initPropsDB (IndexerProperties propDB){
    	driver = propDB.getProperty(IndexerProperties.Property.INDEXER_DB_DRIVER);
		dburl = propDB.getProperty(IndexerProperties.Property.INDEXER_DB_URL);
		user = propDB.getProperty(IndexerProperties.Property.INDEXER_DB_USER);
		password = propDB.getProperty(IndexerProperties.Property.INDEXER_DB_PASSWD);
    }
    
    public static void close(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {
        close(resultSet);
        close(preparedStatement);
        close(connection);
    }

    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException s) {
                // Log warning here.
            }
        }
    }

    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException s) {
                // Log warning here.
            }
        }
    }

    public static void close(PreparedStatement preparedStatement) {
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException s) {
				// Log warning here.
			}
		}
	}
}
