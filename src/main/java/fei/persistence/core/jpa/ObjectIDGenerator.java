package fei.persistence.core.jpa;


import java.io.Serializable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.MappingException;
import org.hibernate.jdbc.util.FormatStyle;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.mapping.Table;
import org.hibernate.type.Type;
import org.hibernate.util.PropertiesHelper;

import org.hibernate.id.Configurable;
import org.hibernate.id.MultipleHiLoPerTableGenerator;
import org.hibernate.id.PersistentIdentifierGenerator;


public class ObjectIDGenerator extends MultipleHiLoPerTableGenerator 
	implements PersistentIdentifierGenerator, Configurable {
	
	private static final Logger log = LoggerFactory.getLogger(MultipleHiLoPerTableGenerator.class);
	
	public static final String ID_TABLE = "table";
	public static final String PK_COLUMN_NAME = "primary_key_column";
	public static final String PK_VALUE_NAME = "primary_key_value";
	public static final String PKID_COLUMN_NAME = "primary_key_id_column";
	public static final String VALUE_COLUMN_NAME = "value_column";
	public static final String PK_LENGTH_NAME = "primary_key_length";

	private static final int DEFAULT_PK_LENGTH = 255;
	public static final String DEFAULT_TABLE = "hibernate_sequences";
	private static final String DEFAULT_PK_COLUMN = "sequence_name";
	private static final String DEFAULT_VALUE_COLUMN = "sequence_next_hi_value";
	private static final String DEFAULT_PKID_COLUMN = "sequence_id_hi_value";
	
	private String tableName;
	private String entityName;
	private String entityId;
	private String entityNextIdToUse;
	private String query;
	private String count;
	private String insert;
	private String update;

	//hilo params
	public static final String MAX_LO = "max_lo";

	private long hi;
	private int lo;
	private int maxLo;
	private Class returnClass;
	private int keySize;
	private String _entityName;

	public String[] sqlCreateStrings(Dialect dialect) throws HibernateException {
		return new String[] {
			new StringBuffer( dialect.getCreateTableString() )
					.append( ' ' )
					.append( tableName )
					.append( " ( " )
					.append( entityName )
					.append( ' ' )
					.append( dialect.getTypeName( Types.VARCHAR, keySize, 0, 0 ) )
					.append( ",  " )
					.append( entityNextIdToUse )
					.append( ' ' )
					.append( dialect.getTypeName( Types.INTEGER ) )
					.append( ",  " )
					.append( entityId )
					.append( ' ' )
					.append( dialect.getTypeName( Types.INTEGER ) )
					.append( " ) " )
					.toString()
		};
	}

	public String[] sqlDropStrings(Dialect dialect) throws HibernateException {
		StringBuffer sqlDropString = new StringBuffer( "drop table " );
		if ( dialect.supportsIfExistsBeforeTableName() ) {
			sqlDropString.append( "if exists " );
		}
		sqlDropString.append( tableName ).append( dialect.getCascadeConstraintsString() );
		if ( dialect.supportsIfExistsAfterTableName() ) {
			sqlDropString.append( " if exists" );
		}
		return new String[] { sqlDropString.toString() };
	}

	public Object generatorKey() {
		return tableName;
	}

	public Serializable doWorkInCurrentTransaction(Connection conn, String sql) throws SQLException {
		int result;
		int nextTypeId;
		int rows;
		do {
			// The loop ensures atomicity of the
			// select + update even for no transaction
			// or read committed isolation level

			//sql = query;
			SQL_STATEMENT_LOGGER.logStatement( sql, FormatStyle.BASIC );
			PreparedStatement qps = conn.prepareStatement(query);
			qps.setString(1, _entityName);
			
			PreparedStatement cps = conn.prepareStatement(count);
			PreparedStatement ips = null;
			try {
				//qps.setString(1, key);
				ResultSet rsq = qps.executeQuery();
				ResultSet rsc = cps.executeQuery();
				rsc.next();
				nextTypeId = rsc.getInt(1);
				
				boolean isInitialized = rsq.next();
				if ( !isInitialized ) { // entity does not exist in the table
					result = 0;
					ips = conn.prepareStatement(insert);
					ips.setString(1, _entityName);
					ips.setInt(2, nextTypeId);
					ips.setInt(3, result);
					ips.execute();
				} else { // entity exists in the table
					nextTypeId = rsq.getInt(1);
					result = rsq.getInt(2);
				}
				rsq.close();
			}
			catch (SQLException sqle) {
				log.error("could not read or init a hi value", sqle);
				throw sqle;
			}
			finally {
				if (ips != null) {
					ips.close();
				}
				qps.close();
				cps.close();
			}

			//sql = update;
			PreparedStatement ups = conn.prepareStatement(update);
			try {
				ups.setInt( 1, result + 1 );
				ups.setInt( 2, result );
				ups.setString( 3, _entityName );
				rows = ups.executeUpdate();
			}
			catch (SQLException sqle) {
				log.error("could not update hi value in: " + tableName, sqle);
				throw sqle;
			}
			finally {
				ups.close();
			}
		}
		while (rows==0);
			return new ObjectID((new Integer(result)).toString(), (new Integer(nextTypeId)).toString());
	}

	public synchronized Serializable generate(SessionImplementor session, Object obj)
		throws HibernateException {
		
		_entityName = obj.getClass().getName();
		return doWorkInNewTransaction(session);
	}

	public void configure(Type type, Properties params, Dialect dialect) throws MappingException {
		tableName = PropertiesHelper.getString(ID_TABLE, params, DEFAULT_TABLE);
		entityName = PropertiesHelper.getString(PK_COLUMN_NAME, params, DEFAULT_PK_COLUMN);
		entityId = PropertiesHelper.getString(PKID_COLUMN_NAME, params, DEFAULT_PKID_COLUMN);
		entityNextIdToUse = PropertiesHelper.getString(VALUE_COLUMN_NAME, params, DEFAULT_VALUE_COLUMN);
		String schemaName = params.getProperty(SCHEMA);
		String catalogName = params.getProperty(CATALOG);
		keySize = PropertiesHelper.getInt(PK_LENGTH_NAME, params, DEFAULT_PK_LENGTH);
		String keyValue = PropertiesHelper.getString(PK_VALUE_NAME, params, params.getProperty(TABLE) );

		if ( tableName.indexOf( '.' )<0 ) {
			tableName = Table.qualify( catalogName, schemaName, tableName );
		}

		query = "select " +
			entityId + ", " + entityNextIdToUse + 
			" from " +
			dialect.appendLockHint(LockMode.UPGRADE, tableName) +
			" where " + entityName + " = ?" +
			dialect.getForUpdateString();

		count = "select count(*)" +
			" from " +
			dialect.appendLockHint(LockMode.UPGRADE, tableName);
		
		update = "update " +
			tableName +
			" set " +
			entityNextIdToUse +
			" = ? where " +
			entityNextIdToUse +
			" = ? and " +
			entityName +
			" = ?";
		
		insert = "insert into " + tableName +
			"(" + entityName + ", " +	entityId + ", " +	entityNextIdToUse + ") " +
			"values(?, ?, ?)";


		//hilo config
		maxLo = PropertiesHelper.getInt(MAX_LO, params, Short.MAX_VALUE);
		lo = maxLo + 1; // so we "clock over" on the first invocation
		returnClass = type.getReturnedClass();
	}
}
