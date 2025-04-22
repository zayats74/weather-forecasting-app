package org.example.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CityRepository {
    List<String> Allcities = new ArrayList<>();

    public CityRepository() throws IOException {
        try(
                BufferedReader reader = Files.newBufferedReader(Paths.get("src/main/java/org/example/file/cities.txt"))  )      {
            Allcities = reader.lines().toList();
        }
        catch(
                IOException ex){

            System.out.println(ex.getMessage());
        }
    }

    public List<String> getAllcities() {
        return Allcities;
    }
}
