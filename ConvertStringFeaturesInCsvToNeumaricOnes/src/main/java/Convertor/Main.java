package Convertor;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        // get the path of the data
        var Reader = Paths.get(System.getProperty("user.dir"), "StringData.csv");

        // break the data into list of lists (lines)
        var ColumnData = Files.readAllLines(Reader, StandardCharsets.UTF_8).stream().
                map(i -> Arrays.asList(Arrays.copyOf(i.split(","), i.split(",").length)))
                .collect(Collectors.toList());

        // get the data header and remove it from the list of lists
        var Header = ColumnData.remove(0);

        // get the distinct values of the features
        var StringValues = Arrays.copyOf(ColumnData.stream().flatMap(List::stream).distinct().toArray(),
                ColumnData.stream().flatMap(List::stream).distinct().toArray().length,
                String[].class);

        // change the string values and convert them to numerical features as multiply of 10
        var result = ColumnData.stream().peek(i -> {
                for (var j = 0; j < i.size() - 1; j++)
                    for (var x = 0; x < StringValues.length; x++) {
                        i.set(j, i.get(j).replace((String) StringValues[x], 10 * (x + 1) + ""));
                    }
        }).collect(Collectors.toList());

        // add back the header to the data
        result.add(0, Header);

        // use open csv to write .csv file in java
        var Writer = new CSVWriter(new FileWriter(System.getProperty("user.dir") + "\\NumericData.csv"));
        // write each list (line) one by one
        for (var i : result)
            Writer.writeNext(Arrays.copyOf(i.toArray(), i.toArray().length, String[].class));
        // close the writer
        Writer.close();
    }
}
