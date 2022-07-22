package com.lwc.flowcontrol.commons.config.mongodb;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.data.mongodb.core.MongoTemplate;

/* 指定多个日志级别 */
//@Data
//public class MongoDBAppender extends UnsynchronizedAppenderBase<LoggingEvent> {
//
//    MongoDBProperties properties = new MongoDBProperties();
////    private MongoClient mongo = MongoClients.create(properties.getUri());
//    private MongoClient mongo = MongoClients.create("mongodb://localhost:27017/logdb");
//    private String dbHost = "127.0.0.1";
//    private int dbPort = 27017;
//    private String dbName = "logdb";
//    private String colName = "logdb";
//    private MongoCollection<Document> get_collection;
//
//    @Override
//    public void start() {
//        try {
//            MongoDatabase db = mongo.getDatabase(dbName);
//            get_collection = db.getCollection(colName);
//        } catch (Exception e) {
//            addStatus(new ErrorStatus("Failed to initialize MondoDB", this, e));
//            return;
//        }
//        super.start();
//    }
//
////    @Override
////    public void stop() {
////        mongo.close();
////        super.stop();
////    }
//
//    @Override
//    protected void append(LoggingEvent e) {
//        /* 考虑异步执行文件写入操作 */
//        Document document = new Document().
//                append("ts", new Date(e.getTimeStamp())).
//                append("msg", e.getFormattedMessage()).
//                append("level", e.getLevel().toString()).
//                append("logger", e.getLoggerName()).
//                append("thread", e.getThreadName());
//        if(e.hasCallerData()) {
//            StackTraceElement st = e.getCallerData()[0];
//            String callerData = String.format("%s.%s:%d", st.getClassName(), st.getMethodName(), st.getLineNumber());
//            document.append("caller", callerData);
//        }
//        get_collection.insertOne(document);
//    }
//}

/* 指定一个日志级别或不设置日志级别限制 */
public class MongoDBAppender extends UnsynchronizedAppenderBase<LoggingEvent> {

    /* 连接接的MongoDB数据库名称*/
    private final static String DB_NAME= "logdb";

    private  MongoTemplate mongoTemplate;

    @Override
    public void start() {
        MongoClient mongoClients = MongoClients.create();
        mongoTemplate = new MongoTemplate(mongoClients,DB_NAME);
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    protected void append(LoggingEvent e) {
        /* 考虑异步执行文件写入操作 */
        final BasicDBObject doc = new BasicDBObject();
        doc.append("level", e.getLevel().toString());
        doc.append("logger", e.getLoggerName());
        doc.append("thread", e.getThreadName());
        doc.append("message", e.getFormattedMessage());
        mongoTemplate.insert(doc,"logdb");
    }
}

