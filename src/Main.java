/**
 * Created by jakefroeb on 9/21/16.
 */
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;


public class Main {
        static HashMap<String, User> users = new HashMap<>();
        static String warning;

    public static void main(String[] args) {




        Spark.get("/", ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");
                    String password = session.attribute("password");
                    warning = session.attribute("warning");
                    User user = users.get(userName);
                    HashMap m = new HashMap();
                    m.put("warning", warning);
                        if (user == null) {
                            return new ModelAndView(m, "index.html");
                        } else {
                            if (user.password.equals(password)) {
                                m.put("name", user.userName);
                                m.put("password", user.password);
                                m.put("messages", user.messages);
                                return new ModelAndView(m, "messages.html");
                            } else {
                                warning = "Wrong Password";
                                m.put("warning", warning);
                                return new ModelAndView(m, "index.html");
                            }

                        }


                }),
                new MustacheTemplateEngine()
        );

        Spark.post("/create-user",
                ((request, response) -> {
                    String name = request.queryParams("loginName");
                    String password = request.queryParams("password");
                    User user = users.get(name);
                    if(name.length()>0 && password.length()>0) {
                        if (user == null) {
                            if (!(name == null || password == null)) {
                                user = new User(name, password);
                                users.put(name, user);
                            } else {
                                warning = "WRONG PASSWORD";
                            }

                        }
                    }else{
                        warning = "Please enter a username and password";
                    }
                    Session session = request.session();
                    session.attribute("userName", name);
                    session.attribute("password", password);
                    session.attribute("warning", warning);
                    response.redirect("/");

                    return "";
                })

        );

        Spark.post("/create-message",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("userName");
                    User user = users.get(name);
                    String messageText = request.queryParams("message");
                    Message message = new Message(messageText);
                    user.messages.add(message);
                    response.redirect("/");
                    return "";
                })

        );

        Spark.post("/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                }));

        Spark.post("/deleteMessage",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("userName");
                    int messageIndex = (Integer.parseInt(request.queryParams("deleteMessageNumber"))-1);
                    User user = users.get(name);
                    user.messages.remove(messageIndex-1);
                    response.redirect("/");
                    return "";
                }));
        Spark.post("/editMessage",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("userName");
                    int messageIndex = (Integer.parseInt(request.queryParams("editMessageNumber"))-1);
                    String text = request.queryParams("editText");
                    User user = users.get(name);
                    Message message = user.messages.get(messageIndex);
                    message.messageText = text;
                    response.redirect("/");
                    return "";
                }));


    }

}
