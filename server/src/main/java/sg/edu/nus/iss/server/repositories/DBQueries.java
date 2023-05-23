package sg.edu.nus.iss.server.repositories;

public class DBQueries {

    // MYSQL
    public static final String SQL_SELECT_IMAGE_BY_ID= "select * from pictures where p_id = ?";
    public static final String SQL_INSERT_IMAGE= "insert into pictures (picture) values (?)";

    // MONGODB
    // db.articles.find({ p_id: 1})
    // db.articles.insert({
    //     p_id: 1,
    //     title: "My article",
    //     content: "My article content"
    // })
}
