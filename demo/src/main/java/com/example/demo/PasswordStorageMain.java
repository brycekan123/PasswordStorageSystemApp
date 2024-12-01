package com.example.demo;
    
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@SpringBootApplication
@RestController
public class PasswordStorageMain implements CommandLineRunner {

    private Map<String, String> passwordStore = new HashMap<>();
    private static final String FILE_PATH = "passwords.json";
    private final ObjectMapper objectMapper = new ObjectMapper(); 
    private final ApplicationContext context;

    public PasswordStorageMain(ApplicationContext context) {
        this.context = context;
        loadFromFile(); 
    }

    public static void main(String[] args) {
        SpringApplication.run(PasswordStorageMain.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        String command;
        while (true) {
            commandPrompts();
            command = scanner.nextLine();
            String[] parts = command.split(" ");
            String action = parts[0].toLowerCase();
            if (action.equals("quit")) {
                System.out.println("Shutting down...");
                scanner.close();
                saveToFile();
                SpringApplication.exit(context, () -> 0);
                System.exit(0);
                return;
            } else if (action.equals("add")) {
                addPassword(parts,scanner);
            } else if (action.equals("remove")) {
                removePassword(parts);
            } else if (action.equals("getall")) {
                openBrowser("http://localhost:8080/getAll");
            } else if (action.equals("get")) {
                getPasswordUrl(parts);
            } else if (action.equals("removeall")) {
                System.out.println("Removing all passwords from database!");
                passwordStore.clear();
            } else {
                System.out.println("Invalid command. Available commands: add website password, remove website, get website, getall, removeall, quit");
            }
        }
    }

    private void addPassword(String[] parts,Scanner scanner) {
        if (parts.length == 3) {
            String website = parts[1];
            String password = parts[2];
            if (passwordStore.containsKey(website)){
                System.out.println(website + " is already in database. Do you mean to update the password?");
                String response = scanner.nextLine();
                response = response.toLowerCase();
                if (response.equals("yes")){
                    passwordStore.put(website, password);
                    saveToFile();
                    System.out.println(website + " has been UPDATED to the Password Storage System!");
                } else if (response.equals("no")) {
                    System.out.println(website+ " was not UPDATED to the Password Storage System!");
                }else{
                    System.out.println("Invalid command!");
                }
            }else{
                passwordStore.put(website, password);
                saveToFile();
                System.out.println(website + " has been ADDED to the Password Storage System!");
            }
            
        } else {
            System.out.println("Invalid format. Use: add website password");
        }
    }

    private void removePassword(String[] parts) {
        if (parts.length == 2) {
            String website = parts[1];
            if (passwordStore.remove(website) != null) {
                saveToFile(); // Save changes to file
                System.out.println(website + " has been REMOVED to the Password Storage System!");
            } else {
                System.out.println(website  + " was not found in the Password Storage System");
            }
        } else {
            System.out.println("Invalid format. Use: remove website");
        }
    }

    private void getPasswordUrl(String[] parts) {
        String url = "";
        if (parts.length == 2) {
            String website = parts[1];
            if (passwordStore.containsKey(website)) {
                url = "http://localhost:8080/getAPassword?website=" + website;
                openBrowser(url);
            }else{
                System.out.println(website + " is NOT in Password Storage System");
            }
        } else {
            System.out.println("Invalid format. Use: get website");
        }
    }

    @GetMapping("/getAll")
    public Map<String, String> getAllPasswords() {
        return passwordStore;
    }
    @GetMapping("/getAPassword")
    public ResponseEntity<String> getPassword(@RequestParam String website) {
        String password = passwordStore.get(website);
        if (password != null) {
            return ResponseEntity.ok("Password for website " + website + ": " + password);
        } else {
            return ResponseEntity.status(404).body("Website " + website + " was not found");
        }
    }
    private void saveToFile() {
        try {
            objectMapper.writeValue(new File(FILE_PATH), passwordStore);
        } catch (IOException e) {
            System.err.println("Error saving passwords: " + e.getMessage());
        }
    }
    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try {
                passwordStore = objectMapper.readValue(file, new TypeReference<Map<String, String>>() {});
            } catch (IOException e) {
                System.err.println("Error loading passwords: " + e.getMessage());
            }
        }
    }
    private void commandPrompts() {
        System.out.println("---*****---");
        System.out.println("Enter Command");
        System.out.println("Option 1: Add www.example.com password");
        System.out.println("Option 2: Remove www.example.com");
        System.out.println("Option 3: Get www.example.com ");
        System.out.println("Option 4: GetAll");
        System.out.println("Option 5: RemoveAll");
        System.out.println("Option 6: Quit");
        System.out.println("---*****---");
    }

    private void openBrowser(String url) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                Runtime.getRuntime().exec("cmd /c start " + url);
            } else if (os.contains("mac")) {
                Runtime.getRuntime().exec("open " + url);
            } else if (os.contains("nix") || os.contains("nux")) {
                Runtime.getRuntime().exec("xdg-open " + url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
