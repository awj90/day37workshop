package sg.edu.nus.iss.server.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.server.models.Article;
import sg.edu.nus.iss.server.repositories.ArticleRepository;

@Service
public class ArticleService {
    
    @Autowired
    private ArticleRepository articleRepository;

    public Integer insertArticle(String title, String content, byte[] picture) throws Exception {
        return articleRepository.insertArticle(title, content, picture);
    }

    public Optional<Article> getArticleById(Integer p_id) {
        return articleRepository.getArticleById(p_id);
    }
}
