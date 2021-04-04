package ilia.nemankov.repository;

import ilia.nemankov.model.User;
import ilia.nemankov.service.UsersFileException;
import org.springframework.stereotype.Repository;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.text.ParseException;

@Repository
public interface UserRepository {

    User findByLogin(String login) throws UsersFileException;

    User save(User user) throws UsersFileException;

}
