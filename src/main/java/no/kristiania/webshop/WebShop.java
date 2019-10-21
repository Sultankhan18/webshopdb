package no.kristiania.webshop;

import org.postgresql.ds.PGSimpleDataSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Properties;

public class WebShop {

    private PGSimpleDataSource dataSource;

    private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    private WebShop() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader("webshop.properties"));

        dataSource = new PGSimpleDataSource();

        dataSource.setUrl("jdbc:postgresql://localhost:5432/mywebshopdb");
        dataSource.setUser("mywebshop");
        dataSource.setPassword(properties.getProperty("dataSource.password"));
    }

    public static void main(String[] args) throws IOException, SQLException {
        new WebShop().run();
    }

    private void run() throws IOException, SQLException {
        System.out.println("Please type the name of a new product");

        String productName = new BufferedReader(new InputStreamReader(System.in)).readLine();

        ProductDao productDao = new ProductDao(dataSource);
        productDao.insert(productName);

        System.out.println("Current products " + productDao.listAll());
    }

}