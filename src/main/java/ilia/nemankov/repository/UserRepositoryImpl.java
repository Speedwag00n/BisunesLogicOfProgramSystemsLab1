package ilia.nemankov.repository;

import ilia.nemankov.model.User;
import ilia.nemankov.service.LoginInUseException;
import ilia.nemankov.service.UsersFileException;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Override
    public User findByLogin(String login) {
        try {
            Document document = getDocument();
            NodeList users = document.getElementsByTagName("user");
            for (int i = 0; i < users.getLength(); i++) {
                Element element = (Element) users.item(i);

                if (element.getElementsByTagName("login").item(0).getTextContent().equals(login)) {
                    User user = new User();
                    user.setId(Integer.parseInt(element.getAttribute("id")));
                    user.setLogin(element.getElementsByTagName("login").item(0).getTextContent());
                    user.setPassword(element.getElementsByTagName("password").item(0).getTextContent());
                    user.setLastLogout(getDateFormat().parse(element.getElementsByTagName("lastLogout").item(0).getTextContent()));
                    user.setBonuses(Integer.parseInt(element.getElementsByTagName("bonuses").item(0).getTextContent()));
                    Set<String> roles = new HashSet<>();
                    user.setRoles(roles);
                    String rolesString = element.getElementsByTagName("roles").item(0).getTextContent();
                    if (rolesString.length() > 0) {
                        Collections.addAll(roles, element.getElementsByTagName("roles").item(0).getTextContent().split(","));
                    }

                    return user;
                }
            }
        } catch (IOException | SAXException | ParserConfigurationException | ParseException e) {
            throw new UsersFileException(e);
        }

        return null;
    }

    @Override
    public User save(User user) {
        try {
            Document document = getDocument();
            Element root = document.getDocumentElement();

            boolean isLoginInUse = false;

            NodeList users = document.getElementsByTagName("user");
            for (int i = 0; i < users.getLength(); i++) {
                Element node = (Element) users.item(i);

                if (node.getElementsByTagName("login").item(0).getTextContent().equals(user.getLogin())) {
                    isLoginInUse = true;
                }

                if (user.getId() == Integer.parseInt(node.getAttribute("id"))) {

                    NodeList userChildren = node.getChildNodes();
                    for (int j = 0; j < userChildren.getLength(); j++) {
                        switch (userChildren.item(j).getNodeName()) {
                            case "login":
                                userChildren.item(j).setTextContent(user.getLogin());
                                break;
                            case "password":
                                userChildren.item(j).setTextContent(user.getPassword());
                                break;
                            case "lastLogout":
                                userChildren.item(j).setTextContent(getDateFormat().format(user.getLastLogout()));
                                break;
                            case "roles":
                                userChildren.item(j).setTextContent(String.join(",", user.getRoles().toArray(new String[0])));
                                break;
                            case "bonuses":
                                userChildren.item(j).setTextContent(user.getBonuses() + "");
                                break;
                        }
                    }
                    saveFile(document);

                    return user;
                }
            }

            if (isLoginInUse) {
                throw new LoginInUseException("Login isn't unique");
            }

            // User doesn't exist. Create new one
            int nextId = Integer.parseInt(document.getElementsByTagName("nextId").item(0).getTextContent());
            NodeList rootChildren = root.getChildNodes();
            for (int i = 0; i < rootChildren.getLength(); i++) {
                Node node = rootChildren.item(i);

                if (node.getNodeName().equals("nextId")) {
                    node.setTextContent((nextId + 1) + "");
                }
            }
            Element newUser = document.createElement("user");

            newUser.setAttribute("id", nextId + "");
            Element login = document.createElement("login");
            login.appendChild(document.createTextNode(user.getLogin()));
            Element password = document.createElement("password");
            password.appendChild(document.createTextNode(user.getPassword()));
            Element lastLogout = document.createElement("lastLogout");
            lastLogout.appendChild(document.createTextNode(getDateFormat().format(user.getLastLogout())));
            Element roles = document.createElement("roles");
            roles.appendChild(document.createTextNode(String.join(",", user.getRoles().toArray(new String[0]))));
            Element bonuses = document.createElement("bonuses");
            bonuses.appendChild(document.createTextNode(user.getBonuses() + ""));

            newUser.appendChild(login);
            newUser.appendChild(password);
            newUser.appendChild(lastLogout);
            newUser.appendChild(roles);
            newUser.appendChild(bonuses);

            root.appendChild(newUser);

            saveFile(document);
        } catch (TransformerException | IOException | SAXException | ParserConfigurationException e) {
            throw new UsersFileException(e);
        }

        return user;
    }

    private Document getDocument() throws IOException, SAXException, ParserConfigurationException {
        File usersFile = ResourceUtils.getFile("classpath:users.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(usersFile);
    }

    private DateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    private void saveFile(Document document) throws TransformerException, FileNotFoundException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(ResourceUtils.getFile("classpath:users.xml"));

        transformer.transform(source, result);
    }
}
