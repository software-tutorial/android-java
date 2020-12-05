package at.ac.htl.restclient;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.val;

import java.net.URL;
import java.util.Arrays;
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

        Observable
                .fromCallable(this::loadAllTodos)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::fillListViewWithTitlesOf);
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