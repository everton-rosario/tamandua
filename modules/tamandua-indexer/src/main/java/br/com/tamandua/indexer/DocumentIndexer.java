/**
 * 
 */
package br.com.tamandua.indexer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;

import br.com.tamandua.indexer.IndexerProperties.Property;

/**
 * @author Everton Rosario(erosario@gmail.com)
 */
public class DocumentIndexer implements Runnable {
    private Document document;
    private IndexWriter writer;
    private String[] params;
    private String query;
    private int start;
    private int end;
    
    /**
     * @param document
     * @param writer
     * @param resultSet
     */
    public DocumentIndexer(String query, int start, int end, Document document, IndexWriter writer, String ...params) {
        super();
        this.query = query;
        this.document = document;
        this.writer = writer;
        this.params = params;
        this.start = start;
        this.end = end;
    }

    /**
     * @return the document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * @param document the document to set
     */
    public void setDocument(Document document) {
        this.document = document;
    }

    /**
     * @return the writer
     */
    public IndexWriter getWriter() {
        return writer;
    }

    /**
     * @param writer the writer to set
     */
    public void setWriter(IndexWriter writer) {
        this.writer = writer;
    }

    @Override
    public void run() {
        long timeStart = System.currentTimeMillis();
        IndexerProperties indexerProperties = IndexerProperties.getInstance();
        Connection connection = null;
        try {
            connection = JDBCIndexer.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setFetchSize(Integer.parseInt(indexerProperties.getProperty(Property.INDEXER_DB_FETCHSIZE)));
            
            // Preenche os parametros na ordem enviada
            int i = 1;
            for (String param : params) {
                preparedStatement.setString(i++, param);
            }
            preparedStatement.setInt(i, start+1);
            preparedStatement.setInt(i+1, end);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet != null) {
                while (resultSet.next()) {
                    int columnCount = resultSet.getMetaData().getColumnCount();
                    for (int j = 1; j <= columnCount; j++) {
                        String columnLabel = resultSet.getMetaData().getColumnLabel(j);
                        String columnValue = StringUtils.isBlank(resultSet.getString(j)) ? "" : /*getNormalizedValue(columnLabel,*/ resultSet.getString(j)/*)*/;
                        Field field = document.getField(columnLabel);
                        if (field == null) {
                            field = new Field(columnLabel, 
                                              columnValue,
                                              indexerProperties.getStore(columnLabel), 
                                              indexerProperties.getIndex(columnLabel), 
                                              indexerProperties.getTermVector(columnLabel));
                            document.add(field);
                        } else {
                            field.setValue(columnValue);
                        }
                    }
                    writer.addDocument(document);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            long timeEnd = System.currentTimeMillis();
            System.out.println("Indexed from ["+(start+1)+"] to ["+end+"] in " + (timeEnd-timeStart) +"ms ");
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    // Nothing to do
                }
            }
        }
    }
    
    
    
}
