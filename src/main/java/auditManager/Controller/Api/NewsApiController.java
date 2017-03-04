package auditManager.Controller.Api;

import auditManager.Controller.Service.UserAuthority;
import auditManager.Model.*;
import auditManager.Model.DTO.MessageDTO;
import auditManager.Model.DTO.NewsDTO;
import auditManager.Model.DTO.ThemeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nettle on 2017/2/27.
 */
@Controller
@RequestMapping("/api/news")
public class NewsApiController {

    @Autowired
    NewsRepository newsRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    private @ResponseBody List<NewsDTO> getAllNews(
            @SessionAttribute(value="currentUser", required = false) User currentUser
    ) {
        List<NewsDTO> newsDTOList = new ArrayList<NewsDTO>();
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.ARTICLE_SYSTEM, currentUser)) {
            List<News> newsList = newsRepository.findAll();
            for (News news: newsList)
                newsDTOList.add(new NewsDTO(news));
        }
        return newsDTOList;
    }

    @RequestMapping(value = "/trash", method = RequestMethod.GET)
    private @ResponseBody List<NewsDTO> getAllTrashNews(
            @SessionAttribute(value="currentUser", required = false) User currentUser
    ) {
        List<NewsDTO> newsDTOList = new ArrayList<NewsDTO>();
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.ARTICLE_SYSTEM, currentUser)) {
            List<News> newsList = newsRepository.findAllByDeleted(true);
            for (News news: newsList)
                newsDTOList.add(new NewsDTO(news));
        }
        return newsDTOList;
    }

    @RequestMapping(value = "/{id:[0-9]+}", method = RequestMethod.GET)
    private ResponseEntity<NewsDTO> getSingleNews(
            @PathVariable(value="id") Long id,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    ) {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.POSTER_SYSTEM, currentUser)) {
            try {
                News news = newsRepository.findById(id);
                Article article = articleRepository.findById(news.getArticleId());
                return new ResponseEntity<NewsDTO>(new NewsDTO(news, article), HttpStatus.OK);
            }   catch (Exception e) {
                return new ResponseEntity<NewsDTO>((NewsDTO) null, HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<NewsDTO>((NewsDTO) null, HttpStatus.BAD_REQUEST);
    }

    /* create */
    @RequestMapping(value = "", method = RequestMethod.POST)
    private ResponseEntity<NewsDTO> createNews(
            @RequestBody NewsDTO newsDTO,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    ) {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.ARTICLE_SYSTEM, currentUser)) {

            Article article = new Article();
            article.setCreated(new Date().getTime());
            article.setUpdated(article.getCreated());
            article.setContent(newsDTO.getContent());
            article.setUserId(currentUser.getId());

            try {
                articleRepository.save(article);
            }   catch (Exception e) {
                return new ResponseEntity<NewsDTO>((NewsDTO) null, HttpStatus.BAD_REQUEST);
            }

            News news = new News();
            news.setTitle(newsDTO.getTitle());
            news.setAttachments(newsDTO.getAttachments());
            news.setImages(newsDTO.getImages());
            news.setTheme(newsDTO.getTheme());
            news.setArticleId(article.getId());
            news.setUserName(currentUser.getUsername());
            news.setUpdated(article.getUpdated());

            try {
                newsRepository.save(news);
            }   catch (Exception e) {
                return new ResponseEntity<NewsDTO>((NewsDTO) null, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<NewsDTO>(new NewsDTO(news), HttpStatus.OK);
        }
        return new ResponseEntity<NewsDTO>((NewsDTO) null, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{id:[0-9]+}", method = RequestMethod.PUT)
    private ResponseEntity<NewsDTO> updateNews(
            @PathVariable(value="id") Long id,
            @RequestBody NewsDTO newsDTO,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.POSTER_SYSTEM, currentUser)) {
            News news = newsRepository.findById(id);
            Article article = articleRepository.findById(news.getArticleId());

            article.setContent(newsDTO.getContent());
            article.setUpdated(new Date().getTime());

            try {
                articleRepository.save(article);
            }   catch (Exception e) {
                return new ResponseEntity<NewsDTO>((NewsDTO) null, HttpStatus.BAD_REQUEST);
            }

            news.setTitle(newsDTO.getTitle());
            news.setTheme(newsDTO.getTheme());
            news.setImages(newsDTO.getImages());
            news.setAttachments(newsDTO.getAttachments());
            news.setUpdated(article.getUpdated());

            try {
                newsRepository.save(news);
            }   catch (Exception e) {
                return new ResponseEntity<NewsDTO>((NewsDTO) null, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<NewsDTO>(new NewsDTO(news, article), HttpStatus.OK);
        }
        return new ResponseEntity<NewsDTO>((NewsDTO) null, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{id:[0-9]+}/publish", method = RequestMethod.PUT)
    private ResponseEntity<MessageDTO> publishNews(
            @PathVariable(value="id") Long id,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.POSTER_SYSTEM, currentUser)) {
            News news = newsRepository.findById(id);
            news.setPublished(true);
            try {
                newsRepository.save(news);
            }   catch (Exception e) {
                return new ResponseEntity<MessageDTO>(new MessageDTO("新闻发布失败"), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<MessageDTO>(new MessageDTO("成功发布该新闻"), HttpStatus.OK);
        }
        return new ResponseEntity<MessageDTO>(new MessageDTO("新闻发布失败"), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{id:[0-9]+}/unpublished", method = RequestMethod.PUT)
    private ResponseEntity<MessageDTO> unpublishedNews(
            @PathVariable(value="id") Long id,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.POSTER_SYSTEM, currentUser)) {
            News news = newsRepository.findById(id);
            news.setPublished(false);
            try {
                newsRepository.save(news);
            }   catch (Exception e) {
                return new ResponseEntity<MessageDTO>(new MessageDTO("取消新闻发布失败"), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<MessageDTO>(new MessageDTO("成功取消发布该新闻"), HttpStatus.OK);
        }
        return new ResponseEntity<MessageDTO>(new MessageDTO("取消新闻发布失败"), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{id:[0-9]+}/recover", method = RequestMethod.PUT)
    private ResponseEntity<MessageDTO> recoverNews(
            @PathVariable(value="id") Long id,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.POSTER_SYSTEM, currentUser)) {
            News news = newsRepository.findById(id);
            news.setDeleted(false);
            try {
                newsRepository.save(news);
            }   catch (Exception e) {
                return new ResponseEntity<MessageDTO>(new MessageDTO("恢复新闻失败"), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<MessageDTO>(new MessageDTO("成功恢复该新闻"), HttpStatus.OK);
        }
        return new ResponseEntity<MessageDTO>(new MessageDTO("恢复新闻失败"), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{id:[0-9]+}", method = RequestMethod.DELETE)
    private ResponseEntity<MessageDTO> deleteModule(
            @PathVariable(value="id") Long id,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.POSTER_SYSTEM, currentUser)) {
            News news = newsRepository.findById(id);
            news.setDeleted(true);
            try {
                newsRepository.save(news);
            }   catch (Exception e) {
                return new ResponseEntity<MessageDTO>(new MessageDTO("新闻删除失败"), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<MessageDTO>(new MessageDTO("成功删除新闻"), HttpStatus.OK);
        }
        return new ResponseEntity<MessageDTO>(new MessageDTO("新闻删除失败"), HttpStatus.BAD_REQUEST);
    }
}
