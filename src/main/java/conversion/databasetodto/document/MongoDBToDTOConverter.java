package conversion.databasetodto.document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import conversion.databasetodto.DBToDTOConverter;
import database.MongoDB;
import dto.mongodb.MongoDBCollectionDTO;
import dto.mongodb.MongoDBDatabaseDTO;
import dto.mongodb.MongoDBDocumentDTO;
import dto.mongodb.MongoDBFieldDTO;
import exceptions.ConversionException;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.bson.BsonType;
import org.bson.Document;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static data.mongodb.ConstantsProvider.DOC_ID_COLUMN_NAME;

@Slf4j
//DTOMongoDBConverter converts MongoDB to MongoDBDatabaseDTO
public class MongoDBToDTOConverter implements DBToDTOConverter<MongoDB, MongoDBDatabaseDTO> {

    protected Set<MongoDBCollectionDTO> collectionsDTO;

    @Override
    public MongoDBDatabaseDTO convert(MongoDB mongoDB) throws ConversionException {
        collectionsDTO = new HashSet<>();
        //make from each collection MongoDBCollectionDTO
        mongoDB.getAllCollections().forEach(this::addCollection);
        log.info("MongoDB is converted into MongoDBDatabaseDTO, database name: " + mongoDB.getName());
        return new MongoDBDatabaseDTO(mongoDB.getName(), collectionsDTO);
    }

    protected void addCollection(MongoCollection<Document> collection) {
        var collectionName = collection.getNamespace().getCollectionName();
        var mongoCollectionDTO = new MongoDBCollectionDTO(collectionName);
        convertDocumentsToDTOs(collection, mongoCollectionDTO);
        collectionsDTO.add(mongoCollectionDTO);
        log.info("Collection is converted into MongoDBCollectionDTO, collection name: " + collectionName);
    }

    protected void convertDocumentsToDTOs(MongoCollection<Document> collection, MongoDBCollectionDTO collectionDTO) {
        for (var document : collection.find()) {
            var documentDTO = getDTOFromDocument(document, collectionDTO);
            var id = document.get(DOC_ID_COLUMN_NAME);
            collectionDTO.addDocument(documentDTO);
            log.info("Document is converted into MongoDBDocumentDTO, document's _id: " + id);
        }
    }

    protected MongoDBDocumentDTO getDTOFromDocument(Document document, MongoDBCollectionDTO collectionDTO) {
        var documentDTO = new MongoDBDocumentDTO(document, collectionDTO);
        var subDocuments = new HashMap<String, MongoDBDocumentDTO>();
        for (var fieldName : document.keySet()) {
            var registry = MongoClient.getDefaultCodecRegistry();
            var bsonDocument = document.toBsonDocument(BsonDocument.class, registry);
            var bsonType = bsonDocument.get(fieldName).getBsonType();
            var isId = fieldName.equals(DOC_ID_COLUMN_NAME);
            new MongoDBFieldDTO(fieldName, bsonType, isId, documentDTO);
            if (bsonType.equals(BsonType.DOCUMENT)) {
                var subDocument = getDTOFromDocument((Document) document.get(fieldName), collectionDTO);
                var id = document.get(DOC_ID_COLUMN_NAME);
                subDocuments.put(fieldName, subDocument);
                log.info("Subdocument's DTO is created, fieldName: " + fieldName + ", document's _id: " + id);
            }
        }
        documentDTO.getSubDocuments().putAll(subDocuments);
        return documentDTO;
    }
}
