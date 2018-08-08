package com.example.vaibhav.leavemanagement.servicegenerator;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;


public class ServiceGenerator {


    // No need to instantiate this class.
    public ServiceGenerator() {
    }

    public static <S> S createService(Class<S> serviceClass) {


        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(60*3, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(60*3, TimeUnit.SECONDS);

        //String BASE_URL = "http://appmomos.com/essmart/api/";
        String BASE_URL = "http://appmomos.com/essmartold/";
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setConverter(new DynamicJsonConverter())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                //.setLog(msg -> Log.d("Retro",msg))
                .setClient(new OkClient(okHttpClient));

        RestAdapter adapter = builder.build();

        return adapter.create(serviceClass);
    }


    static class DynamicJsonConverter implements Converter {

        @Override
        public Object fromBody(TypedInput typedInput, Type type) throws ConversionException {
            try {
                InputStream in = typedInput.in(); // convert the typedInput to String
                String string = fromStream(in);
                in.close(); // we are responsible to close the InputStream after use

                if (String.class.equals(type)) {
                    return string;
                } else {
                    return new Gson().fromJson(string, type); // convert to the supplied type, typically Object, JsonObject or Map<String, Object>
                }
            } catch (Exception e) { // a lot may happen here, whatever happens
                throw new ConversionException(e); // wrap it into ConversionException so retrofit can process it
            }
        }

        @Override
        public TypedOutput toBody(Object object) { // not required
            return null;
        }

        private static String fromStream(InputStream in) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
                out.append("\r\n");
            }
            return out.toString();
        }
    }
}