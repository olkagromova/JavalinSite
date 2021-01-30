package org.example;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.Collections;


public class App {
    public static User user = new User();
    public static Data data = new Data();
    public static void main( String[] args )
    {

        Javalin javalin = Javalin.create().start(2014);
        javalin.get("/", ctx ->{
            System.out.println(ctx.status());
            ctx.render("login.jte");
        });
        javalin.get("/registration", ctx ->{
            System.out.println(ctx.status());
            ctx.render("regist.jte");
        });
        javalin.get("/login", ctx ->{
            System.out.println(ctx.status());
            ctx.render("login.jte");
        });
        javalin.get("/incorrectpass", ctx ->{
            System.out.println(ctx.status());
            ctx.render("incorrect_password.jte");
        });
        javalin.post("/api/auth/", ctx ->{
            System.out.println(ctx.formParam("login"));
            System.out.println(ctx.formParam("pass"));
            String login = ctx.formParam("login");
            String pass = ctx.formParam("pass");
            data.connect(login);
            if (data.auth(login, pass)){
                System.out.println("GOOD");
                ctx.cookie("login", login);
                ctx.redirect("/home");
            }else {
                System.out.println("BAD");
                ctx.redirect("/incorrectpass");
            }
        });
        javalin.post("/api/transfer/", ctx->{
           String id = ctx.formParam("id");
           String balanace = ctx.formParam("amount");
           if(data.getId("login") != Integer.valueOf(id) &&
                   data.getBalance(ctx.cookie("login")) >= Double.valueOf(balanace)){
               data.transfer(data.getId(ctx.cookie("login"))
                       ,Integer.valueOf(id),
                       data.getBalance(ctx.cookie("login")),
                       Double.valueOf(balanace));
               ctx.redirect("/home");
           }


        });
        javalin.get("/home", App::renderMainPage);
        javalin.post("/api/register/", ctx ->{
            System.out.println(ctx.formParam("login"));
            System.out.println(ctx.formParam("pass"));
            System.out.println(ctx.formParam("repass"));
            String login = ctx.formParam("login");
            String pass = ctx.formParam("pass");
            String repass = ctx.formParam("repass");
            data.connect(login);
            if (data.register(login, pass, repass) == true){
                System.out.println("GOOD");
                ctx.cookie("login", login);
                ctx.redirect("/home");
            }else {
                System.out.println("BAD");
            }
        });
    }
    public static void renderMainPage(Context ctx){
        Data data = new Data();
        User user = new User();
        user.id = data.getId(ctx.cookie("login"));
        user.balance = data.getBalance(ctx.cookie("login"));
        ctx.render("home.jte", Collections.singletonMap("user", user));
    }
}
