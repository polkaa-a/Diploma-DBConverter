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
import org.bson.BsonDocument;
import org.bson.BsonType;
import org.bson.Document;

import java.util.*;

import static data.mongodb.ConstantsProvider.DOC_ID_COLUMN_NAME;

//DTOMongoDBConverter converts MongoDB to MongoDBDatabaseDTO
public class MongoDBToDTOConverter implements DBToDTOConverter<MongoDB, MongoDBDatabaseDTO> {

    private Set<MongoDBCollectionDTO> collectionsDTO;

    @Override
    public MongoDBDatabaseDTO convert(MongoDB mongoDB) throws ConversionException {
        collectionsDTO = new HashSet<>();
        //make from each collection MongoDBCollectionDTO
        mongoDB.getAllCollections().forEach(this::addCollection);
        return new MongoDBDatabaseDTO(mongoDB.getName(), collectionsDTO);
    }

    private void addCollection(MongoCollection<Document> collection) {
        var collectionName = collection.getNamespace().getCollectionName();
        var documents = getDocumentsDTOsFromCollection(collection);
        var docCollectionDTO = new MongoDBCollectionDTO(collectionName, documents);
        collectionsDTO.add(docCollectionDTO);
    }

    private List<MongoDBDocumentDTO> getDocumentsDTOsFromCollection(MongoCollection<Document> collection) {
        var documentsDTOs = new ArrayList<MongoDBDocumentDTO>();
        for (var document : collection.find()) {
            var documentDTO = getDTOFromDocument(document);
            documentsDTOs.add(documentDTO);
        }
        return documentsDTOs;
    }

    // TODO: Убрать рекурсию
    private MongoDBDocumentDTO getDTOFromDocument(Document document) {
        var documentDTO = new MongoDBDocumentDTO(document);
        var subDocuments = new HashMap<String, MongoDBDocumentDTO>();
        for (var fieldName : document.keySet()) {
            var registry = MongoClient.getDefaultCodecRegistry();
            var bsonDocument = document.toBsonDocument(BsonDocument.class, registry);
            var bsonType = bsonDocument.get(fieldName).getBsonType();
            var isId = fieldName.equals(DOC_ID_COLUMN_NAME);
            documentDTO.addField(new MongoDBFieldDTO(fieldName, bsonType, isId, documentDTO));
            if (bsonType.equals(BsonType.DOCUMENT)) {
                var subDocument = getDTOFromDocument((Document) document.get(fieldName));
                subDocument.setMongoDBCollectionDTO(documentDTO.getMongoDBCollectionDTO());
                subDocuments.put(fieldName, subDocument);
            }
        }
        documentDTO.getSubDocuments().putAll(subDocuments);
        return documentDTO;
    }
}
