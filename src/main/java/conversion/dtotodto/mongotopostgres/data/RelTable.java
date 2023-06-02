package conversion.dtotodto.mongotopostgres.data;

import dto.mongodb.MongoDBDocumentDTO;
import dto.postgresql.PostgreSQLForeignKeyDTO;

public record RelTable(MongoDBDocumentDTO documentDTO, String tableName, PostgreSQLForeignKeyDTO fk, String fieldName) {
}
