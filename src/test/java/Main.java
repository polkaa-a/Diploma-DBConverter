import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import conversion.databasetodto.document.MongoDBToDTOConverter;
import conversion.dtotodatabase.relational.DTOToPostgreSQLConverter;
import conversion.dtotodto.mongotopostgres.CollectionToTableConverter;
import conversion.manager.DBConverterManager;
import conversion.manager.MongoDBToPostgreSQLDBConverterManager;
import database.MongoDB;
import database.PostgreSQL;
import dto.mongodb.MongoDBDatabaseDTO;
import dto.postgresql.PostgreSQLDatabaseDTO;
import exceptions.ConversionException;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws SQLException {
        Properties info = new Properties();
        info.setProperty("user", "postgres");
        info.setProperty("password", "12345");
        info.setProperty("useUnicode", "true");
        info.setProperty("characterEncoding", "utf8");
        var connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/" + "admin", info);

        MongoClient mongoClient = MongoClients.create("mongodb://admin:admin@localhost:27017");

        MongoDB mongoDB = new MongoDB("admin", Set.of("Accounts", "Dogs"), mongoClient);
        PostgreSQL postgreSQL = new PostgreSQL("admin", null, connection);

        MongoDBToPostgreSQLDBConverterManager manager = new MongoDBToPostgreSQLDBConverterManager(new CollectionToTableConverter());
        try {
            manager.convert(mongoDB, postgreSQL);
        } catch (ConversionException e) {
            System.out.println(e.getMessage());
        }
    }
}
