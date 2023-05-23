package sg.edu.nus.iss.server.repositories;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import sg.edu.nus.iss.server.models.Article;

import static sg.edu.nus.iss.server.repositories.DBQueries.*;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class ArticleRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private MongoTemplate mongoTemplate;

    @Transactional(rollbackFor=Exception.class)
    public Integer insertArticle(String title, String content, byte[] picture) throws Exception {

        // since MySQL supports transactions while MongoDB does not (i.e MongoDB cannot rollback, MySQL's insert should be done first) 

        // inserts image to mySQL database
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT_IMAGE, Statement.RETURN_GENERATED_KEYS);
            ps.setBytes(1, picture);
            return ps;
        }, keyHolder);

        // retrieve p_id from autoincremented mySQL record
        Integer primaryKey = keyHolder.getKey().intValue();

        // insert title and content to MongoDB together with the same p_id
        Article article = new Article();
        article.setP_id(primaryKey);
        article.setTitle(title);
        article.setContent(content);
        mongoTemplate.insert(article, "articles");

        return primaryKey;
    }

    public Optional<Article> getArticleById(Integer p_id) {

        Criteria criteria = Criteria.where("p_id").is(p_id);
        Query query = Query.query(criteria);
        List<Document> documents = mongoTemplate.find(query, Document.class, "articles");

        if (documents.size() < 1) {
            return Optional.empty();
        } else {
            return jdbcTemplate.query(SQL_SELECT_IMAGE_BY_ID, resultSet -> {
                if (!resultSet.next()) {
                    return Optional.empty();
                } else {
                    Article article = new Article();
                    article.setPicture(resultSet.getBytes("picture"));
                    article.setP_id(documents.get(0).getInteger("p_id"));
                    article.setTitle(documents.get(0).getString("title"));
                    article.setContent(documents.get(0).getString("content"));
                    return Optional.of(article);
                }
            }, p_id);
        }

    }
}
