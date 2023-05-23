package sg.edu.nus.iss.server.controllers;

import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import sg.edu.nus.iss.server.models.Article;
import sg.edu.nus.iss.server.services.ArticleService;

@Controller
@RequestMapping
public class ArticleController {
    
    @Autowired
    private ArticleService articleService;

    @PostMapping(path="/upload", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ModelAndView insertArticle(@RequestPart String title, @RequestPart String content, @RequestPart MultipartFile picture, Model model) {
        ModelAndView mav = new ModelAndView();
        try {
            Integer p_id = articleService.insertArticle(title, content, picture.getBytes());
            Optional<Article> opt = articleService.getArticleById(p_id);
            if (opt.isEmpty()) {
                mav.addObject("errorMessage", "Not Found");
                mav.setViewName("error");
                mav.setStatus(HttpStatusCode.valueOf(404));
                return mav;
            }
            Article article = opt.get();
            mav.addObject("p_id", p_id);
            mav.addObject("title", article.getTitle());
            mav.addObject("content", article.getContent());
            mav.addObject("picture", Base64.getEncoder().encodeToString(article.getPicture())); // need to use Base64 encoding and put the resulting encoded string into the src attribute of the img element, prepended by 'data:image/jpeg;base64,'
            mav.setViewName("success");
            mav.setStatus(HttpStatusCode.valueOf(201));
            return mav;
        } catch (Exception ex) {
            mav.addObject("errorMessage", ex.getMessage());
            mav.setViewName("error");
            mav.setStatus(HttpStatusCode.valueOf(500));
            return mav;
        }
    }
}
