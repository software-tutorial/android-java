package at.ac.htl.restclient;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.val;

import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class MainActivity extends Activity {
    @Data
    static class ToDo {
        int id, userId;
        String title;
        boolean completed;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        val mainThread = new Handler(Looper.getMainLooper());
        CompletableFuture
            .supplyAsync(this::loadAllTodos)
            .thenAccept(todos -> mainThread.post(() -> fillListViewWithTitlesOf(todos)));
    }
    @SneakyThrows
    ToDo[] loadAllTodos() {
        val url = new URL("https://jsonplaceholder.typicode.com/todos");
        return new ObjectMapper().readValue(url, ToDo[].class);
    }
    void fillListViewWithTitlesOf(ToDo[] todos) {
        val titlesOfTodos = Arrays.stream(todos)
                .map(ToDo::getTitle)
                .collect(Collectors.toList());
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titlesOfTodos));
    }
}