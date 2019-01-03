package org.lectify.query;

import org.lectify.query.model.entity.Book;
import org.lectify.query.model.entity.IndustryIdentifier;
import org.lectify.query.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class Application implements CommandLineRunner{

    @Autowired
    BookRepository bookRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        Book book = new Book();
        book.setBookId(UUID.randomUUID());
        book.setTitle("Tom Clancy's EndWar: The Missing");
        book.setAuthors(Arrays.asList("Tom Clancy","Peter Telep"));
        book.setPublisher("Penguin");
        book.setPublishedDate("2013-09-03");
        book.setDescription("After a devastating nuclear exchange in the Middle East, America and Russia stand on opposite sides in the quest for the world’s resources. While on a recon mission over Russia, Joint Strike Force pilot Major Stephanie Halverson tests a revolutionary new radar device—until she is shot down. In the jungles of Ecuador, relentless Marine Raider Captain Mikhail “Lex” Alexandrov pursues a wanted terrorist—and stumbles on an international conspiracy that will take him and his team into battle. On an island off Japan, a former Russian spy is hunted by her comrades, and her only way out could be to defect to the West. Each of their fates intertwines with a deadly cabal thought to have been destroyed, but it was only wounded. And now it has returned—stronger than ever… Based on Ubisoft’s bestselling game, Tom Clancy’s EndWar®");
        List<IndustryIdentifier> isbn = new ArrayList<IndustryIdentifier>(){
            {
                add(new IndustryIdentifier("ISBN_10","1101615982"));
                add(new IndustryIdentifier("ISBN_13","9781101615980"));
            }
        };
        book.setIndustryIdentifiers(isbn);
        book.setCategories(Arrays.asList("Fiction"));
        book.setPageCount(448);
        bookRepository.save(book)
                //.block()
        ;

    }
}
