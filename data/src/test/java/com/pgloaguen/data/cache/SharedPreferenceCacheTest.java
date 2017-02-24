package com.pgloaguen.data.cache;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;

import static com.pgloaguen.data.cache.SharedPreferenceCache.EMPTY;
import static com.pgloaguen.data.cache.SharedPreferenceCache.PARSER_SEP;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

/**
 * Created by paul on 21/02/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class SharedPreferenceCacheTest {

    @Mock
    SharedPreferences sharedPreferences;

    private Gson gson;
    private SharedPreferenceCache<String> sharedPreferenceCache;

    @Before
    public void setup() {
        gson = new GsonBuilder().create();
        sharedPreferenceCache = new SharedPreferenceCache<String>(sharedPreferences, gson, String.class);
    }

    @Test
    public void getWithNoValue() throws Exception {
        given(sharedPreferences.getString(anyString(), anyString())).willReturn(EMPTY);
        sharedPreferenceCache.get("key").test()
                .assertNoErrors()
                .assertNoValues();
    }

    @Test
    public void getWithOneValue() throws Exception {
        given(sharedPreferences.getString(anyString(), anyString())).willReturn("toto");
        sharedPreferenceCache.get("key").test().assertResult("toto");
    }

    @Test
    public void getWithMultipleValue() throws Exception {
        given(sharedPreferences.getString(anyString(), anyString())).willReturn("toto" + PARSER_SEP + "titi");
        sharedPreferenceCache.get("key").test().assertResult("toto");
    }


    @Test
    public void getAllWithNoValue() throws Exception {
        given(sharedPreferences.getString(anyString(), anyString())).willReturn(EMPTY);
        sharedPreferenceCache.getAll("key").test().assertNoErrors().assertNoValues();
    }

    @Test
    public void getAll() throws Exception {
        given(sharedPreferences.getString(anyString(), anyString())).willReturn("toto" + PARSER_SEP + "titi");
        sharedPreferenceCache.getAll("key").flatMapObservable(Observable::fromIterable).test().assertResult("toto", "titi");;
    }
}